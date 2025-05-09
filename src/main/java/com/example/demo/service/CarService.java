package com.example.demo.service;

import com.example.demo.DTO.CarRequest;
import com.example.demo.DTO.CarResponse;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CarMapper;
import com.example.demo.model.Brand;
import com.example.demo.model.Car;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CarRepository;
import com.example.demo.specification.CarSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.example.demo.specification.CarSpecification.*;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final CarMapper carMapper;

    public List<CarResponse> getSpecificCars(){
        List<Car> cars = carRepository.findAll(CarSpecification.specialCondition());
        return carMapper.toCarResponseList(cars);
    }
    public List<CarResponse> getAllCars(Long carBrandId, String msg, Long price, String owner) {
        Specification<Car> spec = Specification
                .where(hasBrandId(carBrandId))
                .and(hasMfg(msg))
                .and(hasPrice(price))
                .and(hasOwner(owner));
        List<Car> cars = carRepository.findAll(spec);
        return carMapper.toCarResponseList(cars);

    }
    public CarResponse getCarById(Long id){
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + id));
        return carMapper.toResponse(car);
    }
    @Transactional
    public CarResponse createCar(CarRequest carRequest) {
        if(carRepository.findCarByCarName(carRequest.getCarName()).isPresent())
            throw new ResourceAlreadyExistsException("Car name already exists");

        Brand brand = brandRepository.findById(carRequest.getCarBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand Id not exists"));

        Car car = new Car();
        car.setCarName(carRequest.getCarName());
        car.setMfg(carRequest.getMfg());
        car.setPrice(carRequest.getPrice());
        car.setOwner(carRequest.getOwner());
        car.setBrand(brand);
        car.setCreatedAt(Instant.now());
        Car saved = carRepository.save(car);
        return carMapper.toResponse(saved);
    }

    @Transactional
    public CarResponse updateCar(Long id, CarRequest carUpdated) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        // Kiểm tra tên xe đã tồn tại cho xe khác
        Optional<Car> existCar = carRepository.findCarByCarName(carUpdated.getCarName());
        if (existCar.isPresent() && !existCar.get().getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Car name already exists");
        }

        // Kiểm tra brand có tồn tại không
        Brand brand = brandRepository.findById(carUpdated.getCarBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand id does not exist"));

        // Cập nhật thông tin
        car.setCarName(carUpdated.getCarName());
        car.setMfg(carUpdated.getMfg());
        car.setPrice(carUpdated.getPrice());
        car.setOwner(carUpdated.getOwner());
        car.setBrand(brand);
        Car saved = carRepository.save(car);
        return carMapper.toResponse(saved);
    }

    @Transactional
    public void deleteCar(Long id){
        Car car =carRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Car not found with id: " + id));
        carRepository.deleteById(id);
    }
}
