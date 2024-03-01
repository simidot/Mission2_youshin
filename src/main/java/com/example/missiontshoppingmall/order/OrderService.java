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
import com.example.missiontshoppingmall.utils.PaymentConfirmDto;
import com.example.missiontshoppingmall.utils.TossHttpInterface;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    //todo: PaymentConfirmDto를 가로채보자,

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
    public OrderResponse cancelOrder(Long orderId) {
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

        }
        foundOrder.setPaymentStatus(PaymentStatus.CANCELED);
        foundOrder.setTransactionStatus(TransactionStatus.CANCELED);
        orderRepo.save(foundOrder);
        return OrderResponse.fromEntity(foundOrder);
    }

    // 주인: 구매요청 수락 > 재고 자동 갱신  주인확인 필요
    // transaction status -> done
    public OrderResponse confirmOrder(Long orderId, OrderConfirmDto dto) {
        // 1. 주문정보를 찾고
        ItemOrder foundOrder = optional.gerOrder(orderId);
        // 2. 로그인한 사용자가 쇼핑몰 주인인지 확인
        manager.checkIdIsEqual(foundOrder.getOrderItem().getShoppingMall().getOwner().getAccountId());
        // 3. 전달금액 확인후 구매요청 수락
        if (foundOrder.getPaymentStatus().equals(PaymentStatus.PAID)
        && foundOrder.getTransactionStatus().equals(TransactionStatus.WAIT)) {
            // 혹시 재고가 부족한지 한번 더 확인. (*구매요청 시에는 재고가 있었다가, 구매 수락 시에는 재고가 없을 수 있기때문)
            if (foundOrder.getAmount() > foundOrder.getOrderItem().getStock()) {
                log.info("재고가 부족하여 구매요청이 수락되지 않았습니다.");
                // 구매 취소 처리
                foundOrder.setTransactionStatus(TransactionStatus.CANCELED);
                foundOrder.setPaymentStatus(PaymentStatus.CANCELED);
                // todo: 환불 (지불 취소)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "재고가 부족하여 구매요청이 수락되지 않았습니다.");
            }

            // 재고가 있다면 수락 처리
            foundOrder.setTransactionStatus(TransactionStatus.DONE);
            // 수락되면 상품재고 자동 갱신

            // 거래완료 일자 업데이트
            foundOrder.setTransactionDate(LocalDateTime.now());
            // 쇼핑몰 최근거래 일자도 함께 업데이트
            foundOrder.getOrderItem().getShoppingMall().setRecentOrderDate(LocalDateTime.now());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderRepo.save(foundOrder);
        return OrderResponse.fromEntity(foundOrder);
    }
}
