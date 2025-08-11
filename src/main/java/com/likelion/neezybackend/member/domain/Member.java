package com.likelion.neezybackend.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    // === 소셜 로그인 식별자(반드시 필요) ===
    @Column(name = "provider", nullable = false, length = 20)
    private String provider;          // GOOGLE | KAKAO

    @Column(name = "provider_id", nullable = false, length = 100)
    private String providerId;          // Google sub, Kakao id

    @Column(name = "member_email")
    private String email;


    @Column(name = "member_name")
    private String name;

    @Column(name = "member_picture_url", length = 512)
    private String pictureUrl;

    // 역할: 창업자/사용자/미선택(기본)
    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private Role role = Role.PENDING;  // 기본은 '선택 전'

    // 사용자가 화면에서 역할 고르면 호출
    public void chooseRole(Role role) {  // 아주 단순
        this.role = role;
    }

    @Builder
    public Member(String provider, String providerId,
                  String email, String name, String pictureUrl, Role role) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.role = (role != null ? role : Role.PENDING);
    }
}