package com.likelion.neezybackend.ranking.application;

import com.likelion.neezybackend.post.domain.repository.PostRepository;

import com.likelion.neezybackend.ranking.api.dto.RankingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

    private final PostRepository postRepository;

    public List<RankingResponseDto> getRankingByRegion(String region) {
        var posts = postRepository.findAllByRegion_RegionName(region);

        // 카테고리별 count 집계
        Map<String, Long> counts = posts.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategory().name(), // 카테고리 Enum의 name() 사용
                        Collectors.counting()
                ));

        // count 기준 내림차순 정렬
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new RankingResponseDto(entry.getKey(), entry.getValue()))
                .toList();
    }
}
