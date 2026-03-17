package com.fraud.service;

import com.fraud.model.Alert;
import com.fraud.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public List<Alert> getAllAlerts() {
        return alertRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Alert> getAlert(Long id) {
        return alertRepository.findById(id);
    }

    public Optional<Alert> updateStatus(Long id, String status) {
        return alertRepository.findById(id).map(alert -> {
            alert.setStatus(status);
            return alertRepository.save(alert);
        });
    }

    public Alert saveAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_alerts", alertRepository.count());
        stats.put("open_alerts",  alertRepository.countByStatus("OPEN"));
        stats.put("false_positives", alertRepository.countByStatus("FALSE_POSITIVE"));
        return stats;
    }
}
