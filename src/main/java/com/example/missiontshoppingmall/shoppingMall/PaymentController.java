package com.example.missiontshoppingmall.shoppingMall;

import com.example.missiontshoppingmall.order.OrderService;
import com.example.missiontshoppingmall.order.entity.ItemOrder;
import com.example.missiontshoppingmall.order.entity.PaymentStatus;
import com.example.missiontshoppingmall.utils.EntityFromOptional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final EntityFromOptional optional;

    // 결제요청 html로 이동하는
    @GetMapping("/payment/{orderId}")
    public String requestPayment(
            @PathVariable("orderId") Long orderId,
            RedirectAttributes redirectAttributes
    ) {
        // redirectAttributes로 order에 대한 정보 함께 전달.
        ItemOrder foundOrder = optional.gerOrder(orderId);
        // 만약 not paid상태가 아니면 예외처리
        if (!foundOrder.getPaymentStatus().equals(PaymentStatus.NOT_PAID)) {
            log.info("지불이 불가능한 주문입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        redirectAttributes.addAttribute("orderItem", foundOrder.getOrderItem().getName());
        redirectAttributes.addAttribute("amount", foundOrder.getTotalPrice());
        redirectAttributes.addAttribute("orderId", orderId);
        return "redirect:/static/payment.html";
    }
}
