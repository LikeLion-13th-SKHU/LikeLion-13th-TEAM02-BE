package com.likelion.neezybackend.post.domain;

import com.likelion.neezybackend.member.domain.Member;
import com.likelion.neezybackend.post.api.dto.request.PostUpdateRequestDto;
import com.likelion.neezybackend.region.domain.Region;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    // 지역 필드 추가 (프론트에서 전달받아 저장)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    // 카테고리 필드 추가 (Enum)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Category category;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void update(PostUpdateRequestDto postUpdateRequestDto) {
        if (postUpdateRequestDto.title() != null && !postUpdateRequestDto.title().isBlank()) {
            this.title = postUpdateRequestDto.title();
        }
        if (postUpdateRequestDto.contents() != null && !postUpdateRequestDto.contents().isBlank()) {
            this.contents = postUpdateRequestDto.contents();
        }
        if (postUpdateRequestDto.category() != null) {
            this.category = postUpdateRequestDto.category();
        }
    }

    @Builder
    private Post(String title, String contents, Region region, Category category, Member member) {
        this.title = title;
        this.contents = contents;
        this.region = region;
        this.category = category;
        this.member = member;
    }
}