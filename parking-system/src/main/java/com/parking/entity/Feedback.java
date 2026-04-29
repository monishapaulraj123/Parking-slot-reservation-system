package com.parking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private ParkingSlot slot;

    private int rating;   // 1 to 5
    private String comment;

    // ---------- Constructors ----------
    public Feedback() {}

    public Feedback(Long id, User user, ParkingSlot slot, int rating, String comment) {
        this.id = id;
        this.user = user;
        this.slot = slot;
        this.rating = rating;
        this.comment = comment;
    }

    // ---------- Getters ----------
    public Long getId()           { return id; }
    public User getUser()         { return user; }
    public ParkingSlot getSlot()  { return slot; }
    public int getRating()        { return rating; }
    public String getComment()    { return comment; }

    // ---------- Setters ----------
    public void setId(Long id)           { this.id = id; }
    public void setUser(User user)       { this.user = user; }
    public void setSlot(ParkingSlot slot){ this.slot = slot; }
    public void setRating(int rating)    { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
}
