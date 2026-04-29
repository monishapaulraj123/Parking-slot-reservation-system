package com.parking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.parking.entity.ParkingSlot;

import jakarta.persistence.LockModeType;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    // Find all available slots
    List<ParkingSlot> findByStatus(String status);

    // Find slots by vehicle type
    List<ParkingSlot> findBySlotType(String slotType);

    // Prevent multiple customers from booking the same slot simultaneously
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ParkingSlot p WHERE p.id = :id")
    Optional<ParkingSlot> findByIdForUpdate(@Param("id") Long id);
}