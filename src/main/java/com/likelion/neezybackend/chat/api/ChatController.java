package com.likelion.neezybackend.chat.api;

import com.likelion.neezybackend.chat.application.ChatService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Chat 프록시 API", description = "HackChatBot API")
public class ChatController {

    private final ChatService chatService;

    @Operation(
            summary = "단일 메시지를 기반으로 HackChatBot에 요청",
            description = "프론트에서 단일 'message'를 받아 적절한 형태로 변환 후 HackChatBot에 전달합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "요청 예시",
                                    summary = "단일 메시지 요청",
                                    value = "{\n  \"message\": \"구로구 온수동의 상권분석을 해줘\"\n}"
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "응답 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "응답 예시",
                                    value = "{\n  \"reply\": \"구로구 온수동의 상권분석 결과입니다...\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Upstream API 호출 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "502 예시",
                                    value = "{\n  \"error\": \"Bad Gateway\",\n  \"message\": \"Upstream API 호출 실패\"\n}"
                            )
                    )
            )
    })
    @PostMapping(value = "/chat",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        return chatService.sendMessageToUpstream(message);
    }
}
