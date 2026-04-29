package com.parking.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // ADD THIS
import org.springframework.transaction.annotation.Transactional;

import com.parking.entity.ParkingSlot;
import com.parking.entity.Reservation;
import com.parking.entity.User;
import com.parking.entity.Vehicle;
import com.parking.repository.ReservationRepository;

@Service
public class ReservationService {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private UserService userService;
    @Autowired private VehicleService vehicleService;
    @Autowired private ParkingSlotService slotService;

    @Transactional // ADD THIS
    public Reservation bookSlot(Map<String, String> request) {
        Long userId    = Long.parseLong(request.get("userId"));
        Long vehicleId = Long.parseLong(request.get("vehicleId"));
        Long slotId    = Long.parseLong(request.get("slotId"));

        User user = userService.findById(userId);
        Vehicle vehicle = vehicleService.findById(vehicleId);

        // IMPORTANT: this now uses database locking
        ParkingSlot slot = slotService.findByIdForUpdate(slotId);

        if (!slot.getStatus().equals("Available")) {
            throw new RuntimeException("Slot " + slot.getSlotNumber() + " is already booked!");
        }

        java.time.LocalDate date = java.time.LocalDate.parse(request.get("date"));
        java.time.LocalTime start = java.time.LocalTime.parse(request.get("startTime"));
        java.time.LocalTime end = java.time.LocalTime.parse(request.get("endTime"));

        long minutes = Duration.between(start, end).toMinutes();
        if (minutes <= 0) {
            throw new RuntimeException("End time must be after start time!");
        }

        double hours = minutes / 60.0;
        double totalAmount =
                Math.round(hours * slot.getPricePerHour() * 100.0) / 100.0;

        String paymentMethod =
                request.getOrDefault("paymentMethod", "CASH");

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setVehicle(vehicle);
        reservation.setSlot(slot);
        reservation.setDate(date);
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        reservation.setTotalAmount(totalAmount);
        reservation.setPaymentMethod(paymentMethod);
        reservation.setPaymentStatus("PAID");

        slotService.markAsBooked(slotId);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public double getTotalRevenue() {
        return reservationRepository.getTotalRevenue();
    }

    public long getTotalReservations() {
        return reservationRepository.count();
    }
}