package com.likelion.neezybackend.post.domain;

import com.likelion.neezybackend.member.domain.Member;
import com.likelion.neezybackend.post.api.dto.request.PostUpdateRequestDto;
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
    }

    @Builder
    private Post(String title, String contents, Member member) {
        this.title = title;
        this.contents = contents;
        this.member = member;
    }
}