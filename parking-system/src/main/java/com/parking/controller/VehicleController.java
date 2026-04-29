package com.parking.controller;

import com.parking.entity.Vehicle;
import com.parking.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    @Autowired private VehicleService vehicleService;

    // POST /api/vehicles/{userId}
    @PostMapping("/{userId}")
    public ResponseEntity<?> addVehicle(@PathVariable Long userId, @RequestBody Vehicle vehicle) {
        try {
            return ResponseEntity.ok(vehicleService.addVehicle(userId, vehicle));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/vehicles/{userId}
    @GetMapping("/{userId}")
    public List<Vehicle> getVehicles(@PathVariable Long userId) {
        return vehicleService.getVehiclesByUser(userId);
    }
}
