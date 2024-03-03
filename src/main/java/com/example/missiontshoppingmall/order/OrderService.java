package com.example.missiontshoppingmall.order;

import com.example.missiontshoppingmall.utils.EntityFromOptional;
import com.example.missiontshoppingmall.item.entity.Item;
import com.example.missiontshoppingmall.order.dto.OrderConfirmDto;
import com.example.missiontshoppingmall.order.dto.OrderRequest;
import com.example.missiontshoppingmall.order.dto.OrderResponse;
import com.example.missiontshoppingmall.order.entity.ItemOrder;
import com.example.missiontshoppingmall.order.entity.PaymentStatus;
import com.example.missiontshoppingmall.order.entity.TransactionStatus;
import com.example.missiontshoppingmall.order.repo.OrderRepository;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import com.example.missiontshoppingmall.shoppingMall.dto.PaymentCancelDto;
import com.example.missiontshoppingmall.shoppingMall.dto.PaymentConfirmDto;
import com.example.missiontshoppingmall.utils.TossHttpInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final EntityFromOptional optional;
    private final CustomUserDetailsManager manager;
    private final OrderRepository orderRepo;
    private final TossHttpInterface tossService;

    // 쇼핑몰 상품 구매 (상품, 구매수량 기준으로 구매 요청)
    @Transactional
    public OrderResponse createOrder(Long itemId, OrderRequest dto) {
        // 상품을 찾고,
        Item foundItem = optional.getItem(itemId);
        // 그 상품의 재고가 구매수량보다 크면 구매요청 보냄
        if (foundItem.getStock() < dto.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        ItemOrder newOrder = ItemOrder.builder()
                .orderItem(foundItem)
                .buyer(manager.loadUserFromAuth())
                .amount(dto.getAmount())
                .totalPrice(dto.getAmount()*foundItem.getPrice())
                .paymentStatus(PaymentStatus.NOT_PAID)
                .transactionStatus(TransactionStatus.WAIT)
                .build();
        orderRepo.save(newOrder);
        return OrderResponse.fromEntity(newOrder);
    }

    // orderId로 결제요청하기

    // 결제 승인 요청 보내기
    public OrderResponse confirmPayment(PaymentConfirmDto dto) {
        // HTTP 요청 보냄
        HashMap<String, Object> tossPaymentObj = tossService.confirmPayment(dto);
        log.info("tossPaymentObj :: " + tossPaymentObj.toString());

        String itemOrderId = tossPaymentObj.get("orderName").toString().split("-")[0];
        // orderId로 Order 객체 찾아내기
        ItemOrder foundOrder = optional.gerOrder(Long.parseLong(itemOrderId));
        // 결제 성공시 재고가 갱신되어야 한다.
        Integer stockNow = foundOrder.getOrderItem().getStock(); // 현재재고
        foundOrder.getOrderItem().setStock(stockNow-foundOrder.getAmount()); // 재고 갱신.
        foundOrder.setPaymentStatus(PaymentStatus.PAID);
        foundOrder.setPaymentKey(tossPaymentObj.get("paymentKey").toString()); // 아직은 transaction완료가 아님.

        foundOrder = orderRepo.save(foundOrder);
        return OrderResponse.fromEntity(foundOrder);
    }

    // 구매요청 취소 (before 구매요청 수락) (본인확인)
    public OrderResponse cancelOrder(Long orderId, PaymentCancelDto dto) {
        // 1. 주문정보를 찾고
        ItemOrder foundOrder = optional.gerOrder(orderId);
        // 2. 로그인한 사용자가 주문한 사용자인지 확인
        manager.checkIdIsEqual(foundOrder.getBuyer().getAccountId());
        // 3. 구매요청 취소처리 (transaction status가 wait일 때만 가능)
        if (!foundOrder.getTransactionStatus().equals(TransactionStatus.WAIT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 이미 결제한 경우 구매취소시 결제된 금액 환불
        if (foundOrder.getPaymentStatus().equals(PaymentStatus.PAID)) {
            tossService.cancelPayment(foundOrder.getPaymentKey(), dto);
            // 재고 이전으로 갱신!!
            foundOrder.getOrderItem().setStock(foundOrder.getOrderItem().getStock() - foundOrder.getAmount());
        }
        // 모두 취소로 바꿈.
        foundOrder.setPaymentStatus(PaymentStatus.CANCELED);
        foundOrder.setTransactionStatus(TransactionStatus.CANCELED);
        orderRepo.save(foundOrder);
        return OrderResponse.fromEntity(foundOrder);
    }

    // 주인: 구매요청 수락 주인확인 필요
    // transaction status -> done
    public OrderResponse confirmOrder(Long orderId, OrderConfirmDto dto) {
        // 1. 주문정보를 찾고
        ItemOrder foundOrder = optional.gerOrder(orderId);
        // 2. 로그인한 사용자가 쇼핑몰 주인인지 확인
        manager.checkIdIsEqual(foundOrder.getOrderItem().getShoppingMall().getOwner().getAccountId());
        // 3. 주인이 구매요청 승인시
        if (dto.isConfirmation()) {
            // 이때 결제는 완료된 상태이며, 거래는 대기중인 상태라면!!
            if (foundOrder.getPaymentStatus().equals(PaymentStatus.PAID)
                    && foundOrder.getTransactionStatus().equals(TransactionStatus.WAIT)) {
                // 거래 완료 처리.
                foundOrder.setTransactionStatus(TransactionStatus.DONE);
                // 거래완료 일자 업데이트
                foundOrder.setTransactionDate(LocalDateTime.now());
                // 쇼핑몰 최근거래 일자도 함께 업데이트
                foundOrder.getOrderItem().getShoppingMall().setRecentOrderDate(LocalDateTime.now());
            } else { // 결제가 완료되지 않거나, 거래가 취소/완료된 상태라면 bad request
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        // 주인이 구매요청 불허시
        else {
            // 이유를 밝히고 취소처리 및 결제 환불
            foundOrder.setTransactionDeniedReason(dto.getDeniedReason());
            foundOrder.setTransactionStatus(TransactionStatus.CANCELED);
            // 결제 취소
            tossService.cancelPayment(
                    foundOrder.getPaymentKey(),
                    PaymentCancelDto.builder().cancelReason("쇼핑몰 측의 구매요청 취소").build());
            foundOrder.setPaymentStatus(PaymentStatus.CANCELED);
        }
        orderRepo.save(foundOrder);
        return OrderResponse.fromEntity(foundOrder);
    }
}
