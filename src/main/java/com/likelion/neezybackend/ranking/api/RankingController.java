package com.likelion.neezybackend.ranking.api;

import com.likelion.neezybackend.ranking.api.dto.RankingResponseDto;
import com.likelion.neezybackend.ranking.application.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
@Tag(name = "랭킹 API", description = "지역별 게시글 카테고리 랭킹 조회")
public class RankingController {

    private final RankingService rankingService;

    @Operation(
            summary = "지역별 카테고리 랭킹 조회",
            description = "특정 지역에서 가장 많이 언급된 카테고리 순위를 조회한다"
    )
    @GetMapping("/{region}")
    public ResponseEntity<List<RankingResponseDto>> getRankingByRegion(@PathVariable String region) {
        List<RankingResponseDto> rankings = rankingService.getRankingByRegion(region);
        return ResponseEntity.ok(rankings);
    }
}
