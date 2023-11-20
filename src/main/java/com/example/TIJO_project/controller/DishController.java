package com.example.TIJO_project.controller;

import com.example.TIJO_project.model.Dish;
import com.example.TIJO_project.service.DishService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="/dishes")
@AllArgsConstructor
public class DishController {
    private final DishService dishService;

    @GetMapping(path = "/getAllDishes")
    public List<Dish> getAllDishes(){
        return dishService.getAllDishes();
    }
}
