package com.example.toyauth.app.common.constants;


import org.springframework.beans.factory.annotation.Value;

public class GlobalConstants {
    public final static Integer FRONT_END_PORT_NUM = 5173;

    public final static String HTTP_HTTPS = "http";

    @Value("${value.server.url}")
    public static String SERVER_URL;

    public final static String ACCESS_TOKEN_PARAM_NAME = "access_token";
    public final static String REFRESH_TOKEN_PARAM_NAME = "refresh_token";

    public final static String ACCESS_TOKEN_HEADER_NAME = "Authorization";

    public final static String ACCESS_TOKEN_REFRESH_URL = "/token/refresh";


    public final static String AUTHORIZATION_HEADER = "Authorization";

    //랜덤 닉네임
    public static final String[] ADJECTIVES = {"빠른", "용감한", "똑똑한", "재치있는", "행복한", "충성스러운"};
    public static final String[] NOUNS = {"호랑이", "독수리", "판다", "늑대", "매", "매미"};
}
