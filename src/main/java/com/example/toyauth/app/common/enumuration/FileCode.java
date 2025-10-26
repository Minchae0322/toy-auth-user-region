package com.example.toyauth.app.common.enumuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum FileCode {

    PRODUCT("PRODUCT", "제품 업로드 파일"),
    SALE("SALE", "판매 게시글 이미지"),
    FEED("FEED","피드 업로드 이미지")
    ;

    private String title;
    private String description;

    public static FileCode ofCode(String inputCode) {
        return Arrays.stream(values())
                .filter(v -> v.title.equals(inputCode))
                .findAny()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        String.format("statusCode의 code 값이 올바르지 않습니다. 입력 값 : %s", inputCode)));
    }
}
