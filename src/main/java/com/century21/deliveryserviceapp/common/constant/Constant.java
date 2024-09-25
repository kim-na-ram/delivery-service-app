package com.century21.deliveryserviceapp.common.constant;

public class Constant {
    public static final String AUTH_USER = "auth_user";

    public static final String BEARER_PREFIX = "Bearer ";

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 14 * 24 * 60 * 60 * 1000L;
    public static final long REFRESH_TOKEN_REDIS_TTL = 14 * 24 * 60 * 60L;
}
