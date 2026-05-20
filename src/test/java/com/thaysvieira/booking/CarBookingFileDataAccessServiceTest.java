package com.thaysvieira.booking;

import com.thaysvieira.car.Brand;
import com.thaysvieira.car.Car;
import com.thaysvieira.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarBookingFileDataAccessServiceTest {
    @TempDir
    Path tempDir;
    private Path filePath;
    private CarBookingFileDataAccessService underTest;
    private static CarBooking carBooking;

    @BeforeAll
    static void beforeAll() {
        Car car = new Car(
                UUID.fromString("b53815cd-d7c4-4c8d-98fc-78d236bf908e"),
                "b53815cd",
                BigDecimal.valueOf(30),
                Brand.TOYOTA,
                false
        );
        User user = new User(
                UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"),
                "Jhon"
        );

        carBooking = new CarBooking(
                UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"),
                user,
                car,
                BigDecimal.valueOf(300),
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 6),
                BookingStatus.ACTIVE,
                LocalDateTime.of(2026, 4, 1, 13, 0)
        );
    }

    @BeforeEach
    void setUp() {
        filePath = tempDir.resolve("bookings.txt");
        underTest = new CarBookingFileDataAccessService(filePath.toString());
    }

    @Test
    void saveCarBooking() throws Exception {
        underTest.saveCarBooking(carBooking);
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            CarBooking savedBooking = (CarBooking) in.readObject();
            assertEquals(carBooking, savedBooking);
        }
    }

    @Test
    void getAllCarBooking() {

        underTest.saveCarBooking(carBooking);
        List<CarBooking> bookings = underTest.getAllCarBooking();
        assertEquals(1, bookings.size());
        assertEquals(carBooking, bookings.getFirst());
    }

    @Test
    void getCarBookingById() {
        underTest.saveCarBooking(carBooking);
        CarBooking booking = underTest.getCarBookingById(UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"));
        assertEquals(booking, carBooking);
    }

    @Test
    void cancelBooking() {
        underTest.saveCarBooking(carBooking);
        underTest.cancelBooking(carBooking.getId());
        List<CarBooking> bookings = underTest.getAllCarBooking();
        assertEquals(BookingStatus.CANCELLED,
                bookings.getFirst().getStatus());
    }
}