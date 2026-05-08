package com.thaysvieira.booking;

import com.thaysvieira.car.Car;
import com.thaysvieira.car.CarDao;
import com.thaysvieira.car.CarFakerDataAccessService;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserDao;
import com.thaysvieira.user.UserFakerDataAccessService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class CarBookingFakerDataService implements CarBookingDao {
    private static final UserDao userFakerDataAccessService = new UserFakerDataAccessService();
    private static final CarDao carFakerDataAccessService = new CarFakerDataAccessService();
    private static final List<CarBooking> carBookings = new ArrayList<>();

    static {
        User userFaker = userFakerDataAccessService.getUserById(UUID.fromString("9b4b3c7f-1d3e-4c82-a3f9-5d6e2a8f71b4"));
        Car carMercedes = carFakerDataAccessService.getCarById(UUID.fromString("3d8f1a7b-5c42-4d9e-91f7-2b6c8e4a1f93"));
        CarBooking audiFakeBooked = new CarBooking(UUID.fromString("f2a8d941-7c65-43bb-9ef0-1a4c6d8b2e97"), userFaker, carMercedes, BigDecimal.valueOf(300),
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 6), BookingStatus.COMPLETED, LocalDateTime.of(LocalDate.of(2026, 4, 1), LocalTime.of(13, 0, 0)));
        carBookings.add(audiFakeBooked);
    }

    @Override
    public void saveCarBooking(CarBooking carBooking) {
        carBookings.add(carBooking);
    }

    @Override
    public List<CarBooking> getAllCarBooking() {
        return carBookings;
    }

    @Override
    public CarBooking getCarBookingById(UUID carBookingId) {
        return carBookings.stream()
                .filter(carBooking -> carBooking.getId().equals(carBookingId))
                .findFirst().orElseThrow(NoSuchElementException::new);
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
