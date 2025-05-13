package com.example.demo.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    private Long id;
    private String carName;
    private String mfg;
    private Long price;
    private String owner;
    private Long carBrandId;
    private Instant createdAt;
}
