package com.example.missiontshoppingmall.order;

import com.example.missiontshoppingmall.order.dto.OrderRequest;
import com.example.missiontshoppingmall.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/shopping-malls")
public class OrderController {
    private final OrderService orderService;

    // 쇼핑몰 상품 구매 (상품, 구매수량 기준으로 구매 요청)
    @PostMapping("/items/{itemId}/order")
    public OrderResponse orderItem(
            @PathVariable("itemId") Long itemId,
            @RequestBody OrderRequest dto
    ) {
        return orderService.createOrder(itemId, dto);
    }

    // 구매요청 취소 (before 구매요청 수락) (본인확인)
    @PostMapping("orders/{orderId}/order-cancel")
    public OrderResponse cancelOrder(
            @PathVariable("orderId") Long orderId
    ) {
        return orderService.cancelOrder(orderId);
    }

    // 주인: 구매요청 수락 > 재고 자동 갱신  주인확인 필요
    @PostMapping("/order-requests/{orderId}/order-confirm")
    public OrderResponse confirmOrder(
            @PathVariable("orderId") Long orderId
    ) {
        return orderService.confirmOrder(orderId);
    }
}
