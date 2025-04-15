package com.restaurant.orders.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.Map;

@Service
public class KeycloakTokenService {

    @Value("${spring.security.oauth2.resourceserver.jwt.token-uri:}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private String serviceAccessToken;
    private Instant tokenExpiryTime;


    @PostConstruct
    public void init() {

    System.out.println("Init method called");
        System.out.println(tokenUrl + " " + clientId + " " + clientSecret);
        requestNewToken();
    }


    public void requestNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=client_credentials" +
                             "&client_id=" + clientId +
                             "&client_secret=" + clientSecret;

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            this.serviceAccessToken = (String) body.get("access_token");
            int expiresIn = (Integer) body.get("expires_in");
            this.tokenExpiryTime = Instant.now().plusSeconds(expiresIn);
            System.out.println("expiresIn " + expiresIn + "\n" +" Token : "+serviceAccessToken);
        } else {
            throw new RuntimeException("Failed to obtain token from Keycloak");
        }
    }

    public void refreshServiceAccessToken() {
        if (Instant.now().isAfter(tokenExpiryTime.minusSeconds(30))) {
            System.out.println("Refreshing the Token in Actual ");
            requestNewToken();
        }
    }

    // ✅ Get token when making API calls
    public String getServiceAccessToken() {
        if (Instant.now().isAfter(tokenExpiryTime.minusSeconds(30))) {
            requestNewToken();
        }
        return serviceAccessToken;
    }
}
