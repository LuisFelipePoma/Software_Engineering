package com.hampcode.controller;

import com.hampcode.model.dto.OrderRequestDTO;
import com.hampcode.model.dto.PaymentResponseDTO;
import com.hampcode.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/payment")
    public ResponseEntity<PaymentResponseDTO> processOrderPayment(@Validated @RequestBody OrderRequestDTO orderRequest) {
        PaymentResponseDTO paymentResponse = orderService.processOrderPayment(orderRequest, "PayPal");
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }
}