package com.minio.cloudminio.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.NonNull;

public class StringUtil {

    public static @NonNull String getSoleString() {
        return NanoIdUtils.randomNanoId();
    }
}
