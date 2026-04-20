package com.thaysvieira.car;

import java.util.UUID;

public interface CarDao {

    Car getCarById(UUID carId);

    Car[] getAllCars();
}
