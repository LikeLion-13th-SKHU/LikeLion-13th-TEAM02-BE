package com.likelion.neezybackend.post.domain.repository;

import com.likelion.neezybackend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByRegion_RegionNameOrderByCreatedAtDesc(String regionName);

    List<Post> findAllByRegion_RegionName(String regionName);

}