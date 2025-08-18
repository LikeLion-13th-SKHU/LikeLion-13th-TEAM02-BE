package com.likelion.neezybackend.oauth.api;

import com.likelion.neezybackend.oauth.api.dto.Token;
import com.likelion.neezybackend.oauth.application.AuthLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
@Tag(name = "소셜 로그인 API", description = "구글, 카카오 OAuth2 소셜 로그인")
public class AuthLoginController {

    private final AuthLoginService authLoginService;

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
                                          "accessToken": "ya29.a0AfH6SMBEXAMPLE",
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
                                          "accessToken": "kakao_access_token_example",
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



    @Operation(
            summary = "로그아웃",
            description = "클라이언트 단에서 저장된 AccessToken/RefreshToken을 삭제하여 로그아웃 처리한다. " +
                    "서버에서는 별도의 무효화 작업을 하지 않는다.",
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
