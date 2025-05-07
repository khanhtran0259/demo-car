package com.example.demo.controller;

import com.example.demo.model.Brand;
import com.example.demo.service.BrandService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Brand> getBrandByID(@PathVariable Long id){
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @PostMapping
    public ResponseEntity<Brand> createCar(@RequestBody Brand brand){
        return ResponseEntity.ok(brandService.createBrand(brand));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateCar(@PathVariable Long id, @RequestBody Brand brand){
        return ResponseEntity.ok(brandService.updateBrand(id, brand));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBrand(@PathVariable Long id){
        brandService.deleteBrand(id);
        return ResponseEntity.ok(MessageFormat.format("Brand with id {0} deleted successfully", id));
    }
}
