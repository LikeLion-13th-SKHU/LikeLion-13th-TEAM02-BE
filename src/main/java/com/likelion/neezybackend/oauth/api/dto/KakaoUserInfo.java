package com.likelion.neezybackend.oauth.api.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class KakaoUserInfo {
    private Long id;

    @SerializedName("kakao_account")
    private KakaoAccount kakaoAccount;

    @Data
    public static class KakaoAccount {
        private String email;   // 동의 안 하면 null
        private Profile profile;
    }

    @Data
    public static class Profile {
        private String nickname;
        @SerializedName("profile_image_url")
        private String profileImageUrl;
    }
}
