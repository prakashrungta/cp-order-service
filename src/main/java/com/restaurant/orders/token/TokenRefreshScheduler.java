package com.restaurant.orders.token;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenRefreshScheduler {
    private final KeycloakTokenService keycloakTokenService;

    public TokenRefreshScheduler(KeycloakTokenService keycloakTokenService) {
        this.keycloakTokenService = keycloakTokenService;
    }

    @Scheduled(fixedRate = 15 * 10 * 1000 ) // Runs every 10 minutes
    public void refreshToken() {
        System.out.println("Scheduler called  after 10 min for Token Refresh");
        keycloakTokenService.refreshServiceAccessToken();
    }
}
