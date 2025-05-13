package com.example.demo.service.implement;
import com.example.demo.DTO.request.BrandRequest;
import com.example.demo.DTO.response.BrandResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.BrandMapper;
import com.example.demo.model.Brand;
import com.example.demo.model.Car;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CarRepository;
import com.example.demo.service.BrandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final CarRepository carRepository;
    private final BrandMapper brandMapper;



    @Override
    public List<BrandResponse> getAllBrands() {
        List<Brand> brands = brandRepository.findAllByIsDeletedFalse();
        return brandMapper.toResponseList(brands);
    }

    @Override
    public BrandResponse getBrandById(Long id) {
        Brand brand = brandRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        return brandMapper.toResponse(brand);
    }

    @Override
    public BrandResponse createBrand(BrandRequest request) {
        Brand brand = brandMapper.toEntity(request);
        Brand saved = brandRepository.save(brand);
        return brandMapper.toResponse(saved);
    }

    @Override
    public BrandResponse updateBrand(Long id, BrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .filter(b -> !b.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        brand.setName(request.getBrandName());
        brand.setCarType(request.getBrandType());

        Brand updated = brandRepository.save(brand);
        return brandMapper.toResponse(updated);
    }

    @Override
    public void deleteBrandByName(String brandName) {
        List<Brand> brands = brandRepository.findByNameAndIsDeletedFalse(brandName);

        if (brands.isEmpty()) {
            throw new ResourceNotFoundException("No brand found with name: " + brandName);
        }
        for (Brand brand : brands) {
            brand.setDeleted(true);
            brandRepository.save(brand);
            List<Car> cars = carRepository.findAllByBrand(brand);
            for (Car car : cars) {
                car.setDeletedByAdmin(true);
            }
            carRepository.saveAll(cars);
        }
    }
    @Transactional
    @Override
    public void restoreBrandByName(String brandName) {
        List<Brand> brands = brandRepository.findByNameIgnoreCaseAndIsDeletedTrue(brandName);
        if (brands.isEmpty()) {
            throw new ResourceNotFoundException("No deleted brands found with name: " + brandName);
        }

        for (Brand brand : brands) {
            brand.setDeleted(false);
            List<Car> cars = carRepository.findAllByBrand(brand);
            for (Car car : cars) {
                car.setDeletedByAdmin(false);
            }
            carRepository.saveAll(cars);
        }

        brandRepository.saveAll(brands);
    }

    @Transactional
    @Override
    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .filter(b -> !b.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found or already deleted with id: " + brandId));

        brand.setDeleted(true);
        brandRepository.save(brand);

        List<Car> cars = carRepository.findAllByBrand(brand);
        for (Car car : cars) {
            car.setDeletedByAdmin(true);
        }
        carRepository.saveAll(cars);
    }
}
