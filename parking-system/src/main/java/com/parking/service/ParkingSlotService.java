package com.parking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parking.entity.ParkingSlot;
import com.parking.repository.ParkingSlotRepository;

@Service
public class ParkingSlotService {

    @Autowired
    private ParkingSlotRepository slotRepository;

    public ParkingSlot addSlot(ParkingSlot slot) {
        slot.setStatus("Available");
        return slotRepository.save(slot);
    }

    public List<ParkingSlot> getAllSlots() {
        return slotRepository.findAll();
    }

    public List<ParkingSlot> getAvailableSlots() {
        return slotRepository.findByStatus("Available");
    }

    public ParkingSlot findById(Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
    }

    // NEW METHOD FOR CONCURRENT BOOKING PROTECTION
    public ParkingSlot findByIdForUpdate(Long id) {
        return slotRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
    }

    public void markAsBooked(Long slotId) {
        ParkingSlot slot = findById(slotId);

        if (!slot.getStatus().equals("Available")) {
            throw new RuntimeException("Slot is already booked!");
        }

        slot.setStatus("Booked");
        slotRepository.save(slot);
    }

    public void markAsAvailable(Long slotId) {
        ParkingSlot slot = findById(slotId);
        slot.setStatus("Available");
        slotRepository.save(slot);
    }
}