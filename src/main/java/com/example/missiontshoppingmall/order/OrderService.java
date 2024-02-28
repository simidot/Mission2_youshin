package com.example.missiontshoppingmall.order;

import com.example.missiontshoppingmall.EntityFromOptional;
import com.example.missiontshoppingmall.item.entity.Item;
import com.example.missiontshoppingmall.order.dto.OrderRequest;
import com.example.missiontshoppingmall.order.dto.OrderResponse;
import com.example.missiontshoppingmall.order.entity.ItemOrder;
import com.example.missiontshoppingmall.order.entity.PaymentStatus;
import com.example.missiontshoppingmall.order.entity.TransactionStatus;
import com.example.missiontshoppingmall.order.repo.OrderRepository;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final EntityFromOptional optional;
    private final CustomUserDetailsManager manager;
    private final OrderRepository orderRepo;

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

        // todo: 필요금액 전달한다.
        newOrder.setPaymentStatus(PaymentStatus.PAID);
        orderRepo.save(newOrder);
        return OrderResponse.fromEntity(newOrder);
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
        foundOrder.setPaymentStatus(PaymentStatus.CANCELED);
        foundOrder.setTransactionStatus(TransactionStatus.CANCELED);
        orderRepo.save(foundOrder);
        return OrderResponse.fromEntity(foundOrder);
    }

    // 주인: 구매요청 수락 > 재고 자동 갱신  주인확인 필요
    // transaction status -> done
    public OrderResponse confirmOrder(Long orderId) {
        // 1. 주문정보를 찾고
        ItemOrder foundOrder = optional.gerOrder(orderId);
        // 2. 로그인한 사용자가 쇼핑몰 주인인지 확인
        manager.checkIdIsEqual(foundOrder.getOrderItem().getShoppingMall().getOwner().getAccountId());
        // 3. 전달금액 확인후 구매요청 수락
        if (foundOrder.getPaymentStatus().equals(PaymentStatus.PAID)
        && foundOrder.getTransactionStatus().equals(TransactionStatus.WAIT)) {
            foundOrder.setTransactionStatus(TransactionStatus.DONE);
            // 수락되면 상품재고 자동 갱신
            // 현재재고
            Integer stockNow = foundOrder.getOrderItem().getStock();
            foundOrder.getOrderItem().setStock(stockNow-foundOrder.getAmount());
            // 거래완료 일자 업데이트
            foundOrder.setTransactionDate(LocalDateTime.now());
            foundOrder.getOrderItem().getShoppingMall().setRecentOrderDate(LocalDateTime.now());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderRepo.save(foundOrder);
        return OrderResponse.fromEntity(foundOrder);
    }

}
