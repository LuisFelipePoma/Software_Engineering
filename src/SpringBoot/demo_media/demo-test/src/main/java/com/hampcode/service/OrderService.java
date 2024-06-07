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
    private final EmailService emailService;

    public PaymentResponseDTO processOrderPayment(OrderRequestDTO order, String paymentMethod) {

        boolean paymentProcessed = paymentService.processPayment(order);

        if (paymentProcessed) {
            // Construir respuesta de pago
            PaymentResponseDTO paymentResponse = buildPaymentResponse(order, paymentMethod);

            // Enviar correo de confirmación de pago
            //sendPaymentConfirmationEmail(order);

            return paymentResponse;
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

    public void sendPaymentConfirmationEmail(OrderRequestDTO order) {
        String to ="hmendo81@gmail.com";// order.getCustomerEmail(); // Suponiendo que hay un campo en OrderRequestDTO para el correo electrónico del cliente
        String subject = "Confirmación de pago";
        String text = "Estimado " + order.getCustomerName() + ",\n\nSu pago de $" + order.getAmount() + " ha sido recibido con éxito. Gracias por su compra.\n\nSaludos,\nEl equipo de nuestra tienda";
        emailService.sendEmail(to, subject, text);
    }
}
