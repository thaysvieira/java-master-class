package com.thaysvieira;
// TODO 1. create a new branch called initial-implementation
// TODO 2. create a package with your name. i.e com.franco and move this file inside the new package
// TODO 3. implement https://amigoscode.com/learn/java-cli-build/lectures/3a83ecf3-e837-4ae5-85a8-f8ae3f60f7f5

import com.thaysvieira.booking.*;
import com.thaysvieira.car.*;
import com.thaysvieira.user.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    static void main(String[] args) {

        CarBookingDao carBookingDaoFile = new CarBookingFileDataAccessService("bookings.dat");
        CarBookingDao carBooking = new CarBookingArrayDataAccessService();
        CarBookingDao carBookingFakerDao = new CarBookingFakerDataService();
        CarDao carDao = new CarArrayDataAccessService();
        CarDao carDaoFaker = new CarFakerDataAccessService();
        CarService carServiceFaker = new CarService(carDaoFaker);
        CarService carService = new CarService(carDao);
        UserDao userDao = new UserArrayDataAccessService();
        UserDao fakerUserDao = new UserFakerDataAccessService();
        UserService userService = new UserService(userDao);
        UserService userServiceFaker = new UserService(fakerUserDao);

        CarBookingService carBookingService = new CarBookingService(carBooking, carService, userService);
        CarBookingService carBookingServiceFile = new CarBookingService(carBookingDaoFile, carService, userService);
        CarBookingService carBookingServiceFaker = new CarBookingService(carBookingFakerDao,carServiceFaker,userServiceFaker);


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
                    System.out.println("Saving using Faker");
                    CarBooking fakerBooking = carBookingServiceFaker.bookCar(UUID.fromString("9b4b3c7f-1d3e-4c82-a3f9-5d6e2a8f71b4"), UUID.fromString("3d8f1a7b-5c42-4d9e-91f7-2b6c8e4a1f93"), LocalDate.now(), LocalDate.of(2026, 8, 4));
                    System.out.println(fakerBooking);
                    System.out.println("Saving using file");
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
                    List<CarBooking> carBookingsByUser = carBookingService.getCarBookingsByUser(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
                    List<CarBooking> carBookingsFileByUser = carBookingService.getCarBookingsByUser(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
                    List<CarBooking> carBookingsByUserFaker = carBookingServiceFaker.getCarBookingsByUser(UUID.fromString("9b4b3c7f-1d3e-4c82-a3f9-5d6e2a8f71b4"));

                    System.out.println(carBookingsByUser);
                    System.out.println("Get all bookings from the file");
                    System.out.println(carBookingsFileByUser);
                    System.out.println("Get all bookings from Faker");
                    System.out.println(carBookingsByUserFaker);
                    break;
                case 4:
                    // View all bookings
                    List<CarBooking> allBookings = carBookingService.getAllBookings();
                    System.out.println(allBookings);
                    allBookings = carBooking.getAllCarBooking();
                    System.out.println("Getting all the booking from a file");
                    System.out.println(allBookings);
                    List<CarBooking> allBookingsFaker = carBookingService.getAllBookings();
                    System.out.println("Getting all the booking from Faker");
                    System.out.println(allBookingsFaker);

                    break;
                case 5:
                    // View all the available cars
                    List<Car> availableCars = carBookingService.getAvailableCars();
                    System.out.println(availableCars);
                    List<Car> availableFakerCars = carBookingServiceFaker.getAvailableCars();
                    System.out.println("Getting available cars from Faker");
                    System.out.println(availableFakerCars);
                    break;
                case 6:
                    // View all the available electric cars
                    List<Car> availableElectricCars = carBookingService.getAvailableElectricCars();
                    System.out.println(availableElectricCars);

                    List<Car> availableFakerElectricCars = carBookingServiceFaker.getAvailableElectricCars();
                    System.out.println("View all the available faker electric cars");
                    System.out.println(availableFakerElectricCars);
                    break;
                case 7:
                    // View all users
                    List<User> allUsers = userService.getAllUsers();
                    System.out.println(allUsers);
                    List<User> allFakerUsers = userServiceFaker.getAllUsers();
                    System.out.println("All faker available users");
                    System.out.println(allFakerUsers);
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
