package com.likelion.neezybackend.post.api.dto.response;


import com.likelion.neezybackend.post.domain.Post;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostInfoResponseDto(
        Long postId,
        Long memberId,
        String writer,
        String title,
        String contents,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostInfoResponseDto from(Post post) {
        return PostInfoResponseDto.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .writer(post.getMember().getName())
                .title(post.getTitle())
                .contents(post.getContents())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}