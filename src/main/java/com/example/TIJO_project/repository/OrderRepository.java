package com.example.TIJO_project.repository;

import com.example.TIJO_project.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository <Order, String> {
    @Query("{ 'userId' : ?0 }")
    List<Order> findAllByUserId(String userId);
}
