package com.likelion.neezybackend.post.domain.repository;

import com.likelion.neezybackend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByRegionOrderByCreatedAtDesc(String region);

    List<Post> findAllByRegion(String region);

}