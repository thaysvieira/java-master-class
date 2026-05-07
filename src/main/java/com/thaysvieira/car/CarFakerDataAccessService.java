package com.thaysvieira.car;

import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarFakerDataAccessService implements CarDao {
    private static final List<Car> cars = new ArrayList<>();

    static {
        Faker faker = new Faker();
        cars.add(new Car(UUID.fromString("3d8f1a7b-5c42-4d9e-91f7-2b6c8e4a1f93"), faker.number().digits(5), BigDecimal.valueOf(faker.number().numberBetween(50, 200)), Brand.MERCEDES, true));
        cars.add(new Car(UUID.fromString("b7e4c921-8f5a-4c3d-a2b8-6d1f9e7a5c20"), faker.number().digits(5), BigDecimal.valueOf(faker.number().numberBetween(50, 200)), Brand.AUDI, false));
    }

    @Override
    public Car getCarById(UUID carId) {
        return cars.stream()
                .filter(car -> car.getId().equals(carId))
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public List<Car> getAllCars() {
        return cars;
    }
}
