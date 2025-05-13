package com.example.demo.controller;

import com.example.demo.DTO.request.BrandRequest;
import com.example.demo.DTO.response.BrandResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Brand;
import com.example.demo.service.BrandService;
import com.example.demo.service.implement.BrandServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brands")
public class BrandController {
    private final BrandServiceImpl brandServiceImpl;

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrands(){
        return ResponseEntity.ok(brandServiceImpl.getAllBrands());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandByID(@PathVariable Long id){
        try {
            return ResponseEntity.ok(brandServiceImpl.getBrandById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Brand with id {0} not found", id));
        }
    }


    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody BrandRequest brandRequest){
        if (brandRequest == null || brandRequest.getBrandName() == null || brandRequest.getBrandName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Brand name cannot be empty.");
        }
        return ResponseEntity.ok(brandServiceImpl.createBrand(brandRequest));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Long id, @RequestBody BrandRequest brandRequest){
        if (brandRequest == null || brandRequest.getBrandName() == null || brandRequest.getBrandName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Brand name cannot be empty.");
        }
        try {
            return ResponseEntity.ok(brandServiceImpl.updateBrand(id, brandRequest));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Brand with id {0} not found", id));
        }
    }
    @PutMapping("/restore")
    public ResponseEntity<?> restoreBrandByName(@RequestParam String name) {
        try {
            brandServiceImpl.restoreBrandByName(name);
            return ResponseEntity.ok("Successfully restored brand and associated cars: " + name);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long id){
        try {
            brandServiceImpl.deleteBrand(id);
            return ResponseEntity.ok(MessageFormat.format("Brand with id {0} deleted successfully", id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Brand with id {0} not found", id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBrandByName(@RequestParam String brandName) {
        try {
            brandServiceImpl.deleteBrandByName(brandName);
            return ResponseEntity.ok("Successfully deleted brand and associated cars: " + brandName);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
