package com.thaysvieira.booking;

import com.thaysvieira.car.Car;
import com.thaysvieira.car.CarService;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.thaysvieira.util.ValidateDate.validateDate;

public class CarBookingService {

    private final CarBookingDao carBookingDao;
    private final CarService carService;
    private final UserService userService;

    public CarBookingService(CarBookingDao carBookingDao, CarService carService, UserService userService) {
        this.carBookingDao = carBookingDao;
        this.carService = carService;
        this.userService = userService;
    }


    public CarBooking bookCar(UUID userId, UUID carId, LocalDate startDate, LocalDate
            endDate) {
        //Look up user by userId
        User user = userService.getUserById(userId);
        //Look up car by carId
        Car car = carService.getCarById(carId);
        //validate dates
        validateDate(startDate, endDate);

        if (!isCarAvailable(carId)) {
            throw new IllegalArgumentException("Car not available");
        }

        var booking = new CarBooking(UUID.randomUUID(), user, car, getPrice(startDate, endDate, car.getRentalPricePerDay()), startDate, endDate, BookingStatus.ACTIVE, LocalDateTime.now());
        carBookingDao.saveCarBooking(booking);
        return booking;
    }


    public List<CarBooking> getCarBookingsByUser(UUID userId) {

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        List<CarBooking> bookings = carBookingDao.getAllCarBooking();
        return bookings.stream().filter(booking -> booking.getUser().getId().equals(userId)).toList();
    }

    public List<CarBooking> getAllBookings() {
        return carBookingDao.getAllCarBooking();
    }

    public List<Car> getAvailableCars() {

        List<CarBooking> bookings = carBookingDao.getAllCarBooking();
        List<Car> cars = carService.getCars();
        Set<UUID> activeCarBookings = getUUIdsActiveCars(bookings);

        return cars.stream()
                .filter(car -> !activeCarBookings.contains(car.getId()))
                .toList();
    }

    public List<Car> getAvailableElectricCars() {

        List<CarBooking> bookings = carBookingDao.getAllCarBooking();
        List<Car> cars = carService.getCars();

        Set<UUID> activeCarBookings = getUUIdsActiveCars(bookings);

        return cars.stream()
                .filter(car -> !activeCarBookings.contains(car.getId()))
                .filter(Car::isElectric)
                .toList();
    }

    private Set<UUID> getUUIdsActiveCars(List<CarBooking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.ACTIVE)
                .map(carBooking -> carBooking.getCar().getId())
                .collect(Collectors.toSet());
    }

    private boolean isCarAvailable(UUID carId) {

        List<CarBooking> bookings = carBookingDao.getAllCarBooking();
        List<Car> cars = carService.getCars();

        Set<UUID> activeCarBookings = getUUIdsActiveCars(bookings);

        return cars.stream()
                .anyMatch(car -> !activeCarBookings.contains(carId));
    }


    public BigDecimal getPrice(LocalDate startDate, LocalDate endDate, BigDecimal rentalCarPricePerDay) {

        if (startDate == null && endDate == null && rentalCarPricePerDay == null) {
            throw new IllegalArgumentException("Arguments can´t be null");
        }

        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);

        if (numberOfDays < 1) {
            throw new IllegalArgumentException("Number of the days can't be negative.");
        }
        return rentalCarPricePerDay.multiply(BigDecimal.valueOf(numberOfDays));
    }

}



