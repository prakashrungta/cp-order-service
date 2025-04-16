package com.restaurant.orders.RestClient;

import com.restaurant.orders.token.KeycloakTokenService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestClient {

    @Value("${payment.service.url}")
    private String paymentServiceUrl;


    @Autowired
    private final KeycloakTokenService keycloakTokenService;


    @Autowired
    private final WebClient.Builder webClientBuilder;

    @CircuitBreaker(name = "paymentCB", fallbackMethod = "fallbackForPaymentService")
    public Mono<String> callPaymentService(Long orderId) {
        System.out.println("Calling payment service with order ID: " + orderId + " and URL: " + paymentServiceUrl);

        String accessToken = keycloakTokenService.getServiceAccessToken();

        Map<String, Object> requestBody = Map.of("amount", "44");

        String fullUrl = paymentServiceUrl + orderId;

        return webClientBuilder.build()
                .post()
                .uri(fullUrl)
                .headers(headers -> {
                    headers.setBearerAuth(accessToken);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Fallback must match return type and parameter count
    public Mono<String> fallbackForPaymentService(Long orderId, Throwable ex) {
        return Mono.just("Payment service is currently unavailable. Please try again later.");
    }
}
