package com.example.demo.service;

import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Brand;
import com.example.demo.model.BrandType;
import com.example.demo.model.Car;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private BrandRepository brandRepository;

    public List<Car> getSpecificCars(){
        List<Car> carGreaterThan10M = carRepository.findCarByPriceGreaterThan(10000000L);
        List<Car> carLessThan10M = carRepository.findCarByPriceLessThan(10000000L);
        List<Brand> brandStartWithS = brandRepository.findBrandByNameStartingWith("S");
        List<Brand> brandWithBusType = brandRepository.findBrandByCarType(BrandType.BUS);
        List<Car> result = new ArrayList<>();
        for (Car car : carGreaterThan10M) {
            for (Brand brand : brandStartWithS) {
                if (car.getCarBrandId().equals(brand.getId())) {
                    result.add(car); // Nếu xe thỏa mãn, thêm vào danh sách kết quả
                }
            }
        }

        // Trường hợp 2: Xe có giá < 10 triệu và thuộc loại BUS
        for (Car car : carLessThan10M) {
            for (Brand brand : brandWithBusType) {
                if (car.getCarBrandId().equals(brand.getId())) {
                    result.add(car); // Nếu xe thỏa mãn, thêm vào danh sách kết quả
                }
            }
        }

        return result;
    }
    public List<Car> getAllCars(Long carBrandId, String msg, Long price, String owner){
        if (carBrandId == null && msg == null && price == null && owner == null) {
            return carRepository.findAll();
        }
        if (carBrandId != null) {
            return carRepository.findCarByCarBrandId(carBrandId);
        }
        if (msg != null) {
            return carRepository.findCarByMfg(msg);
        }

        if (price != null) {
            return carRepository.findCarByPrice(price);
        }

        if (owner != null) {
            return carRepository.findCarByOwner(owner);
        }
        return carRepository.findAll();
    }
    public Car getCarById(Long id){
        return carRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Car not found with id: " + id));
    }

    public Car createCar(Car car){
        if(carRepository.findCarByCarName(car.getCarName()).isPresent())
            throw new ResourceAlreadyExistsException("Car name already exists");

        if(!brandRepository.findById(car.getCarBrandId()).isPresent())
            throw  new ResourceNotFoundException("Brand Id not exists");
        if (car.getCreatedAt() == null) {
            car.setCreatedAt(Instant.now());
        }
        return carRepository.save(car);
    }

    public Car updateCar(Long id, Car carUpdated){
        Car car = carRepository.findById(id).orElseThrow(()
        -> new ResourceNotFoundException("Car not found"));
        Optional<Car> existCar =carRepository.findCarByCarName(carUpdated.getCarName());
        if(existCar.isPresent() && !existCar.get().getId().equals(id))
            throw new ResourceAlreadyExistsException("Car name already exists");
        Optional<Brand> existBrand = brandRepository.findById(car.getCarBrandId());
        if(existBrand.isPresent() && !existBrand.get().getId().equals(carUpdated.getCarBrandId()))
            throw new ResourceNotFoundException("Brand id not exist");
        car.setCarName(carUpdated.getCarName());
        car.setMfg(carUpdated.getMfg());
        car.setPrice(carUpdated.getPrice());
        car.setOwner(carUpdated.getOwner());
        car.setCarBrandId(carUpdated.getCarBrandId());
        car.setCreatedAt(carUpdated.getCreatedAt());
        return carRepository.save(car);
    }

    public void deleteCar(Long id){
        Car car =carRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Car not found with id: " + id));
        carRepository.deleteById(id);
    }
}
