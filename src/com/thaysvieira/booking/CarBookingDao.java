package com.thaysvieira.booking;

import com.thaysvieira.car.Car;
import com.thaysvieira.car.CarDao;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserDao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class CarBookingDao {
    private static final UserDao userDao = new UserDao();
    private static final CarDao carDao = new CarDao();
    private static final int CAPACITY = 100;

    private static final CarBooking[] carBookings = new CarBooking[CAPACITY];

    static {
        User user = userDao.getUserById(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
        Car carAudi = carDao.getCarById(UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"));
        Car carToyota = carDao.getCarById(UUID.fromString("b53815cd-d7c4-4c8d-98fc-78d236bf908e"));
        CarBooking audiBooked = new CarBooking(UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"), user, carAudi,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 6), BookingStatus.COMPLETED, LocalDateTime.of(LocalDate.of(2026, 4, 1), LocalTime.of(13, 0, 0)));
        CarBooking toyotaBooked = new CarBooking(UUID.fromString("3f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d87"), user, carToyota,
                LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 16), BookingStatus.COMPLETED, LocalDateTime.of(LocalDate.of(2026, 6, 10), LocalTime.of(13, 0, 0)));
        carBookings[0] = audiBooked;
        carBookings[1] = toyotaBooked;
    }

    public CarBooking saveCarBooking(CarBooking carBooking) {
        for (int i = 0; i < carBookings.length; i++) {
            if (carBookings[i] == null) {
                carBookings[i] = carBooking;
                return carBooking;
            }
        }
        throw new IllegalArgumentException("No space available to save booking");
    }


    public CarBooking[] getAllCarBooking() {
        return carBookings;
    }

    public CarBooking getCarBookingById(UUID carBookingId) {

        for (CarBooking booking : carBookings) {
            if (booking != null && booking.getId().equals(carBookingId)) {
                return booking;
            }
        }
        throw new IllegalArgumentException("Booking not found");
    }

    public CarBooking cancelBooking(UUID bookId) {
        for (CarBooking booking : carBookings) {
            if (booking == null) continue;
            if (booking.getId().equals(bookId)) {
                booking.setStatus(BookingStatus.CANCELLED);
                return booking;
            }
        }
        throw new IllegalArgumentException("Booking not found");
    }

    public CarBooking[] getCarBookingsByUser(UUID userId) {
        userDao.getUserById(userId);
        CarBooking[] bookings = getAllCarBooking();
        CarBooking[] userBookings = new CarBooking[bookings.length];
        int count = 0;
        for (CarBooking booking : bookings) {
            if (booking != null && booking.getUser().getId().equals(userId)) {
                userBookings[count] = booking;
                count++;
            }
        }
        if (count == 0) {
            return new CarBooking[0];
        }
        CarBooking[] result = new CarBooking[count];
        for (int i = 0; i < count; i++) {
            result[i] = userBookings[i];
        }
        return result;
    }

    public Car[] getAvailableCars() {
        CarBooking[] bookings = getAllCarBooking();
        Car[] cars = carDao.getAllCars();
        Car[] availableCars = new Car[cars.length];
        int count = 0;

        for(Car car: cars){
            if (isAvailable(car,bookings)){
                availableCars[count] =car;
                count++;
            }
        }
        if (count == 0) {
            return new Car[0];
        }
        return getCars(count, availableCars);
    }

    public Car[] getElectricCarsAvailable() {
        CarBooking[] bookings = getAllCarBooking();
        Car[] cars = carDao.getAllCars();
        Car[] electricAvailableCars = new Car[cars.length];
        int count = 0;

        for(Car car: cars){
            if (isAvailable(car,bookings) && car.isElectric()){
                electricAvailableCars[count] =car;
                count++;
            }
        }
        if (count == 0) {
            return new Car[0];
        }
        return getCars(count, electricAvailableCars);
    }

    private static Car[] getCars(int count, Car[] availableCars) {
        Car[] result = new Car[count];
        for (int i = 0; i < count; i++) {
            result[i] = availableCars[i];
        }
        return result;
    }

    private boolean isAvailable(Car car,CarBooking[] bookings){
            for (CarBooking booking : bookings) {
                if (booking != null && booking.getCar().getId().equals(car.getId()) && (booking.getStatus() == BookingStatus.ACTIVE)) {
                    return false;
                }
            }
            return true;
        }
}
