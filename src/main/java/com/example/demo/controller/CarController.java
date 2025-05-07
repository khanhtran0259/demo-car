package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.service.CarService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarService carService;

    @GetMapping("/specific")
    public ResponseEntity<List<Car>> getSpecificCars() {
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
    public ResponseEntity<?> createCar(@RequestBody Car car){
        try {
            return ResponseEntity.ok(carService.createCar(car));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestBody Car car){
        try {
            return ResponseEntity.ok(carService.updateCar(id, car));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Car with id {0} not found", id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id){
        try {
            carService.deleteCar(id);
            return ResponseEntity.ok(MessageFormat.format("Car with id {0} deleted successfully", id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Car with id {0} not found", id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra: " + e.getMessage());
        }
    }



}
