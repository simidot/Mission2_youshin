package com.example.missiontshoppingmall.utils;

import com.example.missiontshoppingmall.shoppingMall.dto.PaymentCancelDto;
import com.example.missiontshoppingmall.shoppingMall.dto.PaymentConfirmDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.LinkedHashMap;

@HttpExchange("/payments")
public interface TossHttpInterface {
    // confirm Payment
    @PostExchange("/confirm")
    LinkedHashMap<String, Object> confirmPayment(
            @RequestBody PaymentConfirmDto dto
    );

    // cancel Payment
    @PostExchange("/{paymentKey}/cancel")
    LinkedHashMap<String, Object> cancelPayment(
            @PathVariable("paymentKey") String paymentKey,
            @RequestBody PaymentCancelDto dto
    );

    // orderId로 결제 조회


}
