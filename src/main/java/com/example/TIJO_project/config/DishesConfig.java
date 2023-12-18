package com.example.TIJO_project.config;

import com.example.TIJO_project.model.Dish;
import lombok.Data;

import java.util.List;

@Data
public class DishesConfig {
    private List<Dish> dishes;
}
