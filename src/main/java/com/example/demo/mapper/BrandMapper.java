package com.example.demo.mapper;


import com.example.demo.DTO.request.BrandRequest;
import com.example.demo.DTO.response.BrandResponse;
import com.example.demo.model.Brand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandMapper {

    public BrandResponse toResponse(Brand brand) {
        if (brand == null) return null;

        BrandResponse response = new BrandResponse();
        response.setId(brand.getId());
        response.setBrandName(brand.getName());
        response.setBrandType(brand.getCarType());
        return response;
    }

    public List<BrandResponse> toResponseList(List<Brand> brands) {
        if (brands == null || brands.isEmpty()) return List.of();
        return brands.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Brand toEntity(BrandRequest request) {
        if (request == null) return null;

        Brand brand = new Brand();
        brand.setId(request.getId());
        brand.setName(request.getBrandName());
        brand.setCarType(request.getBrandType());
        return brand;
    }
}
