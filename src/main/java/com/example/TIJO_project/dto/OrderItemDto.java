package com.example.TIJO_project.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemDto {
    private DishDto dish;
    private Integer quantity;
    private Double cost;
}
