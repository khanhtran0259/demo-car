package com.example.demo.service;

import com.example.demo.DTO.request.BrandRequest;
import com.example.demo.DTO.response.BrandResponse;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Brand;
import com.example.demo.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BrandService {
    void deleteBrand(Long brandId);
    List<BrandResponse> getAllBrands();
    BrandResponse getBrandById(Long id);
    BrandResponse createBrand(BrandRequest request);
    BrandResponse updateBrand(Long id, BrandRequest request);
    void deleteBrandByName(String brandName);
    void restoreBrandByName(String brandName);
}

