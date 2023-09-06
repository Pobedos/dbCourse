package ru.pobedonostsev.framework.utils;

public final class StringUtils {
    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty() || string.isBlank();
    }
}
