package com.parking;

import com.parking.entity.*;
import com.parking.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds initial data on startup:
 * - Admin account
 * - Sample parking slots
 * Runs once when the application starts.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepo, ParkingSlotRepository slotRepo) {
        return args -> {

            // ---- Create Admin Account ----
            if (!userRepo.existsByEmail("admin@park.com")) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@park.com");
                admin.setPassword("admin123");
                admin.setPhone("9999999999");
                admin.setRole("ADMIN");
                userRepo.save(admin);
                System.out.println("✅ Admin account created: admin@park.com / admin123");
            }

            // ---- Create Sample Parking Slots ----
            if (slotRepo.count() == 0) {
                String[][] slots = {
                    {"A1","CAR","50"},  {"A2","CAR","50"},  {"A3","CAR","50"},
                    {"A4","CAR","60"},  {"B1","CAR","60"},  {"B2","CAR","60"},
                    {"C1","BIKE","20"}, {"C2","BIKE","20"}, {"C3","BIKE","20"},
                    {"D1","TRUCK","100"},{"D2","TRUCK","100"}
                };
                for (String[] s : slots) {
                    ParkingSlot slot = new ParkingSlot();
                    slot.setSlotNumber(s[0]);
                    slot.setSlotType(s[1]);
                    slot.setPricePerHour(Double.parseDouble(s[2]));
                    slot.setStatus("Available");
                    slotRepo.save(slot);
                }
                System.out.println("✅ Sample parking slots created (11 slots)");
            }
        };
    }
}
