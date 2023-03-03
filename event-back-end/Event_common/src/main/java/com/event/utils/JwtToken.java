package com.event.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JwtToken {
    private static final long EXPIRE_TIME = 80 * 60 * 60 * 1000;
    private static final String TOKEN_SECRET = "qqdqylyq";

    /**
     * 生成签名，150小时过期
     */
    public static String createToken(String userid) {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            Map<String, Object> header = new ConcurrentHashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userid", userid)
                    .withExpiresAt(date)
                    .sign(algorithm);
    }
    /**
     * 检验token是否正确
     * @param **token**
     */
    public static String isToken(String token) {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("userid").asString();
    }
}