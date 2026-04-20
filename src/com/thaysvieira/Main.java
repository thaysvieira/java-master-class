package com.thaysvieira;
// TODO 1. create a new branch called initial-implementation
// TODO 2. create a package with your name. i.e com.franco and move this file inside the new package
// TODO 3. implement https://amigoscode.com/learn/java-cli-build/lectures/3a83ecf3-e837-4ae5-85a8-f8ae3f60f7f5

import com.thaysvieira.booking.*;
import com.thaysvieira.car.*;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserArrayDataAccessService;
import com.thaysvieira.user.UserDao;
import com.thaysvieira.user.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    static void main(String[] args) {

        CarBookingDao carBookingDaoFile = new CarBookingFileDataAccessService("bookings.dat");
        CarBookingDao carBooking = new CarBookingArrayDataAccessService();
        CarDao carDao = new CarArrayDataAccessService();
        CarService carService = new CarService(carDao);
        UserDao userDao = new UserArrayDataAccessService();
        UserService userService = new UserService(userDao);
        CarBookingService carBookingService = new CarBookingService(carBooking, carService, userService);
        CarBookingService carBookingServiceFile = new CarBookingService(carBookingDaoFile, carService, userService);


        System.out.println("Java Master Class");


        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            showMenu();
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    // Book a car saved in file
                    CarBooking booking = carBookingService.bookCar(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"), UUID.fromString("120759d2-a1ba-4bd5-99e8-08e7c82fac30"), LocalDate.now(), LocalDate.of(2026, 8, 4));
                    System.out.println(booking);
                    CarBooking saveBookingInFile = carBookingServiceFile.bookCar(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"), UUID.fromString("120759d2-a1ba-4bd5-99e8-08e7c82fac30"), LocalDate.now(), LocalDate.of(2026, 8, 4));
                    System.out.println(saveBookingInFile);
                    break;
                case 2:
                    // delete booking
                    if (carBooking.cancelBooking(UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"))) {
                        System.out.println("Car is successfully deleted");
                    } else {
                        System.out.println("Couldn't delete the car");
                    }
                    break;
                case 3:
                    // View car booked by the user
                    CarBooking[] carBookingsByUser = carBookingService.getCarBookingsByUser(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
                    CarBooking[] carBookingsFileByUser = carBookingService.getCarBookingsByUser(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
                    System.out.println(Arrays.toString(carBookingsByUser));
                    System.out.println("Get all bookings from the file");
                    System.out.println(Arrays.toString(carBookingsFileByUser));
                    break;
                case 4:
                    // View all bookings
                    CarBooking[] allBookings = carBookingService.getAllBookings();
                    System.out.println(Arrays.toString(allBookings));
                    allBookings = carBooking.getAllCarBooking();
                    System.out.println("Getting all the booking from a file");
                    System.out.println(Arrays.toString(allBookings));
                    break;
                case 5:
                    // View all the available cars
                    Car[] availableCars = carBookingService.getAvailableCars();
                    System.out.println(Arrays.toString(availableCars));
                    break;
                case 6:
                    // View all the available electric cars
                    Car[] availableElectricCars = carBookingService.getAvailableElectricCars();
                    System.out.println(Arrays.toString(availableElectricCars));
                    break;
                case 7:
                    // View all users
                    User[] allUsers = userService.getAllUsers();
                    System.out.println(Arrays.toString(allUsers));
                    break;
                case 8:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

    }

    public static void showMenu() {
        System.out.println("1 - Book Car");
        System.out.println("2 - Delete Booking");
        System.out.println("3 - View All User Booked Cars");
        System.out.println("4 - View All Bookings");
        System.out.println("5 - View Available Cars");
        System.out.println("6 - View Available Electric Cars");
        System.out.println("7 - View All Users");
        System.out.println("8 - Exit");
        System.out.print("Choose an option: ");
    }
}
