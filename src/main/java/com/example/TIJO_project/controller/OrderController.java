package com.example.TIJO_project.controller;

import com.example.TIJO_project.dto.DishDto;
import com.example.TIJO_project.dto.OrderDto;
import com.example.TIJO_project.mapper.DishMapper;
import com.example.TIJO_project.model.Dish;
import com.example.TIJO_project.repository.DishRepository;
import com.example.TIJO_project.service.OrderService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path ="/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    @PostMapping(path = "/acceptOrder")
    public ResponseEntity<?> acceptOrder(@RequestBody(required=false) OrderDto orderDto){

        return orderService.acceptOrder(orderDto);
    }

    @GetMapping(path = "/getOrders")
    public ResponseEntity<?> getOrders(){
        return orderService.getOrders();
    }

    @PostMapping(path = "/addToOrder")
    public ResponseEntity<?> addToOrder(@RequestBody(required=false) OrderDto orderDto, @RequestParam String dishId){

        Optional<Dish> dish = dishRepository.findById(dishId);
        if(dish.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dish do not exist");
        }

        DishDto dishDto = dishMapper.toDto(dish.get());

        return orderService.addToOrder(orderDto, dishDto);
    }

    @PostMapping(path = "/removeFromOrder")
    public ResponseEntity<?> removeFromOrder(@RequestBody(required=false) OrderDto orderDto, @RequestParam String dishId){

        Optional<Dish> dish = dishRepository.findById(dishId);
        if(dish.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dish do not exist");
        }

        if(orderDto.getOrder() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order is empty");
        }

        DishDto dishDto = dishMapper.toDto(dish.get());

        return orderService.removeFromOrder(orderDto, dishDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleException(HttpMessageNotReadableException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
