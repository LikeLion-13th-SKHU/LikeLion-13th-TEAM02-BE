package com.likelion.neezybackend.post.api.dto.request;

public record PostSaveRequestDto(
        String title,
        String contents
) {
}