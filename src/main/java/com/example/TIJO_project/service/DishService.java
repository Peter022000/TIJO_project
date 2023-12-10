package com.example.TIJO_project.service;


import com.example.TIJO_project.model.Dish;
import com.example.TIJO_project.repository.DishRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Long count() {
        return dishRepository.count();
    }

    public void saveAll(List<Dish> dishes) {
        dishRepository.saveAll(dishes);
    }

}
