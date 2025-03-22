package com.restaurant.orders.token;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.Map;

@Service
public class KeycloakTokenService {

    private static final String TOKEN_URL = "http://localhost:8080/realms/spring-boot-realm/protocol/openid-connect/token";
    private static final String CLIENT_ID = "order-service-client";
    private static final String CLIENT_SECRET = "OqT8dumRI0E8jVKgdB8LXSQjEZDYu5TN";

    private final RestTemplate restTemplate = new RestTemplate();
    private String servciceAccessToken;
    private Instant tokenExpiryTime;


    @PostConstruct
    public void init() {
    System.out.println("Init method called");
        requestNewToken();
    }


    public void requestNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=client_credentials" +
                             "&client_id=" + CLIENT_ID +
                             "&client_secret=" + CLIENT_SECRET;

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            this.servciceAccessToken = (String) body.get("access_token");
            int expiresIn = (Integer) body.get("expires_in");
            this.tokenExpiryTime = Instant.now().plusSeconds(expiresIn);
            System.out.println("expiresIn " + expiresIn + "\n" +" Token : "+servciceAccessToken);
        } else {
            throw new RuntimeException("Failed to obtain token from Keycloak");
        }
    }

    public void refreshServiceAccessToken() {
    System.out.println("Refreshing the Token in Actual ");
        if (Instant.now().isAfter(tokenExpiryTime.minusSeconds(30))) {
            requestNewToken();
        }
    }

    // ✅ Get token when making API calls
    public String getServiceAccessToken() {
        if (Instant.now().isAfter(tokenExpiryTime.minusSeconds(30))) {
            requestNewToken();
        }
        return servciceAccessToken;
    }
}
