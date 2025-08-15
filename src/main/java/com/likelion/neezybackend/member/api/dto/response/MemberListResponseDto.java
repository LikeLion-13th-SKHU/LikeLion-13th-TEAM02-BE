package com.likelion.neezybackend.member.api.dto.response;

import com.likelion.neezybackend.member.domain.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record MemberListResponseDto(
        List<MemberInfoResponseDto> members
) {
    // 엔티티 리스트 -> DTO 리스트로 바로 변환
    public static MemberListResponseDto from(List<MemberInfoResponseDto> members) {
        return MemberListResponseDto.builder()
                .members(members)
                .build();
    }
}
