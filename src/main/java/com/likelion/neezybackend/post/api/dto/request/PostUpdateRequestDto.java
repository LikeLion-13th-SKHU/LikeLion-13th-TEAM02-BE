package com.likelion.neezybackend.post.api.dto.request;

import com.likelion.neezybackend.post.domain.Category;

public record PostUpdateRequestDto(
        String title,
        String contents,
        Category category

) {
}
