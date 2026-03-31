package com.thaysvieira.util;

import com.thaysvieira.booking.BookingStatus;
import com.thaysvieira.booking.CarBooking;
import com.thaysvieira.car.Car;

import java.time.LocalDate;

public class Utils {

    public static void validateDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }
}
