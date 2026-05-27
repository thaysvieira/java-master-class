package com.thaysvieira.booking;

import com.thaysvieira.car.Brand;
import com.thaysvieira.car.Car;
import com.thaysvieira.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CarBookingArrayDataAccessServiceTest {
    private CarBookingArrayDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CarBookingArrayDataAccessService();
    }

    @Test
    void shouldSaveCarBooking() {

        Car toyota = new Car(
                UUID.fromString("b53815cd-d7c4-4c8d-98fc-78d236bf908e"),
                "b53815cd",
                BigDecimal.valueOf(30),
                Brand.TOYOTA,
                false);

        User user = new User(
                UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"),
                "Jhon");

        CarBooking booking = new CarBooking(
                UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"),
                user,
                toyota,
                BigDecimal.valueOf(300),
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 6),
                BookingStatus.ACTIVE, LocalDateTime.of(LocalDate.of(2026, 4, 1),
                LocalTime.of(13, 0, 0)));

        underTest.saveCarBooking(booking);
        List<CarBooking> bookings = underTest.getAllCarBooking();
        assertThat(bookings).isNotNull();
        assertThat(bookings.size()).isEqualTo(3);
    }

    @Test
    void shouldReturnAllCarBooking() {
        List<CarBooking> bookings = underTest.getAllCarBooking();
        assertThat(bookings).isNotNull();
        assertThat(bookings.size()).isEqualTo(3);
    }

    @Test
    void shouldGetCarBookingById() {
        UUID bookingId = UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80");
        CarBooking booking = underTest.getCarBookingById(bookingId);
        assertThat(booking).isNotNull();
        assertThat(booking.getCar().isElectric()).isTrue();
        assertThat(booking.getId().equals(bookingId)).isTrue();
    }

    @Test
    void shouldCancelBooking() {
        CarBooking booking = underTest.getCarBookingById(UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"));
        underTest.cancelBooking(booking.getId());
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }
}