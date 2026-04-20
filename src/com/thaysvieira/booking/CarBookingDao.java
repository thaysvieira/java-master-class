package com.thaysvieira.booking;

import java.util.List;
import java.util.UUID;

public interface CarBookingDao {

    boolean saveCarBooking(CarBooking carBooking);

    List<CarBooking> getAllCarBooking();

    CarBooking getCarBookingById(UUID carBookingId);

    boolean cancelBooking(UUID bookingId);
}
