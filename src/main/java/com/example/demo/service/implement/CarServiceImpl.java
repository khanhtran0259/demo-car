package com.example.demo.service.implement;

import com.example.demo.DTO.filter.CarFilterRequest;
import com.example.demo.DTO.request.CarRequest;
import com.example.demo.DTO.response.CarResponse;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CarMapper;
import com.example.demo.model.Brand;
import com.example.demo.model.Car;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CarRepository;
import com.example.demo.service.CarService;
import com.example.demo.specification.CarSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final CarMapper carMapper;


    @Override
    public List<CarResponse> getSpecificCars() {
        List<Car> cars = carRepository.findAll(CarSpecification.specialCondition());

        return carMapper.toCarResponseList(cars);
    }

    @Override
    public Page<CarResponse> getAllCars(CarFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Car> cars;
        if (Boolean.TRUE.equals(request.getIsAdmin())) {
            cars = carRepository.findAllByIsDeletedByAdminFalse(pageable);
        } else {
            if (request.getOwner() == null) {
                throw new IllegalArgumentException("Owner is required for non-admin access");
            }
            cars = carRepository.findAllByOwnerAndIsDeletedByUserFalse(request.getOwner(), pageable);
        }
        return cars.map(carMapper::toResponse);
    }

    @Override
    public CarResponse getCarById(Long id, CarFilterRequest request) throws AccessDeniedException {
        boolean isAdmin = request.getIsAdmin();
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + id));
        if (isAdmin) {
            if (car.isDeletedByAdmin()) {
                throw new AccessDeniedException("This car is deleted by admin and cannot be accessed.");
            }
        } else {
            if (car.isDeletedByUser()) {
                throw new AccessDeniedException("This car is deleted by user and cannot be accessed.");
            }
            if (!car.getOwner().equals(request.getOwner())) {
                throw new AccessDeniedException("You do not have permission to access this car.");
            }
        }
        return carMapper.toResponse(car);
    }
    @Override
    @Transactional
    public CarResponse createCar(CarRequest carRequest, CarFilterRequest filterRequest) throws AccessDeniedException {
        if (!Boolean.TRUE.equals(filterRequest.getIsAdmin())) {
            throw new AccessDeniedException("Only admin can create a car");
        }

        return getCarResponse(carRequest, carRepository, brandRepository, carMapper);
    }

    private CarResponse getCarResponse(CarRequest carRequest, CarRepository carRepository, BrandRepository brandRepository, CarMapper carMapper) {
        if (carRepository.findCarByCarName(carRequest.getCarName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Car name already exists");
        }

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

    @Override
    @Transactional
    public CarResponse updateCar(Long id, CarRequest carUpdated, CarFilterRequest filterRequest) throws AccessDeniedException {
        boolean isAdmin = filterRequest.getIsAdmin();  // Lấy thông tin isAdmin từ filterRequest

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));


        if (car.isDeletedByAdmin()) {
            throw new ResourceNotFoundException("Car has been deleted by admin and cannot be updated");
        }

        if (!isAdmin) {
            if (!car.getOwner().equals(carUpdated.getOwner())) {
                throw new AccessDeniedException("You can only update your own car");
            }
            if (car.isDeletedByUser()) {
                throw new ResourceNotFoundException("Car has been deleted by user and cannot be updated");
            }
        }

        Optional<Car> existCar = carRepository.findCarByCarName(carUpdated.getCarName());
        if (existCar.isPresent() && !existCar.get().getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Car name already exists");
        }
        Brand brand = brandRepository.findById(carUpdated.getCarBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand Id does not exist"));

        car.setCarName(carUpdated.getCarName());
        car.setMfg(carUpdated.getMfg());
        car.setPrice(carUpdated.getPrice());
        car.setOwner(carUpdated.getOwner());
        car.setBrand(brand);

        Car saved = carRepository.save(car);
        return carMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteCar(Long id, boolean isAdmin, String owner) throws AccessDeniedException {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + id));

        if (!isAdmin) {
            if (!car.getOwner().equals(owner)) {
                throw new AccessDeniedException("You do not have permission to perform this action.");
            }
            if (car.getOwner().equals(owner)) {
                car.setDeletedByUser(true);
                carRepository.save(car);
            }
        } else {
            car.setDeletedByAdmin(true);
            carRepository.save(car);
        }
    }

    @Override
    @Transactional
    public CarResponse restoreCar(Long id, CarFilterRequest filterRequest) throws AccessDeniedException {

        if (!filterRequest.getIsAdmin()) {
            throw new AccessDeniedException("Only admin can restore a car");
        }


        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        if (!car.isDeletedByAdmin() && !car.isDeletedByUser()) {
            throw new ResourceNotFoundException("This car is not deleted and cannot be restored");
        }

        car.setDeletedByAdmin(false);
        car.setDeletedByUser(false);

        Car restoredCar = carRepository.save(car);

        return carMapper.toResponse(restoredCar);  // Trả về thông tin xe đã khôi phục
    }

}
