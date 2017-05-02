package com.johnxue.common.config;

import java.util.List;

/**
 * @author han.xue
 * @since 2017-04-30 10:43：S
 */
public class AuthorityConfig {
    public static List<String> basePackages;
    public static String destination;

    public List<String> getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(List<String> basePackages) {
        AuthorityConfig.basePackages = basePackages;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        AuthorityConfig.destination = destination;
    }
}
