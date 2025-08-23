package com.likelion.neezybackend.post.application;

import com.likelion.neezybackend.member.domain.repository.MemberRepository;
import com.likelion.neezybackend.post.api.dto.request.PostSaveRequestDto;
import com.likelion.neezybackend.post.api.dto.request.PostUpdateRequestDto;
import com.likelion.neezybackend.post.api.dto.response.PostInfoResponseDto;
import com.likelion.neezybackend.post.api.dto.response.PostListResponseDto;
import com.likelion.neezybackend.post.domain.Post;
import com.likelion.neezybackend.post.domain.repository.PostRepository;
import com.likelion.neezybackend.region.domain.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RegionRepository regionRepository;

    // 게시물 저장
    @Transactional
    public void postSave(Long memberId, PostSaveRequestDto dto) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        var region = regionRepository.findByRegionNameIgnoreCase(dto.regionName())
                .orElseThrow(() -> new IllegalArgumentException("해당 지역을 찾을 수 없습니다: " + dto.regionName()));


        var post = Post.builder()
                .title(dto.title())
                .contents(dto.contents())
                .region(region)       // region 추가
                .category(dto.category())      // category 추가
                .member(member)
                .build();

        postRepository.save(post);
    }

    // 특정 지역 최신순 조회
    public PostListResponseDto findAllByRegionLatest(String regionName) {
        var posts = postRepository.findAllByRegion_RegionNameOrderByCreatedAtDesc(regionName);
        var items = posts.stream().map(PostInfoResponseDto::from).toList();
        return PostListResponseDto.from(items);
    }


    // 게시물 수정 (작성자만)
    @Transactional
    public void postUpdate(Long postId, Long memberId, PostUpdateRequestDto dto) {
        var post = postRepository.findById(postId)
                .orElseThrow(IllegalArgumentException::new);

        if (!post.getMember().getMemberId().equals(memberId)) {
            throw new IllegalStateException("작성자만 수정할 수 있습니다.");
        }
        post.update(dto);
    }

    // 게시물 삭제 (작성자만)
    @Transactional
    public void postDelete(Long postId, Long memberId) {
        var post = postRepository.findById(postId)
                .orElseThrow(IllegalArgumentException::new);

        if (!post.getMember().getMemberId().equals(memberId)) {
            throw new IllegalStateException("작성자만 삭제할 수 있습니다.");
        }
        postRepository.delete(post);
    }

}

