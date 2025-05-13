package com.example.demo.DTO.request;

import com.example.demo.model.BrandType;
import lombok.Data;

@Data
public class BrandRequest {
    private Long id;
    private String brandName;
    private BrandType brandType;
}
