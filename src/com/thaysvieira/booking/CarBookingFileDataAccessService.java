package com.thaysvieira.booking;

import com.thaysvieira.car.*;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CarBookingFileDataAccessService implements CarBookingDao {

    private final String filePath;


    public CarBookingFileDataAccessService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveCarBooking(CarBooking carBooking) {

        try {
            writeBookingsInFile(carBooking);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<CarBooking> getAllCarBooking() {
        return readBookingsInFile();
    }

    @Override
    public CarBooking getCarBookingById(UUID carBookingId) {
        for (CarBooking booking : readBookingsInFile()) {
            if (booking == null) break;
            if (booking.getId().equals(carBookingId)) {
                return booking;
            }
        }
        return null;
    }

    public boolean cancelBooking(UUID bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking id cannot be null");
        }
        for (CarBooking cancelBooking : readBookingsInFile()) {
            if (cancelBooking == null) {
                throw new IllegalArgumentException("Booking not found");
            }
            if (cancelBooking.getId().equals(bookingId)) {
                System.out.println("Booking has been cancelled in the file too");
                cancelBooking.setStatus(BookingStatus.CANCELLED);
                writeBookingsInFile(cancelBooking);
                break;
            }
        }
        return true;
    }

    private void writeBookingsInFile(CarBooking carBooking) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {

            out.writeObject(carBooking);

        } catch (IOException e) {
            throw new RuntimeException("Failed to write bookings", e);
        }
    }

    private List<CarBooking> readBookingsInFile() {
        File file = new File(filePath);

        if (file.exists() || file.length() == 0) {
            return Collections.emptyList();
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {

            return List.of((CarBooking[]) in.readObject());

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to read bookings", e);
        }
    }
}
