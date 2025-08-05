package com.hei.school.client;

import com.hei.school.domain.PaymentStatus;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

@Repository
public class VolaClient {
    private final String apiKey;

    public VolaClient() {
        this.apiKey = System.getenv("VOLA_API_KEY");
        if (apiKey == null) {
            throw new IllegalStateException("VOLA_API_KEY not set in environment variables");
        }
    }

    public PaymentStatus getPaymentStatus(String transactionId) {
        try {
            String url = "https://api.vola.mg/payments/" + transactionId;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Failed to fetch payment status from Vola: " + responseCode);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(jsonBuffer.toString());
            String status = json.getString("status").toUpperCase();
            return PaymentStatus.valueOf(status);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching payment status: " + e.getMessage(), e);
        }
    }
}
