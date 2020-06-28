package com.huchonglin.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: hcl
 * @date: 2020/6/27 21:10
 */
@Slf4j
public class PropertiesUtil {
    public static Properties loadProperties() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            log.error("加载jdbc.properties失败");
        }
        return properties;
    }
}
