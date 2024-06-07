package com.hampcode.service;

import com.hampcode.exception.InsufficientBalanceException;
import com.hampcode.model.dto.OrderRequestDTO;
import com.hampcode.model.dto.PaymentResponseDTO;
import com.hampcode.model.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PaymentServiceExternal paymentService;

    public PaymentResponseDTO processOrderPayment(OrderRequestDTO order, String paymentMethod) {

        boolean paymentProcessed = paymentService.processPayment(order);

        if (paymentProcessed) {
            return buildPaymentResponse(order, paymentMethod);
        } else {
            throw new InsufficientBalanceException("Saldo insuficiente para generar el pago del peedido: " + order.getOrderNumber());
        }
    }

    private PaymentResponseDTO buildPaymentResponse(OrderRequestDTO order, String paymentMethod) {
        order.setStatus(OrderStatus.PAID);
        PaymentResponseDTO paymentResponse = new PaymentResponseDTO();
        paymentResponse.setOrderId(order.getOrderNumber());
        paymentResponse.setAmount(order.getAmount());
        paymentResponse.setPaymentMethod(paymentMethod);
        return paymentResponse;
    }

}
