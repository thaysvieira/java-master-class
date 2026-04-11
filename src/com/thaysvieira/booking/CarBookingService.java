package com.thaysvieira.booking;

import com.thaysvieira.car.Car;
import com.thaysvieira.car.CarService;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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


    public CarBooking[] getCarBookingsByUser(UUID userId) {

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        CarBooking[] bookings = carBookingDao.getAllCarBooking();
        int count = 0;
        for (CarBooking booking : bookings) {
            if (booking != null && booking.getUser().getId().equals(userId)) {
                count++;
            }
        }

        CarBooking[] userBookings = new CarBooking[count];
        int userCount = 0;
        for (CarBooking booking : bookings) {
            if (booking != null && booking.getUser().getId().equals(userId)) {
                userBookings[userCount++] = booking;
            }
        }
        return userBookings;
    }

    public CarBooking[] getAllBookings() {
        return carBookingDao.getAllCarBooking();
    }

    public Car[] getAvailableCars() {

        CarBooking[] bookings = carBookingDao.getAllCarBooking();
        Car[] cars = carService.getCars();
        int count = getCountForAvailableCars(bookings, cars);
        Car[] availableCars = new Car[count];
        int countAvailableCar = 0;

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
                availableCars[countAvailableCar++] = car;
            }
        }
        return availableCars;
    }

    public Car[] getAvailableElectricCars() {

        CarBooking[] bookings = carBookingDao.getAllCarBooking();
        Car[] cars = carService.getCars();
        int count = getCountForAvailableAndElectricCars(bookings, cars);
        Car[] electricCars = new Car[count];
        int countAvailableElectricCar = 0;

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
                electricCars[countAvailableElectricCar++] = car;
            }
        }
        return electricCars;
    }

    private boolean isCarAvailable(UUID carId) {

        Car car = carService.getCarById(carId);
        CarBooking[] bookings = carBookingDao.getAllCarBooking();
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

    private static int getCountForAvailableCars(CarBooking[] bookings, Car[] cars) {
        if (cars == null) {
            throw new IllegalArgumentException("Cars cannot be null");
        }
        int count = 0;

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
                count++;
            }
        }

        return count;
    }

    private static int getCountForAvailableAndElectricCars(CarBooking[] bookings, Car[] cars) {

        if (cars == null) {
            throw new IllegalArgumentException("Cars cannot be null");
        }
        int count = 0;

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
                count++;
            }
        }
        return count;
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



