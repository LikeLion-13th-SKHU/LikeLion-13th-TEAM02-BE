package com.likelion.neezybackend.member.application;

import com.likelion.neezybackend.member.api.dto.request.ChooseRoleRequestDto;
import com.likelion.neezybackend.member.api.dto.request.MemberUpdateRequestDto;
import com.likelion.neezybackend.member.api.dto.response.MemberInfoResponseDto;
import com.likelion.neezybackend.member.domain.Member;
import com.likelion.neezybackend.member.domain.Role;
import com.likelion.neezybackend.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    // 내 정보 조회
    public MemberInfoResponseDto memberFindOne(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(IllegalArgumentException::new);
        return MemberInfoResponseDto.from(member);
    }

    // 사용자 정보 수정
    @Transactional
    public void memberUpdate(Long memberId,
                             MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        member.update(memberUpdateRequestDto);
    }

    // 사용자 정보 삭제
    @Transactional
    public void memberDelete(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        memberRepository.delete(member);
    }

    // 온보딩: PENDING일 때 1회만 역할 선택
    @Transactional
    public void memberChooseRole(Long memberId, ChooseRoleRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        if (dto.role() == null || dto.role() == Role.PENDING) {
            throw new IllegalArgumentException("invalid role");
        }
        if (member.getRole() != Role.PENDING) {
            throw new IllegalStateException("role already chosen");
        }

        member.chooseRole(dto.role()); // 엔티티 메서드
    }
}