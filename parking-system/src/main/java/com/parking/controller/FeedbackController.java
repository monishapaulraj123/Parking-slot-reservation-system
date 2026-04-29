package com.parking.controller;

import com.parking.entity.Feedback;
import com.parking.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired private FeedbackService feedbackService;

    // POST /api/feedback
    @PostMapping
    public ResponseEntity<?> submit(@RequestBody Map<String, String> request) {
        try {
            return ResponseEntity.ok(feedbackService.submitFeedback(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/feedback — admin view all feedback
    @GetMapping
    public List<Feedback> getAll() {
        return feedbackService.getAllFeedback();
    }
}
