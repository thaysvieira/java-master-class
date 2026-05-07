package com.thaysvieira.car;

import java.util.List;
import java.util.UUID;

public interface CarDao {

    Car getCarById(UUID carId);

    List<Car> getAllCars();
}
