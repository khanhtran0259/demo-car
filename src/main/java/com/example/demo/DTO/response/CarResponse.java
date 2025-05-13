package com.example.demo.DTO.response;

import com.example.demo.model.BrandType;
import lombok.Data;

import java.time.Instant;
@Data
public class CarResponse {
    private Long id;
    private String carName;
    private String mfg;
    private Long price;
    private String owner;
    private String brandName;
    private String brandType;
    private Instant createdAt;

}
