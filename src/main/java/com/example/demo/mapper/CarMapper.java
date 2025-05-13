package com.example.demo.mapper;

import com.example.demo.DTO.response.CarResponse;
import com.example.demo.model.Car;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class CarMapper {
    public CarResponse toResponse(Car car) {
        CarResponse response = new CarResponse();
        response.setId(car.getId());
        response.setCarName(car.getCarName());
        response.setMfg(car.getMfg());
        response.setPrice(car.getPrice());
        response.setOwner(car.getOwner());
        response.setBrandName(car.getBrand().getName());
        response.setBrandType(car.getBrand().getCarType().name());
        response.setCreatedAt(car.getCreatedAt());
        return response;
    }
    public List<CarResponse> toCarResponseList(List<Car> cars) {
        return cars.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


}
