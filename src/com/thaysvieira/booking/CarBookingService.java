package com.thaysvieira.booking;

import com.thaysvieira.car.Car;
import com.thaysvieira.car.CarService;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.thaysvieira.util.Utils.validateDate;

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
        checkCarAvailability(car,startDate,endDate);
        CarBooking booked =
                new CarBooking(UUID.randomUUID(),user,car,startDate,endDate,BookingStatus.ACTIVE, LocalDateTime.now());

        return carBookingDao.saveCarBooking(booked);
    }

    public CarBooking cancelBooking(UUID bookingId){
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking id cannot be null");
        }
        CarBooking booking = carBookingDao.getCarBookingById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }
        return carBookingDao.cancelBooking(bookingId);
    }

    public CarBooking[] getCarBookingsByUser(UUID userId){
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        return carBookingDao.getCarBookingsByUser(userId);
    }

    public CarBooking[] getAllBookings(){
        if(carBookingDao.getAllCarBooking() == null){
            return new CarBooking[0];
        }
        return carBookingDao.getAllCarBooking();
    }

    public Car[] getAvailableCars(){
        if(carBookingDao.getAvailableCars() == null){
            return new Car[0];
        }
        return carBookingDao.getAvailableCars();
    }
    public Car[] getAvailableElectricCars(){
        if(carBookingDao.getElectricCarsAvailable() == null){
            return new Car[0];
        }
        return carBookingDao.getElectricCarsAvailable();
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

}
