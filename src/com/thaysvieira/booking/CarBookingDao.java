package com.thaysvieira.booking;

import java.util.UUID;

public interface CarBookingDao {

    boolean saveCarBooking(CarBooking carBooking);

    CarBooking[] getAllCarBooking();

    CarBooking getCarBookingById(UUID carBookingId);

    boolean cancelBooking(UUID bookingId);
}
