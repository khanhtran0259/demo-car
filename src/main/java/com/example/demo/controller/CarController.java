package com.example.demo.controller;

import com.example.demo.DTO.CarRequest;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
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
        return ResponseEntity.ok(carService.getAllCars(carBrandId, msg, price, owner));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id){
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping
    public ResponseEntity<?> createCar(@RequestBody CarRequest carRequest) {
        try {
            return ResponseEntity.ok(carService.createCar(carRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestBody CarRequest carRequest) {
        try {
            return ResponseEntity.ok(carService.updateCar(id, carRequest));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
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
