package com.parking.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private ParkingSlot slot;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private double totalAmount;
    private String paymentMethod;
    private String paymentStatus;

    // ---------- Constructors ----------
    public Reservation() {
        this.paymentStatus = "PAID"; // default
    }

    public Reservation(Long id, User user, Vehicle vehicle, ParkingSlot slot,
                       LocalDate date, LocalTime startTime, LocalTime endTime,
                       double totalAmount, String paymentMethod, String paymentStatus) {
        this.id = id;
        this.user = user;
        this.vehicle = vehicle;
        this.slot = slot;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    // ---------- Getters ----------
    public Long getId()               { return id; }
    public User getUser()             { return user; }
    public Vehicle getVehicle()       { return vehicle; }
    public ParkingSlot getSlot()      { return slot; }
    public LocalDate getDate()        { return date; }
    public LocalTime getStartTime()   { return startTime; }
    public LocalTime getEndTime()     { return endTime; }
    public double getTotalAmount()    { return totalAmount; }
    public String getPaymentMethod()  { return paymentMethod; }
    public String getPaymentStatus()  { return paymentStatus; }

    // ---------- Setters ----------
    public void setId(Long id)                       { this.id = id; }
    public void setUser(User user)                   { this.user = user; }
    public void setVehicle(Vehicle vehicle)          { this.vehicle = vehicle; }
    public void setSlot(ParkingSlot slot)            { this.slot = slot; }
    public void setDate(LocalDate date)              { this.date = date; }
    public void setStartTime(LocalTime startTime)    { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime)        { this.endTime = endTime; }
    public void setTotalAmount(double totalAmount)   { this.totalAmount = totalAmount; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
