package com.likelion.neezybackend.oauth.api;

import com.likelion.neezybackend.oauth.api.dto.Token;
import com.likelion.neezybackend.oauth.application.AuthLoginService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
@Tag(name = "소셜 로그인 API", description = "구글, 카카오 OAuth2 소셜 로그인")
public class AuthLoginController {

    private final AuthLoginService authLoginService;

    @Value("${oauth.google.client-id}") private String googleClientId;
    @Value("${oauth.google.redirect-uri:http://localhost:8080/login/oauth2/code/google}") private String googleRedirectUri;
    @Value("${kakao.client-id}") private String kakaoClientId;
    @Value("${kakao.redirect-uri}") private String kakaoRedirectUri;

    // ====== 로그인 URL을 JSON으로 반환 ======
    @Operation(
            summary = "소셜 로그인 URL 반환",
            description = "provider(google|kakao)를 입력하면, 소셜 로그인 화면으로 이동 가능한 URL을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"url\":\"https://accounts.google.com/o/oauth2/v2/auth?client_id=...&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Flogin%2Foauth2%2Fcode%2Fgoogle&response_type=code&scope=email%20profile\"}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "지원하지 않는 provider")
    })
    @GetMapping("/login-url/{provider}")
    public ResponseEntity<Map<String, String>> loginUrl(
            @Parameter(description = "로그인 서비스", schema = @Schema(allowableValues = {"google","kakao"}))
            @PathVariable String provider
    ) {
        String url = switch (provider.toLowerCase()) {
            case "google" -> UriComponentsBuilder
                    .fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                    .queryParam("client_id", googleClientId)
                    .queryParam("redirect_uri", googleRedirectUri)
                    .queryParam("response_type", "code")
                    .queryParam("scope", "email profile")
                    .encode()
                    .toUriString();
            case "kakao" -> UriComponentsBuilder
                    .fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                    .queryParam("client_id", kakaoClientId)
                    .queryParam("redirect_uri", kakaoRedirectUri)
                    .queryParam("response_type", "code")
                    // 이메일은 동의항목 미설정이므로 scope 제외
                    .encode()
                    .toUriString();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 provider");
        };
        return ResponseEntity.ok(Map.of("url", url));
    }

// ====== 기존: 구글 콜백 (변경 없음) ======
    @Operation(
            summary = "Google 로그인",
            description = "구글 OAuth2 인증 후 Authorization Code를 전달받아 AccessToken을 발급한다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공 - 토큰 반환",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Token.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "accessToken": "ya29.a0AfH6SMBEXAMPLE"
                                        }
                                    """)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 (code 값 누락/유효하지 않음)"),
                    @ApiResponse(responseCode = "500", description = "소셜 서버 오류 또는 내부 서버 에러")
            }
    )
    @GetMapping("/code/google")
    public ResponseEntity<Token> googleCallback(@RequestParam(name = "code") String code) {
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        Token token = authLoginService.loginOrSignUp(googleAccessToken);
        return ResponseEntity.ok(token);
    }

    // ====== 기존: 카카오 콜백 (변경 없음) ======
    @Operation(
            summary = "Kakao 로그인",
            description = "카카오 OAuth2 인증 후 Authorization Code를 전달받아 AccessToken을 발급한다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공 - 토큰 반환",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Token.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "accessToken": "kakao_access_token_example"
                                        }
                                    """)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 (code 값 누락/유효하지 않음)"),
                    @ApiResponse(responseCode = "500", description = "소셜 서버 오류 또는 내부 서버 에러")
            }
    )
    @GetMapping("/code/kakao")
    public ResponseEntity<Token> kakaoCallback(@RequestParam(name = "code") String code) {
        String kakaoAccessToken = authLoginService.getKakaoAccessToken(code);
        Token token = authLoginService.loginOrSignUpWithKakao(kakaoAccessToken);
        return ResponseEntity.ok(token);
    }

    // ====== 기존: 로그아웃 (변경 없음) ======
    @Operation(
            summary = "로그아웃",
            description = "클라이언트 단에서 저장된 AccessToken/RefreshToken을 삭제하여 로그아웃 처리한다. 서버에서는 별도의 무효화 작업을 하지 않는다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그아웃 성공",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "로그아웃 완료"))
                    )
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("로그아웃 완료");
    }
}
