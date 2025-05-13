package com.example.demo.service;

import com.example.demo.DTO.filter.CarFilterRequest;
import com.example.demo.DTO.request.CarRequest;
import com.example.demo.DTO.response.CarResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CarService {
    List<CarResponse> getSpecificCars();

    List<CarResponse> getAllCars(CarFilterRequest request);

    CarResponse getCarById(Long id, CarFilterRequest request) throws AccessDeniedException;

    CarResponse createCar(CarRequest carRequest, CarFilterRequest request) throws AccessDeniedException;

    CarResponse updateCar(Long id, CarRequest carUpdated, CarFilterRequest filterRequest) throws AccessDeniedException;

    void deleteCar(Long id, boolean isAdmin, String owner) throws AccessDeniedException;
    CarResponse restoreCar(Long id, CarFilterRequest filterRequest) throws AccessDeniedException;
}
