package com.thaysvieira.car;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CarArrayDataAccessServiceTest {

    private CarArrayDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CarArrayDataAccessService();
    }


    @Test
    void canGetCarById() {
        UUID id = UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1");
        Car car = underTest.getCarById(id);
        assertThat(car).isNotNull();
        assertThat(car.getId()).isEqualTo(id);
        assertThat(car.isElectric()).isTrue();
    }

    @Test
    void getAllCars() {
        List<Car> carList = underTest.getAllCars();
        assertThat(carList).isNotNull();
        assertThat(carList.size()).isEqualTo(12);
    }
}