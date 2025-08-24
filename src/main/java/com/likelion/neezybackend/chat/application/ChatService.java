// src/main/java/com/likelion/neezybackend/chat/application/ChatService.java
package com.likelion.neezybackend.chat.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient; // ← 이걸로!
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final WebClient chatWebClient;

    @Value("${proxy.upstream-url}")
    private String upstreamUrl;

    public Mono<ResponseEntity<String>> sendMessageToUpstream(String message) {
        if (message == null || message.isBlank()) {
            return Mono.just(
                    ResponseEntity.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{\"error\":\"Bad Request\",\"message\":\"message 필드는 필수입니다.\"}")
            );
        }

        Map<String, Object> proxyRequestBody = Map.of(
                "messages", List.of(Map.of("role", "user", "content", message)),
                "temperature", 0.7,
                "max_context", 16
        );

        return chatWebClient.post()
                .uri(upstreamUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(proxyRequestBody)
                .exchangeToMono(clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> {
                                    HttpStatus status = (HttpStatus) clientResponse.statusCode();
                                    HttpHeaders headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                    if (status.is2xxSuccessful()) {
                                        return new ResponseEntity<>(body, headers, HttpStatus.OK);
                                    } else {
                                        String errBody = body.isBlank()
                                                ? "{\"error\":\"Upstream Error\",\"status\":" + status.value() + "}"
                                                : body;
                                        return new ResponseEntity<>(errBody, headers, status);
                                    }
                                })
                )
                .timeout(java.time.Duration.ofSeconds(300))
                .onErrorResume(err -> Mono.just(
                        ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("{\"error\":\"Timeout\",\"message\":\"ChatBot API 응답 지연\"}")
                ));
    }

}
