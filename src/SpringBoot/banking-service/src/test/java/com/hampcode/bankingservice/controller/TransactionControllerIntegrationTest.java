package com.hampcode.bankingservice.controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hampcode.bankingservice.model.dto.TransactionRequestDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;


@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetTransactionsByAccountNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/accounts/{accountNumber}", "123456"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateTransaction() throws Exception {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        // Configurar los datos de la solicitud
        requestDTO.setSourceAccountNumber("66667097778");
        requestDTO.setTargetAccountNumber("12367097778");
        requestDTO.setAmount(BigDecimal.valueOf(100.0));
        requestDTO.setDescription("Test transaction");

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testGenerateTransactionReport() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/report")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-31")
                        .param("accountNumber", "123456"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
