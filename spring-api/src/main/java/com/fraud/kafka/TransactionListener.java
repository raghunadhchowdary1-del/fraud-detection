package com.fraud.kafka;

import com.fraud.model.Alert;
import com.fraud.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionListener {

    @Autowired
    private AlertService alertService;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @KafkaListener(topics = "transactions", groupId = "fraud-group")
    public void onTransaction(String message) {
        try {
            JSONObject txn = new JSONObject(message);
            Map<String, Object> features = new HashMap<>();
            features.put("amount",      txn.optDouble("amount", 0));
            features.put("hour",        txn.optInt("hour", 12));
            features.put("geo_risk",    txn.optInt("geo_risk", 0));
            features.put("high_amount", txn.optDouble("amount", 0) > 1000 ? 1 : 0);
            features.put("night_txn",   txn.optInt("hour", 12) < 6 ? 1 : 0);

            @SuppressWarnings("unchecked")
            Map<String, Object> result = restTemplate.postForObject(
                mlServiceUrl + "/predict", features, Map.class);

            if (result != null && Boolean.TRUE.equals(result.get("is_fraud"))) {
                Alert alert = new Alert();
                alert.setTxnId(txn.optString("txn_id", "unknown"));
                alert.setUserId(txn.optString("user_id", "unknown"));
                alert.setFraudScore(((Number) result.get("score")).doubleValue());
                alert.setAmount(txn.optDouble("amount", 0));
                alert.setConfidence((String) result.get("confidence"));
                alertService.saveAlert(alert);
                System.out.println("FRAUD ALERT saved: txn=" + alert.getTxnId());
            }
        } catch (Exception e) {
            System.err.println("Error processing transaction: " + e.getMessage());
        }
    }
}