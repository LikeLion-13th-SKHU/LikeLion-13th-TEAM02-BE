package com.likelion.neezybackend.post.api.dto.response;

import com.likelion.neezybackend.post.domain.Category;
import com.likelion.neezybackend.post.domain.Post;
import com.likelion.neezybackend.region.domain.Region;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostInfoResponseDto(
        Long postId,
        Long memberId,
        String writer,
        String title,
        String contents,
        String region,
        Category category,
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
                .region(post.getRegion().getRegionName())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}