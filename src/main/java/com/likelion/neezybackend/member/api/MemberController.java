package com.likelion.neezybackend.member.api;

import com.likelion.neezybackend.member.api.dto.request.ChooseRoleRequestDto;
import com.likelion.neezybackend.member.api.dto.request.MemberUpdateRequestDto;
import com.likelion.neezybackend.member.api.dto.response.MemberInfoResponseDto;
import com.likelion.neezybackend.member.application.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "멤버 API", description = "회원 조회, 수정, 삭제, 역할 설정")
public class MemberController {
    private final MemberService memberService;


    // 회원 id를 통해 특정 사용자 조회
    @Operation(summary = "회원 단건 조회", description = "memberId로 특정 회원 정보를 가져온다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberInfoResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "memberId": 1,
                                      "nickname": "김멋사",
                                      "role": "FOUNDER"
                                    }
                                    """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 회원 없음")
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponseDto> memberFindOne(@PathVariable("memberId") Long memberId) {
        MemberInfoResponseDto memberInfoResponseDto = memberService.memberFindOne(memberId);
        return new ResponseEntity<>(memberInfoResponseDto, HttpStatus.OK);
    }



    // 회원 id를 통한 사용자 수정
    @Operation(summary = "회원 정보 수정", description = "memberId로 마이페이지의 회원 정보를 수정한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberInfoResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "memberId": 1,
                                      "nickname": "새닉네임",
                                      "role": "FOUNDER"
                                    }
                                    """
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "해당 회원 없음")
    })
    @PatchMapping("/{memberId}")
    public ResponseEntity<String> memberUpdate(@PathVariable("memberId") Long memberId,
                                               @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        memberService.memberUpdate(memberId, memberUpdateRequestDto);
        return new ResponseEntity<>("사용자 수정", HttpStatus.OK);
    }



    // 회원 id를 통한 사용자 삭제
    @Operation(summary = "회원 삭제", description = "memberId로 회원을 삭제한다")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원 없음")
    })
    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> memberDelete(@PathVariable("memberId") Long memberId) {
        memberService.memberDelete(memberId);
        return new ResponseEntity<>("사용자 삭제", HttpStatus.OK);
    }




    // 역할 선택
    @Operation(summary = "역할 선택", description = "회원이 최초 로그인 시 역할(창업자/일반 사용자)을 선택한다. " +
            "이미 선택된 경우 다시 변경 불가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "역할 선택 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (invalid role / 이미 선택된 경우)"),
            @ApiResponse(responseCode = "404", description = "해당 회원 없음")
    })    @PatchMapping("/{memberId}/role")
    public ResponseEntity<String> chooseRole(@PathVariable Long memberId,
                                             @RequestBody ChooseRoleRequestDto dto) {
        memberService.memberChooseRole(memberId, dto);
        return new ResponseEntity<>("역할 선택 완료", HttpStatus.OK);
    }
}

