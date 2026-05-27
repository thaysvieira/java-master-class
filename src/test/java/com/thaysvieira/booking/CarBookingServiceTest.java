package com.thaysvieira.booking;

import com.thaysvieira.car.*;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CarBookingServiceTest {
    @Mock
    private CarBookingDao carBookingDao;
    @Mock
    private  CarService carService;
    @Mock
    private  UserService userService;
    @InjectMocks
    private CarBookingService underTest;
    private static final List<CarBooking> carBookings = new ArrayList<>();
    private static final List<Car> cars = new ArrayList<>();


    @BeforeAll
    static void beforeAll() {
        User user = new User(
                UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"),
                "Jhon");

        Car audi = new Car(
                UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"),
                "28fbcd64",
                BigDecimal.valueOf(70), Brand.AUDI, true);

        Car toyota = new Car(
                UUID.fromString("b53815cd-d7c4-4c8d-98fc-78d236bf908e"),
                "b53815cd",
                BigDecimal.valueOf(30),
                Brand.TOYOTA,
                false);
        Car tesla = new Car(
                UUID.fromString("120759d2-a1ba-4bd5-99e8-08e7c82fac30"),
                "120759d2",
                BigDecimal.valueOf(150),
                Brand.TESLA,
                true);

        Car mercedes = new Car(
                UUID.fromString("d4a6f9e2-7a3d-4a93-a12f-7c1e8b5d5555"),
                "MER001",
                BigDecimal.valueOf(110),
                Brand.MERCEDES, true);

        CarBooking audiBooked = new CarBooking(
                UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"),
                user,
                audi,
                BigDecimal.valueOf(300),
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 6),
                BookingStatus.ACTIVE,
                LocalDateTime.of(LocalDate.of(2026, 4, 1),
                        LocalTime.of(13, 0, 0)));

        CarBooking toyotaBooked = new CarBooking(
                UUID.fromString("3f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d87"),
                user,
                toyota,
                BigDecimal.valueOf(250),
                LocalDate.of(2026, 6, 10),
                LocalDate.of(2026, 6, 16),
                BookingStatus.ACTIVE,
                LocalDateTime.of(LocalDate.of(2026, 6, 10),
                        LocalTime.of(13, 0, 0)));

        carBookings.add(audiBooked);
        carBookings.add(toyotaBooked);
        cars.add(audi);
        cars.add(toyota);
        cars.add(tesla);
        cars.add(mercedes);
    }

    @Test
    void shouldBookCar() {
        UUID userId = UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474");
        UUID carId = UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1");

        User user = new User(userId, "john");

        Car car = new Car(
                carId,
                "28fbcd64",
                BigDecimal.valueOf(70),
                Brand.AUDI,
                true
        );

        LocalDate startDate = LocalDate.of(2026, 8, 1);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        given(userService.getUserById(userId)).willReturn(user);
        given(carService.getCarById(carId)).willReturn(car);
        given(carService.getCars()).willReturn(List.of(car));
        given(carBookingDao.getAllCarBooking()).willReturn(List.of());

        CarBooking booking = underTest.bookCar(userId, carId, startDate, endDate);

        assertThat(booking).isNotNull();
        assertThat(booking.getUser()).isEqualTo(user);
        assertThat(booking.getCar()).isEqualTo(car);
        assertThat(booking.getPrice()).isEqualTo(BigDecimal.valueOf(210));
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.ACTIVE);

        verify(carBookingDao).saveCarBooking(booking);
    }

    @Test
    void shouldGetCarBookingsByUser() {
        UUID userId = UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474");
        User user = new User(userId, "john");

        given(userService.getUserById(userId)).willReturn(user);
        given(carBookingDao.getAllCarBooking()).willReturn(carBookings);

        List<CarBooking> carBookingList = underTest.getCarBookingsByUser(userId);
        assertThat(carBookingList).isNotNull();
        assertThat(carBookingList.size()).isEqualTo(2);
        assertThat(carBookingList.get(0).getUser().getId()).isEqualTo(userId);

    }

    @Test
    void shouldReturnAllBookings() {
        given(carBookingDao.getAllCarBooking()).willReturn(carBookings);
        List<CarBooking> carBookingList = underTest.getAllBookings();
        assertThat(carBookingList).isNotNull();
        assertThat(carBookingList.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnAvailableElectricCars() {

        given(carBookingDao.getAllCarBooking()).willReturn(carBookings);
        given(carService.getCars()).willReturn(cars);
        List<Car> availableElectricCars = underTest.getAvailableElectricCars();
        assertThat(availableElectricCars).isNotNull();
        assertThat(availableElectricCars.size()).isEqualTo(2);

    }
}