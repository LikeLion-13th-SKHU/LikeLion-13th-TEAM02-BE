package com.likelion.neezybackend.chat.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${proxy.upstream-url}")
    private String upstreamUrl;

    public Mono<ResponseEntity<Object>> sendMessageToUpstream(String message) {
        if (message == null || message.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of(
                    "error", "Bad Request",
                    "message", "message 필드는 필수입니다."
            )));
        }

        // 상류 요청 바디 구성
        Map<String, Object> proxyRequestBody = Map.of(
                "messages", List.of(Map.of("role", "user", "content", message)),
                "temperature", 0.7,
                "max_context", 16
        );

        // WebClient 요청
        return webClient.post()
                .uri(upstreamUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(proxyRequestBody)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(err -> Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(Map.of(
                                "error", "Bad Gateway",
                                "message", "ChatBot API 호출 실패"
                        ))));
    }
}
