package com.thaysvieira.car;

import java.util.UUID;

public class CarService {
    private final CarDao carDao = new CarDao();

    public Car getCarById(UUID carId) {
        if (carId == null) {
            throw new IllegalArgumentException("Car id cannot be null");
        }
        return carDao.getCarById(carId);
    }

    public Car[] getCars() {
        return carDao.getAllCars();
    }
}
