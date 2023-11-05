package com.lwq.bilibili.service.util;

import java.util.Calendar;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lwq.bilibili.domain.exception.ConditionException;

public class TokenUtil {
    private static String ISSUER = "bilibili";

    public static String generateToken(Long userId) throws IllegalArgumentException, Exception {
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 30);
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);

    }

    public static Long vertifyToken(String token) {
        Algorithm algorithm;
        try {
            algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        String userId = jwt.getKeyId();
        return Long.valueOf(userId);
        } catch (TokenExpiredException e) {
        throw new ConditionException("555", "token已过期");
        } catch (Exception e) {
        throw new ConditionException("非法用户token");
        }
    }
}
