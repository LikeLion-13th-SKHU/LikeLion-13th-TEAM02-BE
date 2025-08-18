package com.likelion.neezybackend.member.api.dto.response;

import com.likelion.neezybackend.member.domain.Member;
import lombok.Builder;

@Builder
public record MemberInfoResponseDto(
        Long id,
        String name,
        String email,
        Integer age,       // null 가능
        String gender,     // enum -> 문자열로 노출(프론트 호환성↑)
        String pictureUrl,
        String role        // enum -> 문자열
) {
    public static MemberInfoResponseDto from(Member member) {
        return MemberInfoResponseDto.builder()
                .id(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .age(member.getAge())
                .gender(member.getGender() != null ? member.getGender().name() : "UNKNOWN")
                .pictureUrl(member.getPictureUrl())
                .role(member.getRole() != null ? member.getRole().name() : null)
                .build();
    }
}
