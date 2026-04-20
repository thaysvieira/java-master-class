package com.thaysvieira.car;

import java.math.BigDecimal;
import java.util.UUID;

public class CarArrayDataAccessService implements CarDao {
    private static Car[] cars;

    static {
        cars = new Car[]{
                new Car(UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"), "28fbcd64", BigDecimal.valueOf(70), Brand.AUDI, true),
                new Car(UUID.fromString("b53815cd-d7c4-4c8d-98fc-78d236bf908e"), "b53815cd", BigDecimal.valueOf(30), Brand.TOYOTA, false),
                new Car(UUID.fromString("900eb4a4-4a43-4a02-9afe-723006eb0f6a"), "900eb4a4", BigDecimal.valueOf(100), Brand.MERCEDES, true),
                new Car(UUID.fromString("120759d2-a1ba-4bd5-99e8-08e7c82fac30"), "120759d2", BigDecimal.valueOf(150), Brand.TESLA, true),
                new Car(UUID.fromString("3d4a1c67-1d23-4a73-9f7c-0a1f21b6a111"), "AUDI001", BigDecimal.valueOf(75), Brand.AUDI, true),
                new Car(UUID.fromString("6b1e9e1d-8f4e-4e1b-9f27-2b1e0c7e2222"), "AUDI002", BigDecimal.valueOf(80), Brand.AUDI, false),
                new Car(UUID.fromString("f0d3a7b5-0d21-4c4e-b7f1-5c4a2c9e3333"), "TOY001", BigDecimal.valueOf(35), Brand.TOYOTA, false),
                new Car(UUID.fromString("b7c8d2f1-3f6b-4d3c-9c55-1c2b3a4e4444"), "TOY002", BigDecimal.valueOf(40), Brand.TOYOTA, true),
                new Car(UUID.fromString("d4a6f9e2-7a3d-4a93-a12f-7c1e8b5d5555"), "MER001", BigDecimal.valueOf(110), Brand.MERCEDES, true),
                new Car(UUID.fromString("a1b7c9d8-6e2f-4b71-b0c5-9f1e2d3c6666"), "MER002", BigDecimal.valueOf(120), Brand.MERCEDES, false),
                new Car(UUID.fromString("c5a8f2d9-4c1e-4a83-b1f7-2a9c3d8b7777"), "TES001", BigDecimal.valueOf(160), Brand.TESLA, true),
                new Car(UUID.fromString("e2d7c4b1-9f3a-4b6c-8a2e-5d1f7c9a8888"), "TES002", BigDecimal.valueOf(170), Brand.TESLA, true)
        };
    }

    @Override
    public Car getCarById(UUID carId) {
        for (Car car : cars) {
            if (car != null && car.getId().equals(carId)) {
                return car;
            }
        }
        throw new IllegalArgumentException("Car not found");
    }

    @Override
    public Car[] getAllCars() {
        return cars;
    }
}
