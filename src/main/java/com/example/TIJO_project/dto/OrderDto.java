package com.example.TIJO_project.dto;

import com.example.TIJO_project.model.PaymentMethod;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document("orders")
public class OrderDto {
    private String tableNoId;
    private Double cost;
    private List<OrderItemDto> order;
    private PaymentMethod paymentMethod;
}
