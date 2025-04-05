package com.restaurant.orders.RestClient;

import com.restaurant.orders.token.KeycloakTokenService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RestClient {
    private final KeycloakTokenService keycloakTokenService;
    private final RestTemplate restTemplate = new RestTemplate();

    public RestClient(KeycloakTokenService keycloakTokenService) {
        this.keycloakTokenService = keycloakTokenService;
    }


    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallbackForPaymentService")
    public String callPaymentService( Long orderId) {
        String accessToken = keycloakTokenService.getServiceAccessToken(); // Get token

       ; // Replace with actual order ID
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", "44");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Add token to request
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String,Object>> request = new HttpEntity<>(requestBody, headers);
        String payMentServiceUrl = "http://localhost:8082/api/v1/payments/" + orderId ; // Replace with actual URL

        ResponseEntity<String> response = restTemplate.exchange(payMentServiceUrl, HttpMethod.POST, request, String.class);

        return response.getBody(); // Response from S2
    }

    public String fallbackForPaymentService(Long orderId ,Exception ex) {
        return "Payment service is currently unavailable. Please try later.";
    }
}
