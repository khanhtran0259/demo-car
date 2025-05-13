package com.example.demo.DTO.response;

import com.example.demo.model.BrandType;
import lombok.Data;

@Data
public class BrandResponse {
    private Long id;
    private String brandName;
    private BrandType brandType;
}
