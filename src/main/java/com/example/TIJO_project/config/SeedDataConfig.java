package com.example.TIJO_project.config;

import com.example.TIJO_project.model.Dish;
import com.example.TIJO_project.service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {
    private final DishService dishService;

    @Override
    public void run(String... args) throws Exception {
        if (dishService.count() == 0) {
            DishesConfig dishesConfig = YamlLoader.loadDishesConfig("dishes.yaml");
            List<Dish> dishes = dishesConfig.getDishes();
            dishService.saveAll(dishes);
            log.debug("Loaded dishes from YAML file");
        }
    }
}
