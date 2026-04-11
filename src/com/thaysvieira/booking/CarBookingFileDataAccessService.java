package com.thaysvieira.booking;

import com.thaysvieira.car.*;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserArrayDataAccessService;
import com.thaysvieira.user.UserDao;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class CarBookingFileDataAccessService implements CarBookingDao {

    private final String filePath;

    private static final UserDao userArrayDataAccessService = new UserArrayDataAccessService();
    private static final CarDao carArrayDataAccessService = new CarArrayDataAccessService();
    private static final int CAPACITY = 100;
    private static final CarBooking[] carBookings = new CarBooking[CAPACITY];

    static {

        User user = userArrayDataAccessService.getUserById(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
        Car carAudi = carArrayDataAccessService.getCarById(UUID.fromString("28fbcd64-c811-4fe9-8e4b-ac8166f267b1"));
        Car carToyota = carArrayDataAccessService.getCarById(UUID.fromString("b53815cd-d7c4-4c8d-98fc-78d236bf908e"));


        CarBooking audiBooked = new CarBooking(UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"), user, carAudi, BigDecimal.valueOf(300),
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 6), BookingStatus.ACTIVE, LocalDateTime.of(LocalDate.of(2026, 4, 1), LocalTime.of(13, 0, 0)));
        CarBooking toyotaBooked = new CarBooking(UUID.fromString("3f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d87"), user, carToyota, BigDecimal.valueOf(250),
                LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 16), BookingStatus.ACTIVE, LocalDateTime.of(LocalDate.of(2026, 6, 10), LocalTime.of(13, 0, 0)));
        carBookings[0] = audiBooked;
        carBookings[1] = toyotaBooked;
    }

    public CarBookingFileDataAccessService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean saveCarBooking(CarBooking carBooking) {
        if (carBooking == null) {
            return false;
        }
        try {
            writeBookingsInFile(carBookings);
            CarBooking[] bookings = readBookingsInFile();

            CarBooking[] updateBookings = new CarBooking[carBookings.length];

            for (int i = 0; i < bookings.length; i++) {
                if (updateBookings[i] == null) {
                    updateBookings[i] = bookings[i];
                }
                if (bookings[i] == null) {
                    updateBookings[i] = carBooking;
                    carBookings[i] = carBooking;
                    break;
                }
            }
            writeBookingsInFile(updateBookings);
            System.out.println("Data has been saved correctly as file!");
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public CarBooking[] getAllCarBooking() {
        writeBookingsInFile(carBookings);
        if (readBookingsInFile() == null) {
            throw new RuntimeException("The booking file is empty.");
        }
        return readBookingsInFile();
    }

    @Override
    public CarBooking getCarBookingById(UUID carBookingId) {
        writeBookingsInFile(carBookings);
        if (readBookingsInFile() == null) {
            throw new RuntimeException("The booking file is empty.");
        }
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
        writeBookingsInFile(carBookings);
        for (CarBooking cancelBooking : carBookings) {
            if (cancelBooking == null) {
                throw new IllegalArgumentException("Booking not found");
            }
            if (cancelBooking.getId().equals(bookingId)) {
                System.out.println("Booking has been cancelled in the file too");
                cancelBooking.setStatus(BookingStatus.CANCELLED);
                break;
            }
        }
        return true;
    }

    private void writeBookingsInFile(CarBooking[] carBookings) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {

            out.writeObject(carBookings);

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
