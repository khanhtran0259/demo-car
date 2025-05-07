package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.service.CarService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarService carService;

    @GetMapping("/filtered")
    public ResponseEntity<List<Car>> getFilteredCars() {
        List<Car> filteredCars = carService.getSpecificCars();
        return ResponseEntity.ok(filteredCars);
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars(
            @RequestParam(required = false) Long carBrandId,
            @RequestParam(required = false) String msg,
            @RequestParam(required = false) Long price,
            @RequestParam(required = false) String owner){
        List<Car> cars = carService.getAllCars(carBrandId, msg, price, owner);
        return ResponseEntity.ok(carService.getAllCars(carBrandId, msg, price, owner));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id){
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car){
        return ResponseEntity.ok(carService.createCar(car));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car){
        return ResponseEntity.ok(carService.updateCar(id, car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id){
        carService.deleteCar(id);
        return ResponseEntity.ok(MessageFormat.format("Car with id {0} deleted successfully", id));
    }


}
