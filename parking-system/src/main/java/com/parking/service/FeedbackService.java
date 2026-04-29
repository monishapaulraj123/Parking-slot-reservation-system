package com.parking.service;

import com.parking.entity.*;
import com.parking.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * Converted from original Feedback class.
 */
@Service
public class FeedbackService {

    @Autowired private FeedbackRepository feedbackRepository;
    @Autowired private UserService userService;
    @Autowired private ParkingSlotService slotService;

    public Feedback submitFeedback(Map<String, String> request) {
        Long userId = Long.parseLong(request.get("userId"));
        Long slotId = Long.parseLong(request.get("slotId"));
        int rating  = Integer.parseInt(request.get("rating"));

        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Feedback feedback = new Feedback();
        feedback.setUser(userService.findById(userId));
        feedback.setSlot(slotService.findById(slotId));
        feedback.setRating(rating);
        feedback.setComment(request.getOrDefault("comment", ""));

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
}
