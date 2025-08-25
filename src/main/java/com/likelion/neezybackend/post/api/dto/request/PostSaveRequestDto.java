package com.likelion.neezybackend.post.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.likelion.neezybackend.post.domain.Category;
import com.likelion.neezybackend.region.domain.Region;

public record PostSaveRequestDto(
        String title,
        String contents,
        @JsonProperty("region")
        String regionName,     // 지역 (필수 입력)
        Category category
) {
}