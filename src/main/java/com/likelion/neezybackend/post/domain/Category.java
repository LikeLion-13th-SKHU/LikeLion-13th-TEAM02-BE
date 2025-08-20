package com.likelion.neezybackend.post.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "게시글 카테고리")
public enum Category {

    // 음식점
    KOREAN("한식"),
    CHICKEN("치킨집"),
    PIZZA("피자가게"),
    BURGER("햄버거집"),
    SNACK_BAR("분식집"),
    JAPANESE("일식"),
    CHINESE("중식"),
    WESTERN("양식"),
    BBQ("고기집"),
    CAFETERIA("백반집"),
    DESSERT("디저트 전문점"),
    CAFE("카페"),
    BAKERY("빵집"),

    // 놀거리
    PC_ROOM("PC방"),
    KARAOKE("노래방"),
    BOWLING("볼링장"),
    BILLIARD("당구장"),
    ARCADE("오락실"),
    ESCAPE_ROOM("방탈출"),
    VR_ZONE("VR 체험관"),
    MOVIE_THEATER("영화관"),
    SPORTS_COMPLEX("스포츠 시설"),
    GYM("헬스장"),
    YOGA_PILATES("요가/필라테스"),

    // 생활편의
    CONVENIENCE_STORE("편의점"),
    MART("대형마트"),
    SUPERMARKET("슈퍼마켓"),
    PHARMACY("약국"),
    HOSPITAL("병원"),
    CLINIC("의원"),
    DENTAL("치과"),
    VET("동물병원"),
    HAIR_SHOP("미용실"),
    NAIL_SHOP("네일샵"),
    STUDY_CAFE("스터디카페"),
    LIBRARY("도서관"),
    ACADEMY("학원"),

    // 기타
    BANK("은행"),
    POST_OFFICE("우체국"),
    CULTURE_CENTER("문화센터"),
    KIDS_CARE("어린이집/유치원"),
    ETC("기타");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

}
