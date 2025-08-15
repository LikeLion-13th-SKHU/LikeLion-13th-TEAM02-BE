package com.likelion.neezybackend.oauth.api;


import com.likelion.neezybackend.oauth.api.dto.Token;
import com.likelion.neezybackend.oauth.application.AuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthLoginController {
    private final AuthLoginService authLoginService;


    @GetMapping("/code/google")
    public Token googleCallback(@RequestParam(name = "code") String code){
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        return loginOrSignup(googleAccessToken);
    }

    @GetMapping("/code/kakao")
    public Token kakaoCallback(@RequestParam String code){
        String at = authLoginService.getKakaoAccessToken(code);
        return authLoginService.loginOrSignUpWithKakao(at);
    }

    public Token loginOrSignup(String googleAccessToken){
        return authLoginService.loginOrSignUp(googleAccessToken);
    }
}