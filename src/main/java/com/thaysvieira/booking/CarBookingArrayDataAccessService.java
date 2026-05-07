package com.thaysvieira.booking;

import com.thaysvieira.car.Car;
import com.thaysvieira.car.CarArrayDataAccessService;
import com.thaysvieira.car.CarDao;
import com.thaysvieira.car.CarFakerDataAccessService;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserArrayDataAccessService;
import com.thaysvieira.user.UserDao;
import com.thaysvieira.user.UserFakerDataAccessService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarBookingArrayDataAccessService implements CarBookingDao {

    private static final UserDao userArrayDataAccessService = new UserArrayDataAccessService();
    private static final UserDao userFakerDataAccessService = new UserFakerDataAccessService();
    private static final CarDao carArrayDataAccessService = new CarArrayDataAccessService();
    private static final CarDao carFakerDataAccessService = new CarFakerDataAccessService();


    private static final List<CarBooking> carBookings = new ArrayList<>();

    static {
        User userFaker = userFakerDataAccessService.getUserById(UUID.fromString("9b4b3c7f-1d3e-4c82-a3f9-5d6e2a8f71b4"));
        User user = userArrayDataAccessService.getUserById(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
        Car carAudi = carArrayDataAccessService.getCarById(UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"));
        Car carToyota = carArrayDataAccessService.getCarById(UUID.fromString("b53815cd-d7c4-4c8d-98fc-78d236bf908e"));
        Car carMercedes = carFakerDataAccessService.getCarById(UUID.fromString("3d8f1a7b-5c42-4d9e-91f7-2b6c8e4a1f93"));


        CarBooking audiBooked = new CarBooking(UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"), user, carAudi, BigDecimal.valueOf(300),
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 6), BookingStatus.ACTIVE, LocalDateTime.of(LocalDate.of(2026, 4, 1), LocalTime.of(13, 0, 0)));
        CarBooking toyotaBooked = new CarBooking(UUID.fromString("3f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d87"), user, carToyota, BigDecimal.valueOf(250),
                LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 16), BookingStatus.ACTIVE, LocalDateTime.of(LocalDate.of(2026, 6, 10), LocalTime.of(13, 0, 0)));
        CarBooking audiFakeBooked = new CarBooking(UUID.fromString("f2a8d941-7c65-43bb-9ef0-1a4c6d8b2e97"), userFaker, carMercedes, BigDecimal.valueOf(300),
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 6), BookingStatus.COMPLETED, LocalDateTime.of(LocalDate.of(2026, 4, 1), LocalTime.of(13, 0, 0)));
        carBookings.add(audiBooked);
        carBookings.add(toyotaBooked);
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
                .findFirst().orElse(null);
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
