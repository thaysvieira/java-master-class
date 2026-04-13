package com.thaysvieira.booking;

import com.thaysvieira.car.*;

import java.io.*;
import java.util.UUID;

public class CarBookingFileDataAccessService implements CarBookingDao {

    private final String filePath;


    public CarBookingFileDataAccessService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean saveCarBooking(CarBooking updateBooking) {
        if (updateBooking == null) {
            return false;
        }
        try {
            writeBookingsInFile(updateBooking);
            System.out.println("Data has been saved correctly as file!");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public CarBooking[] getAllCarBooking() {
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

    private CarBooking[] readBookingsInFile() {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return new CarBooking[0];
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (CarBooking[]) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to read bookings", e);
        }
    }
}
