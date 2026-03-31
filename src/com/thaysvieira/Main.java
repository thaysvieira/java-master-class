package com.thaysvieira;
// TODO 1. create a new branch called initial-implementation
// TODO 2. create a package with your name. i.e com.franco and move this file inside the new package
// TODO 3. implement https://amigoscode.com/learn/java-cli-build/lectures/3a83ecf3-e837-4ae5-85a8-f8ae3f60f7f5

import com.thaysvieira.booking.CarBooking;
import com.thaysvieira.booking.CarBookingDao;
import com.thaysvieira.booking.CarBookingService;
import com.thaysvieira.car.Car;
import com.thaysvieira.car.CarDao;
import com.thaysvieira.car.CarService;
import com.thaysvieira.user.User;
import com.thaysvieira.user.UserDao;
import com.thaysvieira.user.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final CarBookingDao carBookingDao = new CarBookingDao();
    private static final UserDao userDao = new UserDao();
    private static  final UserService userService = new UserService(userDao);
    private static final CarDao carDao = new CarDao();
    private static final CarService carService = new CarService(carDao);
    private static  final  CarBookingService carBookingService = new CarBookingService(carBookingDao,carService,userService);

    public static void main(String[] args) {
        System.out.println("Java Master Class");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            showMenu();
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    // Book a car
                    CarBooking booking = carBookingService.bookCar(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"), UUID.fromString("b53815cd-d7c4-4c8d-98fc-78d236bf908e"), LocalDate.of(2026, 4, 2), LocalDate.of(2026, 4, 4));
                    System.out.println(booking);
                    break;
                case 2:
                    // delete booking
                    CarBooking cancelBooking = carBookingService.cancelBooking(UUID.fromString("4f1c6a7e-8c5b-4e2a-9d13-6b9a2f4c1d80"));
                    System.out.println(cancelBooking);
                    break;
                case 3:
                    // View car booked by the user
                    CarBooking[] carBookingsByUser = carBookingService.getCarBookingsByUser(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
                    System.out.println(Arrays.toString(carBookingsByUser));
                    break;
                case 4:
                    // View all bookings
                    CarBooking[] allBookings = carBookingService.getAllBookings();
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
