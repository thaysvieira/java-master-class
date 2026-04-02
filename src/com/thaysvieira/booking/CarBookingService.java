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

import static com.thaysvieira.util.Validate.requireNonNull;
import static com.thaysvieira.util.Validate.validateDate;

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
        checkCarAvailability(car, startDate, endDate);
        CarBooking booking =
                new CarBooking(UUID.randomUUID(), user, car, getPrice(startDate, endDate, car.getRentalPricePerDay()), startDate, endDate, BookingStatus.ACTIVE, LocalDateTime.now());
        var isBooked = carBookingDao.saveCarBooking(booking);
        if (!isBooked) {
            throw new IllegalArgumentException("Failed booking");
        }
        return booking;
    }

    public boolean cancelBooking(UUID bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking id cannot be null");
        }
        CarBooking booking = carBookingDao.getCarBookingById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        return true;
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
        if (carBookingDao.getAllCarBooking() == null) {
            return new CarBooking[]{};
        }
        return carBookingDao.getAllCarBooking();
    }

    public Car[] getAvailableCars() {
        CarBooking[] bookings = carBookingDao.getAllCarBooking();
        requireNonNull(bookings, "Bookings can´t be null");
        Car[] cars = carService.getCars();
        requireNonNull(cars, "Cars can´t be null");
        int count = getCountForAvailableCars(bookings);
        Car[] availableCars = new Car[count];
        int countAvailableCar = 0;
        for (CarBooking booking : bookings) {
            if (booking != null && booking.getStatus() != BookingStatus.ACTIVE) {
                availableCars[countAvailableCar++] = booking.getCar();
            }
        }
        if (availableCars.length == 0) {
            return new Car[]{};
        }
        return availableCars;
    }

    public Car[] getAvailableElectricCars() {
        CarBooking[] bookings = carBookingDao.getAllCarBooking();
        requireNonNull(bookings, "Bookings can´t be null");
        Car[] cars = carService.getCars();
        requireNonNull(cars, "Cars can´t be null");
        int count = getCountForAvailableAndElectricCars(bookings, cars);
        Car[] electricCars = new Car[count];
        int countAvailableElectricCar = 0;
        for (Car car : cars) {
            for (CarBooking booking : bookings) {
                if (booking != null && (booking.getStatus() != (BookingStatus.ACTIVE)) && car.getId().equals(booking.getCar().getId()) && car.isElectric()) {
                    electricCars[countAvailableElectricCar++] = booking.getCar();
                }
            }
        }
        return electricCars;
    }

    private void checkCarAvailability(Car car, LocalDate startDate, LocalDate
            endDate) {
        for (CarBooking booking : carBookingDao.getAllCarBooking()) {
            if (booking == null) continue;

            boolean sameCar = booking.getCar().getId().equals(car.getId());
            boolean active = booking.getStatus() == BookingStatus.ACTIVE;

            boolean overlap = !booking.getStartDate().isAfter(endDate)
                    && !startDate.isAfter(booking.getEndDate());

            if (sameCar && active && overlap) {
                throw new IllegalArgumentException("Car is not available for the selected dates");
            }
        }

    }

    private static int getCountForAvailableCars(CarBooking[] bookings) {
        int count = 0;
        for (CarBooking booking : bookings) {
            if (booking != null && booking.getStatus() != BookingStatus.ACTIVE) {
                count++;
            }
        }
        return count;
    }

    private static int getCountForAvailableAndElectricCars(CarBooking[] bookings, Car[] cars) {
        int count = 0;
        for (Car car : cars) {
            for (CarBooking booking : bookings) {
                if (booking != null && booking.getStatus() != BookingStatus.ACTIVE && car.getId().equals(booking.getCar().getId()) && car.isElectric()) {
                    count++;
                }
            }
        }
        return count;
    }

    public BigDecimal getPrice(LocalDate startDate, LocalDate endDate, BigDecimal rentalCarPricePerDay) {
        if (startDate == null && endDate == null && rentalCarPricePerDay == null) {
            throw new IllegalArgumentException("Arguments can´t be null");
        }
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        return rentalCarPricePerDay.multiply(BigDecimal.valueOf(numberOfDays));
    }

}
