package com.example.TIJO_project.controller;

import com.example.TIJO_project.model.QrCode;
import com.example.TIJO_project.service.QrCodeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path ="/qrCode")
@AllArgsConstructor
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @GetMapping(path = "/getValue/{id}")
    public ResponseEntity<QrCode> checkCode(@PathVariable String id){

        Optional<QrCode> qrCode = qrCodeService.checkCode(id);

        if(qrCode.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(qrCode.get());
        }
    }
}
