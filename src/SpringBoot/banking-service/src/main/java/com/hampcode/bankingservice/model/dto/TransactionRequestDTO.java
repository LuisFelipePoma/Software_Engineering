package com.hampcode.bankingservice.model.dto;



import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    private Long id;

    @NotNull(message = "El numero de la cuenta de origen no puede estar vacío")
    private String sourceAccountNumber;

    @NotNull(message = "El numero de la cuenta de destino no puede estar vacío")
    private String targetAccountNumber;

    @NotNull(message = "La cantidad no puede estar vacía")
    @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor que cero")
    private BigDecimal amount;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

}
