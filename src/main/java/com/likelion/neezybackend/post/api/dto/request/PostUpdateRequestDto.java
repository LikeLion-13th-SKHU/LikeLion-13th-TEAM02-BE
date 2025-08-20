package com.likelion.neezybackend.post.api.dto.request;

public record PostUpdateRequestDto(
        String title,
        String contents
) {
}
