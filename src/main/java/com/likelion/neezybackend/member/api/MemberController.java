package com.likelion.neezybackend.member.api;

import com.likelion.neezybackend.member.api.dto.request.ChooseRoleRequestDto;
import com.likelion.neezybackend.member.api.dto.request.MemberUpdateRequestDto;
import com.likelion.neezybackend.member.api.dto.response.MemberInfoResponseDto;
import com.likelion.neezybackend.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;


    // 회원 id를 통해 특정 사용자 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponseDto> memberFindOne(@PathVariable("memberId") Long memberId) {
        MemberInfoResponseDto memberInfoResponseDto = memberService.memberFindOne(memberId);
        return new ResponseEntity<>(memberInfoResponseDto, HttpStatus.OK);
    }

    // 회원 id를 통한 사용자 수정
    @PatchMapping("/{memberId}")
    public ResponseEntity<String> memberUpdate(@PathVariable("memberId") Long memberId,
                                               @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        memberService.memberUpdate(memberId, memberUpdateRequestDto);
        return new ResponseEntity<>("사용자 수정", HttpStatus.OK);
    }

    // 회원 id를 통한 사용자 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> memberDelete(@PathVariable("memberId") Long memberId) {
        memberService.memberDelete(memberId);
        return new ResponseEntity<>("사용자 삭제", HttpStatus.OK);
    }

    // 역할 선택 
    @PatchMapping("/api/members/{memberId}/role")
    public ResponseEntity<String> chooseRole(@PathVariable Long memberId,
                                             @RequestBody ChooseRoleRequestDto dto) {
        memberService.memberChooseRole(memberId, dto);
        return new ResponseEntity<>("역할 선택 완료", HttpStatus.OK);
    }
}

