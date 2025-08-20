package com.likelion.neezybackend.post.api;

import com.likelion.neezybackend.post.api.dto.request.PostSaveRequestDto;
import com.likelion.neezybackend.post.api.dto.request.PostUpdateRequestDto;
import com.likelion.neezybackend.post.api.dto.response.PostListResponseDto;
import com.likelion.neezybackend.post.application.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "게시글 API", description = "게시글 작성, 조회, 수정, 삭제 관련 기능")
public class PostController {
    private final PostService postService;


    // 게시글 조회 (전체 최신순)
    @Operation(
            summary = "게시글 전체 조회",
            description = "등록되어 있는 모든 게시글 정보를 최신순으로 조회한다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PostListResponseDto.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "posts": [
                                            {
                                              "postId": 1,

        return ResponseEntity.ok(body);
    }

    // 게시물 저장
    @Operation(
            summary = "게시글 저장",
            description = "회원 ID를 기준으로 게시글을 저장한다",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "게시글 저장 요청 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostSaveRequestDto.class),
                            examples = @ExampleObject(value = """
                                {
                                  "title": "새 글 제목",
                                }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "게시물 저장 성공",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "게시물 저장")))
            }
    )
    @PostMapping("/{memberId}")
    public ResponseEntity<String> postSave(@PathVariable Long memberId,
                                           @RequestBody PostSaveRequestDto dto) {
        postService.postSave(memberId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("게시물 저장");
    }

    // 게시물 수정 (작성자만)
    @Operation(
            summary = "게시글 수정",
            description = "회원 ID를 기준으로 게시글을 작성한 작성자가 본인 게시글을 수정한다",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "게시글 수정 요청 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostUpdateRequestDto.class),
                            examples = @ExampleObject(value = """
                                {
                                  "title": "수정된 제목",

                                }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 수정 성공",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "게시물 수정")))
            }
    )
    @PatchMapping("/{memberId}/{postId}")
    public ResponseEntity<String> postUpdate(@PathVariable Long memberId,
                                             @PathVariable Long postId,
                                             @RequestBody PostUpdateRequestDto dto) {
        postService.postUpdate(postId, memberId, dto);
        return ResponseEntity.ok("게시물 수정");
    }

    // 게시물 삭제 (작성자만)
    @Operation(
            summary = "게시글 삭제",
            description = "회원 ID를 기준으로 게시글을 작성한 작성자가 본인 게시글을 삭제한다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 삭제 성공",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "게시물 삭제")))
            }
    )
    @DeleteMapping("/{memberId}/{postId}")
    public ResponseEntity<String> postDelete(@PathVariable Long memberId,
                                             @PathVariable Long postId) {
        postService.postDelete(postId, memberId);
        return ResponseEntity.ok("게시물 삭제");
    }
}
