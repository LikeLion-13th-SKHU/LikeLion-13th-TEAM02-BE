package com.likelion.neezybackend.member.domain.repository;

import com.likelion.neezybackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

    // 소셜 식별자로 조회 (provider + providerId)
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    // (선택) 존재 여부만 체크할 때 편리
    boolean existsByProviderAndProviderId(String provider, String providerId);
}
