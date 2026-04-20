package com.thaysvieira.booking;

import com.thaysvieira.car.Car;
import com.thaysvieira.car.CarArrayDataAccessService;
import com.thaysvieira.car.CarDao;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserArrayDataAccessService;
import com.thaysvieira.user.UserDao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class CarBookingArrayDataAccessService implements CarBookingDao {

    private static final UserDao userArrayDataAccessService = new UserArrayDataAccessService();
    private static final CarDao carArrayDataAccessService = new CarArrayDataAccessService();
    private static final int CAPACITY = 100;

    private static final CarBooking[] carBookings = new CarBooking[CAPACITY];

    static {
        User user = userArrayDataAccessService.getUserById(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
        Car carAudi = carArrayDataAccessService.getCarById(UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"));
        Car carToyota = carArrayDataAccessService.getCarById(UUID.fromString("b53815cd-d7c4-4c8d-98fc-78d236bf908e"));


        CarBooking audiBooked = new CarBooking(UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"), user, carAudi, BigDecimal.valueOf(300),
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 6), BookingStatus.ACTIVE, LocalDateTime.of(LocalDate.of(2026, 4, 1), LocalTime.of(13, 0, 0)));
        CarBooking toyotaBooked = new CarBooking(UUID.fromString("3f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d87"), user, carToyota, BigDecimal.valueOf(250),
                LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 16), BookingStatus.ACTIVE, LocalDateTime.of(LocalDate.of(2026, 6, 10), LocalTime.of(13, 0, 0)));
        carBookings[0] = audiBooked;
        carBookings[1] = toyotaBooked;
    }

    @Override
    public boolean saveCarBooking(CarBooking carBooking) {
        for (int i = 0; i < carBookings.length; i++) {
            if (carBookings[i] == null) {
                carBookings[i] = carBooking;
                return true;
            }
        }
        return false;
    }

    @Override
    public CarBooking[] getAllCarBooking() {
        return carBookings;
    }

    @Override
    public CarBooking getCarBookingById(UUID carBookingId) {
        for (CarBooking booking : carBookings) {
            if (booking != null && booking.getId().equals(carBookingId)) {
                return booking;
            }
        }
        return null;
    }

    @Override
    public boolean cancelBooking(UUID bookingId) {

        if (bookingId == null) {
            throw new IllegalArgumentException("Booking id cannot be null");
        }
        CarBooking booking = getCarBookingById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        return true;
    }

}
