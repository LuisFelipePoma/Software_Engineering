package com.hampcode.model.dto;

import com.hampcode.model.enums.OrderStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    @NotNull(message = "El número de pedido no puede ser nulo")
    private Long orderNumber;

    @NotBlank(message = "El nombre del cliente no puede estar en blanco")
    private String customerName;

    @NotNull(message = "El monto del pedido no puede ser nulo")
    //@Positive(message = "El monto del pedido debe ser un número positivo")
    @DecimalMin(value = "0.01", message = "El monto del pedido debe ser mayor que cero")
    private BigDecimal amount;

    private OrderStatus status;
}
