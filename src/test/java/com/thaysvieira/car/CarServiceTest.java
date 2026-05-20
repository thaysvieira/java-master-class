package com.thaysvieira.car;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @Mock
    private CarDao carDao;
    @InjectMocks
    private CarService underTest;

    private static final List<Car> cars = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        cars.add(new Car(UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"), "28fbcd64", BigDecimal.valueOf(70), Brand.AUDI, true));
        cars.add(new Car(UUID.fromString("b53815cd-d7c4-4c8d-98fc-78d236bf908e"), "b53815cd", BigDecimal.valueOf(30), Brand.TOYOTA, false));
        cars.add(new Car(UUID.fromString("900eb4a4-4a43-4a02-9afe-723006eb0f6a"), "900eb4a4", BigDecimal.valueOf(100), Brand.MERCEDES, true));
    }

    @Test
    void canGetCarById() {
        given(underTest.getCars()).willReturn(cars);
        List<Car> carList = underTest.getCars();
        assertThat(carList).isNotNull();
        assertThat(carList.size()).isEqualTo(3);
        verify(carDao).getAllCars();
    }

    @Test
    void getCars() {
        given(carDao.getCarById(UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"))).willReturn(cars.getFirst());
        Car car = carDao.getCarById(UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"));
        assertThat(car).isNotNull();
        assertThat(car.getId()).isEqualTo(UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"));
        verify(carDao).getCarById(UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"));

    }
}