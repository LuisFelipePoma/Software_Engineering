package com.hampcode.service;


import com.hampcode.exception.InsufficientBalanceException;
import com.hampcode.model.dto.OrderRequestDTO;
import com.hampcode.model.dto.PaymentResponseDTO;
import com.hampcode.model.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//Esta anotación se utiliza junto con JUnit 5 para habilitar el soporte de Mockito en las pruebas.
// Con esta anotación, JUnit ejecutará las pruebas utilizando el motor de Mockito, lo que te permite utilizar anotaciones
// como @Mock y @InjectMocks.
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    //Esta anotación se utiliza para crear mocks de las dependencias necesarias para la prueba.
    //Dependencias simuladas
    @Mock
    private PaymentServiceExternal paymentService;

    //Esta anotación se utiliza para inyectar dependencias en el objeto que estás probando.
    @InjectMocks
    private OrderService orderService;

    //Esta anotación se utiliza para marcar un método como un método de prueba.
    // JUnit ejecutará cualquier método anotado con @Test durante la ejecución de la prueba.
    @Test
    public void processOrderPayment_SuccessfulPayment_ReturnsPaymentResponse() {
        // Arrange: Esta sección se utiliza para configurar el estado inicial necesario para la prueba,
        // como la creación de objetos y la configuración de simulaciones.
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setOrderNumber(1L);
        orderRequestDTO.setAmount(BigDecimal.valueOf(50.0));
        String paymentMethod = "PayPal";

        //Configuración para simular el comportamiento del método processPayment del servicio de pago, devolviendo true cuando
        // se llama con cualquier instancia de OrderRequestDTO.
        when(paymentService.processPayment(any(OrderRequestDTO.class))).thenReturn(true);

        // Act:Aquí se realiza la acción que se está probando, como llamar a un método o interactuar con el sistema bajo prueba.
        PaymentResponseDTO paymentResponseDTO = orderService.processOrderPayment(orderRequestDTO, paymentMethod);

        // Assert:Esta parte se utiliza para verificar que el resultado de la acción realizada en la sección "Act" sea el esperado,
        // utilizando afirmaciones para comprobar el comportamiento o el estado resultante.
        assertNotNull(paymentResponseDTO);
        assertEquals(orderRequestDTO.getOrderNumber(), paymentResponseDTO.getOrderId());
        assertEquals(orderRequestDTO.getAmount(), paymentResponseDTO.getAmount());
        assertEquals(paymentMethod, paymentResponseDTO.getPaymentMethod());
        assertEquals(OrderStatus.PAID, orderRequestDTO.getStatus());
    }

    @Test
    public void processOrderPayment_InsufficientBalance_ThrowsException() {
        // Arrange
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setOrderNumber(2L);
        orderRequestDTO.setAmount(BigDecimal.valueOf(200.0));
        String paymentMethod = "PayPal";

        //Configuración para simular el comportamiento del método processPayment del servicio de pago, devolviendo false cuando
        // se llama con cualquier instancia de OrderRequestDTO.
        when(paymentService.processPayment(any(OrderRequestDTO.class))).thenReturn(false);

        // Act
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            orderService.processOrderPayment(orderRequestDTO, paymentMethod);
        });

        //Assert
        assertEquals("Saldo insuficiente para generar el pago del peedido: 2", exception.getMessage());
    }
}
