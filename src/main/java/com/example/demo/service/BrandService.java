package com.example.demo.service;

import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Brand;
import com.example.demo.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;
    public List<Brand> getAllBrands(){
        return brandRepository.findAll();
    }
    public Brand getBrandById(Long id){
        return brandRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Brand not found with id: " + id));
    }

    public Brand createBrand(Brand brand){

        return brandRepository.save(brand);
    }

    public Brand updateBrand(Long id, Brand brandUpdate){
        Brand brand = getBrandById(id);
        brand.setName(brandUpdate.getName());
        brand.setCarType(brandUpdate.getCarType());
        return brandRepository.save(brand);

    }

    public void deleteBrand(Long id){
        Brand brand = brandRepository.findById(id).orElseThrow(()
        ->new ResourceNotFoundException("Car not found with id: " + id));
        brandRepository.deleteById(id);
    }


}
