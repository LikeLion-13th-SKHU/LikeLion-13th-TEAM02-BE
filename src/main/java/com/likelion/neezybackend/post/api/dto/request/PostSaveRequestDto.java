package com.likelion.neezybackend.post.api.dto.request;

import com.likelion.neezybackend.post.domain.Category;

public record PostSaveRequestDto(
        String title,
        String contents,
        String region,     // 지역 (필수 입력)
        Category category
) {
}