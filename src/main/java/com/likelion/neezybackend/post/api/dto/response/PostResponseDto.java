package com.likelion.neezybackend.post.api.dto.response;

import com.likelion.neezybackend.post.domain.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;

    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContents();
    }
}
