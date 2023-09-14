package com.demo.provider.common.assertion;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 入参校验；可仿照{@link org.springframework.util.Assert}编写
 */
public class ParameterAssert {

    public static void hasText(@Nullable String text, @NonNull String name) {
        if (!StringUtils.hasText(text)) {
            throw new ParameterAssertionError(name, "have text", "it must not be null, empty, or blank");
        }
    }

    public static void notNull(@Nullable Object object, @NonNull String name) {
        if (Objects.isNull(object)) {
            throw new ParameterAssertionError(name, "not null", "it must not be null");
        }
    }

}
