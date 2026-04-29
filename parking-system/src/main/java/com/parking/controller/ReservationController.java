package com.parking.controller;

import com.parking.entity.Reservation;
import com.parking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired private ReservationService reservationService;

    // POST /api/reservations/book
    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody Map<String, String> request) {
        try {
            Reservation r = reservationService.bookSlot(request);
            return ResponseEntity.ok(r);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/reservations/user/{userId} — booking history
    @GetMapping("/user/{userId}")
    public List<Reservation> getUserReservations(@PathVariable Long userId) {
        return reservationService.getByUser(userId);
    }

    // GET /api/reservations — admin: all reservations
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // GET /api/reservations/report — admin: revenue report
    @GetMapping("/report")
    public Map<String, Object> getReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("totalReservations", reservationService.getTotalReservations());
        report.put("totalRevenue", reservationService.getTotalRevenue());
        return report;
    }
}
