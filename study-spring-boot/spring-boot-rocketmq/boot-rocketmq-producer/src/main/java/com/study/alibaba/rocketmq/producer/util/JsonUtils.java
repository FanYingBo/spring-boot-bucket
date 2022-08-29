package com.study.alibaba.rocketmq.producer.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static  <T> T readJsonFromByte(byte[] jsonBytes, Class<T> tClass) throws IOException {
        return objectMapper.readValue(jsonBytes, tClass);
    }

    public static  <T> T readJsonFromString(String jsonStr, Class<T> tClass) throws IOException {
        return objectMapper.readValue(jsonStr, tClass);
    }
}
