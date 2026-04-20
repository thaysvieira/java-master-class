package com.thaysvieira.booking;

import com.thaysvieira.car.Car;
import com.thaysvieira.car.CarService;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            throw new IllegalArgumentException("Car id not found");
        }

        var booking = new CarBooking(UUID.randomUUID(), user, car, getPrice(startDate, endDate, car.getRentalPricePerDay()), startDate, endDate, BookingStatus.ACTIVE, LocalDateTime.now());
        var isBooked = carBookingDao.saveCarBooking(booking);

        if (!isBooked) {
            throw new IllegalArgumentException("Failed booking");
        }
        return booking;
    }


    public List<CarBooking> getCarBookingsByUser(UUID userId) {

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        List<CarBooking> bookings = carBookingDao.getAllCarBooking();

        List<CarBooking> userBookings = new ArrayList<>();
        for (CarBooking booking : bookings) {
            if (booking != null && booking.getUser().getId().equals(userId)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    public List<CarBooking> getAllBookings() {
        return carBookingDao.getAllCarBooking();
    }

    public List<Car> getAvailableCars() {

        List<CarBooking> bookings = carBookingDao.getAllCarBooking();
        List<Car> cars = carService.getCars();
        List<Car> availableCars = new ArrayList<>();

        for (Car car : cars) {
            if (car == null) continue;

            boolean isAvailable = true;

            if (bookings != null) {
                for (CarBooking booking : bookings) {
                    if (booking == null || booking.getCar() == null) break;

                    if (booking.getStatus() == BookingStatus.ACTIVE && booking.getCar().getId().equals(car.getId())) {
                        isAvailable = false;
                        break;
                    }
                }
            }
            if (isAvailable) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    public List<Car> getAvailableElectricCars() {

        List<CarBooking> bookings = carBookingDao.getAllCarBooking();
        List<Car> cars = carService.getCars();
        List<Car> electricCars = new ArrayList<>();

        for (Car car : cars) {
            if (car == null) continue;

            boolean isAvailable = true;

            if (bookings != null) {
                for (CarBooking booking : bookings) {
                    if (booking == null || booking.getCar() == null) break;

                    if (booking.getStatus() == BookingStatus.ACTIVE && booking.getCar().getId().equals(car.getId()) || !car.isElectric()) {
                        isAvailable = false;
                        break;
                    }
                }
            }
            if (isAvailable) {
                electricCars.add(car);
            }
        }
        return electricCars;
    }

    private boolean isCarAvailable(UUID carId) {

        Car car = carService.getCarById(carId);
        List<CarBooking> bookings = carBookingDao.getAllCarBooking();
        if (bookings != null) {
            for (CarBooking carBooking : bookings) {
                if (carBooking == null || carBooking.getCar() == null) break;
                if (carBooking.getStatus() == BookingStatus.ACTIVE && carBooking.getCar().getId().equals(car.getId())) {
                    return false;
                }
            }
        }
        return true;
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



