package com.example.demo.controller;

import com.example.demo.DTO.filter.CarFilterRequest;
import com.example.demo.DTO.request.CarRequest;
import com.example.demo.DTO.response.CarResponse;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.implement.CarServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.text.MessageFormat;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarServiceImpl carServiceImpl;

    @GetMapping("/specific")
    public ResponseEntity<List<CarResponse>> getSpecificCars() {
        return ResponseEntity.ok(carServiceImpl.getSpecificCars());
    }

    @GetMapping
    public ResponseEntity<Page<CarResponse>> getCars(@ModelAttribute CarFilterRequest request) {
        return ResponseEntity.ok(carServiceImpl.getAllCars(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id, @ModelAttribute CarFilterRequest request) throws AccessDeniedException {
        return ResponseEntity.ok(carServiceImpl.getCarById(id, request));
    }

    @PostMapping
    public ResponseEntity<?> createCar(@RequestBody CarRequest carRequest, @ModelAttribute CarFilterRequest request) throws AccessDeniedException {
        try {
            return ResponseEntity.ok(carServiceImpl.createCar(carRequest, request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestBody CarRequest carRequest, @ModelAttribute CarFilterRequest filterRequest) throws AccessDeniedException {
        try {
            return ResponseEntity.ok(carServiceImpl.updateCar(id, carRequest, filterRequest));
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
    @PutMapping("/restore/{id}")
    public ResponseEntity<CarResponse> restoreCar(@PathVariable Long id, @RequestParam boolean isAdmin) throws AccessDeniedException {
        CarFilterRequest filterRequest = new CarFilterRequest();
        filterRequest.setIsAdmin(isAdmin);  // Chỉ admin mới có thể khôi phục xe

        CarResponse restoredCar = carServiceImpl.restoreCar(id, filterRequest);
        return ResponseEntity.ok(restoredCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(
            @PathVariable Long id,
            @RequestParam(name = "isAdmin") boolean isAdmin,
            @RequestParam(name = "owner") String owner){
        try {
            carServiceImpl.deleteCar(id, isAdmin, owner);
            return ResponseEntity.ok(MessageFormat.format("Car with id {0} deleted successfully", id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Car with id {0} not found", id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }



}
