package com.parking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "parking_slots")
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String slotNumber;

    private String slotType;  // CAR, BIKE, TRUCK

    private String status;    // "Available" or "Booked"

    private double pricePerHour;

    // ---------- Constructors ----------
    public ParkingSlot() {
        this.status = "Available"; // default
    }

    public ParkingSlot(Long id, String slotNumber, String slotType, String status, double pricePerHour) {
        this.id = id;
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.status = status;
        this.pricePerHour = pricePerHour;
    }

    // ---------- Getters ----------
    public Long getId()             { return id; }
    public String getSlotNumber()   { return slotNumber; }
    public String getSlotType()     { return slotType; }
    public String getStatus()       { return status; }
    public double getPricePerHour() { return pricePerHour; }

    // ---------- Setters ----------
    public void setId(Long id)                   { this.id = id; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public void setSlotType(String slotType)     { this.slotType = slotType; }
    public void setStatus(String status)         { this.status = status; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }
}
