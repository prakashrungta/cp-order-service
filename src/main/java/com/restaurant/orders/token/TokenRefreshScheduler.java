package com.restaurant.orders.token;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenRefreshScheduler {
    private final KeycloakTokenService keycloakTokenService;

    public TokenRefreshScheduler(KeycloakTokenService keycloakTokenService) {
        this.keycloakTokenService = keycloakTokenService;
    }

    @Scheduled(fixedRate = 1 * 60 * 1000 ) // Runs every 20 minutes
    public void refreshToken() {
        System.out.println("Scheduler called  after 20 min for Token Refresh");
        keycloakTokenService.refreshServiceAccessToken();
    }
}
