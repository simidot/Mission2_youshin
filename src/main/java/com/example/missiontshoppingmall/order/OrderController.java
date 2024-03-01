package com.example.missiontshoppingmall.order;

import com.example.missiontshoppingmall.order.dto.OrderConfirmDto;
import com.example.missiontshoppingmall.order.dto.OrderRequest;
import com.example.missiontshoppingmall.order.dto.OrderResponse;
import com.example.missiontshoppingmall.utils.PaymentConfirmDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/shopping-malls")
public class OrderController {
    private final OrderService orderService;

    // 쇼핑몰 상품 구매 (상품, 구매수량 기준으로 구매 요청)
    @PostMapping("/items/{itemId}/order")
    public void orderItem(
            @PathVariable("itemId") Long itemId,
            @RequestBody OrderRequest dto,
            HttpServletResponse res
    ) throws IOException {
        OrderResponse response = orderService.createOrder(itemId, dto);
//        return response;
        res.sendRedirect("http://localhost:8080/payment/"+response.getId());
    } // 상품구매를 하고 나서 > 결제 요청을 위해 Redirect를 하여 html로 이동한다고 가정.

    // 쇼핑몰 상품 구매시 결제 승인 요청
    @PostMapping("/order/{orderId:.*}/payment")
    public OrderResponse confirmPayment(
            @RequestBody PaymentConfirmDto dto
    ) {
        return orderService.confirmPayment(dto);
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
            @PathVariable("orderId") Long orderId,
            @RequestBody OrderConfirmDto dto
    ) {
        return orderService.confirmOrder(orderId, dto);
    }
}
