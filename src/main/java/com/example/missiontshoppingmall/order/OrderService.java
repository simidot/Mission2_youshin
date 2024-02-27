package com.example.missiontshoppingmall.order;

import com.example.missiontshoppingmall.EntityFromOptional;
import com.example.missiontshoppingmall.item.entity.Item;
import com.example.missiontshoppingmall.item.repo.ItemRepository;
import com.example.missiontshoppingmall.order.dto.OrderRequest;
import com.example.missiontshoppingmall.order.dto.OrderResponse;
import com.example.missiontshoppingmall.order.entity.Order;
import com.example.missiontshoppingmall.order.entity.PaymentStatus;
import com.example.missiontshoppingmall.order.entity.TransactionStatus;
import com.example.missiontshoppingmall.order.repo.OrderRepository;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final EntityFromOptional optional;
    private final CustomUserDetailsManager manager;
    private final OrderRepository orderRepo;

    // 쇼핑몰 상품 구매 (상품, 구매수량 기준으로 구매 요청)
    public OrderResponse createOrder(Long itemId, OrderRequest dto) {
        // 상품을 찾고,
        Item foundItem = optional.getItem(itemId);
        // 그 상품의 재고가 구매수량보다 크면 구매요청 보냄
        if (foundItem.getStock() < dto.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Order newOrder = Order.builder()
                .orderItem(foundItem)
                .buyer(manager.loadUserFromAuth())
                .amount(dto.getAmount())
                .totalPrice(dto.getAmount()*foundItem.getPrice())
                .paymentStatus(PaymentStatus.NOT_PAID)
                .transactionStatus(TransactionStatus.WAIT)
                .build();
        return OrderResponse.fromEntity(newOrder);
    }

    // 필요금액 전달 (본인확인)

    // 구매요청 취소 (before 구매요청 수락) (본인확인)

    // 주인: 구매요청 수락 > 재고 자동 갱신  주인확인 필요
}
