package com.hampcode.service;

import com.hampcode.model.dto.OrderRequestDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceExternal {

    private static  BigDecimal AVAILABLE_BALANCE = BigDecimal.valueOf(1000.0);

    public boolean processPayment(OrderRequestDTO order) {
        BigDecimal paymentAmount = order.getAmount();
        boolean paymentProcessed = false;

        if (AVAILABLE_BALANCE.compareTo(paymentAmount) >= 0) {
            AVAILABLE_BALANCE = AVAILABLE_BALANCE.subtract(paymentAmount);
            paymentProcessed = true;
        }

        return paymentProcessed;
    }

}