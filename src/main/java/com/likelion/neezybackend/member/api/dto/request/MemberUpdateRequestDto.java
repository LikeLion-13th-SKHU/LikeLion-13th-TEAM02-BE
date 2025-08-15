package com.likelion.neezybackend.member.api.dto.request;

import com.likelion.neezybackend.member.domain.Gender;
import com.likelion.neezybackend.member.domain.Role;
import jakarta.validation.constraints.*;

public record MemberUpdateRequestDto(
        @Size(min = 1, max = 80) String name,
        @Email @Size(max = 255) String email,
        @Min(0) @Max(120) Integer age,
        Gender gender,
        @Size(max = 512) String pictureUrl,
        Role role
) {
}
