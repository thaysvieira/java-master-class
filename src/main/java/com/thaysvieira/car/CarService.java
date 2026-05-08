package com.thaysvieira.car;

import java.util.List;
import java.util.UUID;

public class CarService {
    private final CarDao carArrayDataAccessService;

    public CarService(CarDao carArrayDataAccessService) {
        this.carArrayDataAccessService = carArrayDataAccessService;
    }

    public Car getCarById(UUID carId) {
        if (carId == null) {
            throw new IllegalArgumentException("Car id cannot be null");
        }
        return carArrayDataAccessService.getCarById(carId);
    }

    public List<Car> getCars() {
        return carArrayDataAccessService.getAllCars();
    }
}
