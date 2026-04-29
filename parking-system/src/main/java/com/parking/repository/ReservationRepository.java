package com.parking.repository;

import com.parking.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

    // Report logic: sum all revenue (from original Report class)
    @Query("SELECT COALESCE(SUM(r.totalAmount), 0) FROM Reservation r")
    double getTotalRevenue();
}
