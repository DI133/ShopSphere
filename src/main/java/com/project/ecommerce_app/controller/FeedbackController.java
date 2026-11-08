package com.project.ecommerce_app.controller;

import com.project.ecommerce_app.entity.Feedback;
import com.project.ecommerce_app.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // add feedback for a product
    @PostMapping("/submit")
    public ResponseEntity<Feedback> submitFeedback(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam String message) {
        Feedback feedback = feedbackService.submitFeedback(userId, productId, message);
        return ResponseEntity.ok(feedback);
    }

    // delete feedback (not used)
    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Integer feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.noContent().build();
    }

    // get feedback of product by it's id
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Feedback>> getFeedbacksForProduct(@PathVariable Integer productId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksForProduct(productId);
        return ResponseEntity.ok(feedbacks);
    }
}
