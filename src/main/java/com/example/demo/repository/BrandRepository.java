package com.example.demo.repository;

import com.example.demo.model.Brand;
import com.example.demo.model.BrandType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findBrandByName(String name);

    List<Brand> findAllByIsDeletedFalse();

    Optional<Brand> findByIdAndIsDeletedFalse(Long id);

    List<Brand> findByNameAndIsDeletedFalse(String brandName);

    List<Brand> findByNameIgnoreCaseAndIsDeletedTrue(String brandName);
}
