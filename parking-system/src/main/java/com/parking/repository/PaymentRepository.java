package com.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parking.entity.Payment;

/**
 * PaymentRepository - Handles DB operations for Payment entity.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // JpaRepository provides all basic CRUD operations we need
    // No additional custom queries required for this simple system
}
