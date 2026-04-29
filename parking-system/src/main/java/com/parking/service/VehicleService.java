package com.parking.service;

import com.parking.entity.Vehicle;
import com.parking.entity.User;
import com.parking.repository.VehicleRepository;
import com.parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Converted from original Vehicle class logic.
 */
@Service
public class VehicleService {

    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private UserRepository userRepository;

    public Vehicle addVehicle(Long userId, Vehicle vehicle) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        vehicle.setUser(user);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getVehiclesByUser(Long userId) {
        return vehicleRepository.findByUserId(userId);
    }

    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }
}
