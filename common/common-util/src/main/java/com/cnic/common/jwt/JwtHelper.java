package com.cnic.common.jwt;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;


public class JwtHelper {
    // token有效时间
    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;
    // 签名密钥
    private static String tokenSignKey = "123456";

    // 根据用户 id 和用户名称， 生成token的字符串
    public static String createToken(Long userId, String username) {
        String token = Jwts.builder()
                .setSubject("AUTH-USER")
                // 设置有效时长
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                // 设置主体部分
                .claim("userId", userId)
                .claim("username", username)
                // 设置签名
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    // 从token中获取用户id
    public static Long getUserId(String token) {
        try {
            if (StringUtils.isEmpty(token)) return null;

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            Integer userId = (Integer) claims.get("userId");
            return userId.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUsername(String token) {
        try {
            if (StringUtils.isEmpty(token)) return "";

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        String token = JwtHelper.createToken(1L, "admin");
        System.out.println(token);
        String username = JwtHelper.getUsername(token);
        Long userId = JwtHelper.getUserId(token);

        System.out.println("username = " + username);
        System.out.println("userId = " + userId);
    }

}
