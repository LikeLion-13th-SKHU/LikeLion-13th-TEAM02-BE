package com.likelion.neezybackend.oauth.application;

import com.google.gson.Gson;
import com.likelion.neezybackend.global.jwt.JwtTokenProvider;
import com.likelion.neezybackend.member.domain.Member;
import com.likelion.neezybackend.member.domain.Role;
import com.likelion.neezybackend.member.domain.repository.MemberRepository;
import com.likelion.neezybackend.oauth.api.dto.Token;
import com.likelion.neezybackend.oauth.api.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.likelion.neezybackend.oauth.api.dto.KakaoUserInfo;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthLoginService {

//    // 컨트롤러에서 받은 authorization code와 소셜 로그인 id를 받아 콘솔에 출력
//    public void socialLogin(String code, String registrationId){
//        System.out.println("code = " + code);
//        System.out.println("registrationId = " + registrationId);
//    }

    @Value("${client-id}") // import 시 lombok으로 하면 안됨
    private String GOOGLE_CLIENT_ID;

    @Value("${client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    // 구글 인증 코드를 엑세스 토큰으로 교환하는 API 주소
    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    // OAuth 인증 후 구글이 리디렉션할 URI
    private final String GOOGLE_REDIRECT_URI = "http://localhost:5173/login/oauth2/code/google";

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String getGoogleAccessToken(String code) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        org.springframework.util.MultiValueMap<String,String> body = new org.springframework.util.LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", GOOGLE_CLIENT_ID);
        body.add("client_secret", GOOGLE_CLIENT_SECRET);
        body.add("redirect_uri", GOOGLE_REDIRECT_URI); // 기존 값 그대로 사용
        body.add("grant_type", "authorization_code");  // scope 파라미터는 필요 없음

        ResponseEntity<String> res =
                rt.postForEntity(GOOGLE_TOKEN_URL, new HttpEntity<>(body, headers), String.class);

        if(res.getStatusCode().is2xxSuccessful()) {
            return new Gson()
                    .fromJson(res.getBody(), Token.class)
                    .getAccessToken();
        }
        throw new RuntimeException("구글 엑세스 토큰을 가져오는데 실패했습니다.");
    }

    public Token loginOrSignUp(String googleAccessToken){
        UserInfo userInfo = getUserInfo(googleAccessToken);

        if(!userInfo.getVerifiedEmail()){
            throw new RuntimeException("이메일 인증이 되지 않은 유저입니다.");
        }

        Member member = memberRepository.findByEmail(userInfo.getEmail()).orElseGet(() ->
                memberRepository.save(
                        Member.builder()
                                .provider("google")                   // 필수
                                .providerId(userInfo.getId())         // v2 userinfo는 id, OIDC면 sub
                                .email(userInfo.getEmail())
                                .name(userInfo.getName())
                                .pictureUrl(userInfo.getPictureUrl())
                                .role(Role.PENDING)                   // ROLE_USER 아님
                                .build()
                )
        );

        String jwt = jwtTokenProvider.generateToken(member);
        return new Token(jwt, member.getMemberId());
    }

    // --- Kakao 설정 (클래스 멤버로 추가) ---
    @Value("${kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client-secret:}")
    private String KAKAO_CLIENT_SECRET; // 시크릿 미사용이면 빈 문자열도 OK

    @Value("${kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    // 인가코드 -> 카카오 액세스 토큰 (폼 인코딩 필수)
    public String getKakaoAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        org.springframework.util.MultiValueMap<String,String> body = new org.springframework.util.LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", KAKAO_REDIRECT_URI);
        body.add("code", code);
        if (KAKAO_CLIENT_SECRET != null && !KAKAO_CLIENT_SECRET.isBlank()) {
            body.add("client_secret", KAKAO_CLIENT_SECRET);
        }

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> res =
                rt.postForEntity(url, new HttpEntity<>(body, headers), String.class);

        if (res.getStatusCode().is2xxSuccessful()) {
            return new Gson().fromJson(res.getBody(), Token.class).getAccessToken();
        }
        throw new RuntimeException("카카오 액세스 토큰 발급 실패");
    }

    // 액세스 토큰 -> 카카오 유저 정보
    public KakaoUserInfo getKakaoUserInfo(String accessToken){
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> res =
                rt.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        if (res.getStatusCode().is2xxSuccessful()) {
            return new Gson().fromJson(res.getBody(), com.likelion.neezybackend.oauth.api.dto.KakaoUserInfo.class);
        }
        throw new RuntimeException("카카오 사용자 정보 조회 실패");
    }

    // 회원 처리 + JWT 발급
    public Token loginOrSignUpWithKakao(String accessToken){
        var u = getKakaoUserInfo(accessToken);

        String provider   = "kakao";
        String providerId = String.valueOf(u.getId());
        String email = (u.getKakaoAccount() != null) ? u.getKakaoAccount().getEmail() : null;
        String name  = (u.getKakaoAccount() != null && u.getKakaoAccount().getProfile() != null)
                ? u.getKakaoAccount().getProfile().getNickname() : null;
        String pic   = (u.getKakaoAccount() != null && u.getKakaoAccount().getProfile() != null)
                ? u.getKakaoAccount().getProfile().getProfileImageUrl() : null;

        // 이메일은 동의 안 할 수 있으니 provider+providerId로 식별
        Member member = memberRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .provider(provider).providerId(providerId)
                                .email(email).name(name).pictureUrl(pic)
                                .role(Role.PENDING)   // 역할 선택 전
                                .build()
                ));

        String jwt = jwtTokenProvider.generateToken(member);
        return new Token(jwt, member.getMemberId());
    }


    public UserInfo getUserInfo(String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            String json = responseEntity.getBody();
            Gson gson = new Gson();
            return gson.fromJson(json, UserInfo.class);
        }

        throw new RuntimeException("유저 정보를 가져오는데 실패했습니다.");
    }

    public void logout() {

        }
    }