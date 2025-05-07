package com.example.demo.controller;

import com.example.demo.model.Brand;
import com.example.demo.service.BrandService;
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
    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands(){
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandByID(@PathVariable Long id){
        try {
            return ResponseEntity.ok(brandService.getBrandById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Brand with id {0} not found", id));
        }
    }


    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody Brand brand){
        if (brand == null || brand.getName() == null || brand.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Brand name cannot be empty.");
        }
        return ResponseEntity.ok(brandService.createBrand(brand));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Long id, @RequestBody Brand brand){
        if (brand == null || brand.getName() == null || brand.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Brand name cannot be empty.");
        }
        try {
            return ResponseEntity.ok(brandService.updateBrand(id, brand));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Brand with id {0} not found", id));
        }
    }


    @DeleteMapping
    public ResponseEntity<String> deleteBrand(@PathVariable Long id){
        try {
            brandService.deleteBrand(id);
            return ResponseEntity.ok(MessageFormat.format("Brand with id {0} deleted successfully", id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Brand with id {0} not found", id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

}
