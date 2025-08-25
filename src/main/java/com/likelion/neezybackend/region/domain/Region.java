package com.likelion.neezybackend.region.domain;

import com.likelion.neezybackend.post.domain.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regions")
@RequiredArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionId;

    private String regionName; // 지역 이름, 예: 서울, 부산 등

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>(); // 지역에 속한 게시글들

    @Builder
    public Region(String regionName) {
        this.regionName = regionName;
    }

}
