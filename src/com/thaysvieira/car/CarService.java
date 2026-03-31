package com.thaysvieira.car;

import java.util.UUID;

public class CarService {
    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }
    public Car getCarById(UUID carId){
        if (carId == null) {
            throw new IllegalArgumentException("Car id cannot be null");
        }
        return carDao.getCarById(carId);
    }

    public Car[] getCars(){
        Car[] cars = carDao.getAllCars();
        if(carDao.getAllCars()== null){
            return new Car[0];
        }
        return cars;
    }
}
