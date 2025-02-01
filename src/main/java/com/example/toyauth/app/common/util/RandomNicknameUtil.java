package com.example.toyauth.app.common.util;

import java.util.Random;

import static com.example.toyauth.app.common.constants.GlobalConstants.ADJECTIVES;
import static com.example.toyauth.app.common.constants.GlobalConstants.NOUNS;

public class RandomNicknameUtil {

    private static final Random RANDOM = new Random();

    public static String generateRandomNickname() {
        String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[RANDOM.nextInt(NOUNS.length)];
        int number = RANDOM.nextInt(100000);
        return adjective + noun + number;
    }
}
