package com.fraud.controller;

import com.fraud.model.Alert;
import com.fraud.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping("/alerts")
    public List<Alert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/alerts/{id}")
    public ResponseEntity<Alert> getAlert(@PathVariable Long id) {
        return alertService.getAlert(id)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/alerts/review/{id}")
    public ResponseEntity<Alert> reviewAlert(@PathVariable Long id,
                                              @RequestParam String status) {
        return alertService.updateStatus(id, status)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(alertService.getStats());
    }
}
