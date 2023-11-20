package com.example.TIJO_project.repository;

import com.example.TIJO_project.model.QrCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QrCodeRepository extends MongoRepository<QrCode, String> {
}
