package com.example.TIJO_project.service;


import com.example.TIJO_project.model.QrCode;
import com.example.TIJO_project.repository.QrCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;

    public Optional<QrCode> checkCode(String id) {
        return qrCodeRepository.findById(id);
    }
}
