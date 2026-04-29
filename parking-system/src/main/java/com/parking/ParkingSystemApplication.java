package com.parking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Parking Slot Reservation System.
 * @SpringBootApplication enables auto-configuration, component scanning, etc.
 */
@SpringBootApplication
public class ParkingSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParkingSystemApplication.class, args);
    }
}
