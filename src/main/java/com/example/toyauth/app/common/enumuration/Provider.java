package com.example.toyauth.app.common.enumuration;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Provider {

    GITHUB("github"),
    COMMON("common");


    Provider(String provider) {
        this.provider = provider;
    }

    public String getValue() {
        return this.provider;
    }

    private final String provider;

    public static Provider ofProvider(String input) {
        return Arrays.stream(values())
                .filter(v -> v.provider.equals(input))
                .findAny()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        String.format("statusCode의 provider 값이 올바르지 않습니다. 입력 값 : %s", input)));
    }
}
