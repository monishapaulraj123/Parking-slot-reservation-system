package com.parking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String vehicleNumber;
    private String type;   // CAR, BIKE, TRUCK
    private String model;
    private String color;

    // ---------- Constructors ----------
    public Vehicle() {}

    public Vehicle(Long id, User user, String vehicleNumber, String type, String model, String color) {
        this.id = id;
        this.user = user;
        this.vehicleNumber = vehicleNumber;
        this.type = type;
        this.model = model;
        this.color = color;
    }

    // ---------- Getters ----------
    public Long getId()               { return id; }
    public User getUser()             { return user; }
    public String getVehicleNumber()  { return vehicleNumber; }
    public String getType()           { return type; }
    public String getModel()          { return model; }
    public String getColor()          { return color; }

    // ---------- Setters ----------
    public void setId(Long id)                       { this.id = id; }
    public void setUser(User user)                   { this.user = user; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public void setType(String type)                 { this.type = type; }
    public void setModel(String model)               { this.model = model; }
    public void setColor(String color)               { this.color = color; }
}
