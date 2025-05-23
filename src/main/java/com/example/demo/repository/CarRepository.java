package com.example.demo.repository;

import com.example.demo.model.Brand;
import com.example.demo.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {
    Optional<Car> findCarByCarName(String name);


    List<Car> findAllByBrand(Brand brand);

    Page<Car> findAllByIsDeletedByAdminFalse(Pageable pageable);

    Page<Car> findAllByOwnerAndIsDeletedByUserFalse(String owner, Pageable pageable);
}
