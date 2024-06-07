package com.hampcode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hampcode.model.dto.OrderRequestDTO;
import com.hampcode.model.dto.PaymentResponseDTO;
import com.hampcode.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

//Anotación para cargar la aplicación Spring Boot completa para la prueba.
@SpringBootTest
//Anotación para configurar automáticamente el entorno MockMvc para las pruebas de integración.
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class OrderControllerIntegrationTest {


    private final MockMvc mockMvc;


    //El ObjectMapper se utiliza para convertir objetos Java en formato JSON y viceversa.
    private final ObjectMapper objectMapper;


    private final OrderService orderService;

    @Test
    public void processOrderPayment_SuccessfulPayment_ReturnsPaymentResponse() throws Exception {
        // Arrange
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setOrderNumber(1L);
        orderRequestDTO.setAmount(BigDecimal.valueOf(5.0));

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/orders/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        PaymentResponseDTO responseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), PaymentResponseDTO.class);

        //  Assert Verify
        assert responseDTO != null;
        assert responseDTO.getOrderId().equals(orderRequestDTO.getOrderNumber());
        assert responseDTO.getAmount().equals(orderRequestDTO.getAmount());
        assert responseDTO.getPaymentMethod().equals("PayPal");
    }
}
