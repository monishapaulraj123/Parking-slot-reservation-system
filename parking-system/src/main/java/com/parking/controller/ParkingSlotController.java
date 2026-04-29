package com.parking.controller;

import com.parking.entity.ParkingSlot;
import com.parking.service.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "*")
public class ParkingSlotController {

    @Autowired private ParkingSlotService slotService;

    // Admin: POST /api/slots
    @PostMapping
    public ResponseEntity<?> addSlot(@RequestBody ParkingSlot slot) {
        try {
            return ResponseEntity.ok(slotService.addSlot(slot));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/slots — all slots (admin view)
    @GetMapping
    public List<ParkingSlot> getAllSlots() {
        return slotService.getAllSlots();
    }

    // GET /api/slots/available — only available (user booking view)
    @GetMapping("/available")
    public List<ParkingSlot> getAvailable() {
        return slotService.getAvailableSlots();
    }
}
