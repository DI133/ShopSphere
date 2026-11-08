package com.project.ecommerce_app.controller;

import com.project.ecommerce_app.entity.SellerStats;
import com.project.ecommerce_app.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    // get analytics details of user by userId
    @GetMapping("/{userId}")
    public ResponseEntity<SellerStats> getStats(@PathVariable Integer userId) {
        SellerStats stats = analyticsService.getSellerStats(userId);
        return ResponseEntity.ok(stats);
    }

    // create seller stats of a particular user
    @GetMapping("/create/{userId}")
    public ResponseEntity<String> calculateAndSaveSellerStats(@PathVariable Integer userId){
        analyticsService.calculateAndSaveSellerStats(userId);
        return ResponseEntity.ok("Stats created successfully");
    }
}
