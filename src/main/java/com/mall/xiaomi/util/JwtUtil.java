package com.mall.xiaomi.util;

import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
@Component
@Data
@Slf4j
public class JwtUtil {



    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration}")
    private Long  expiration;



    // 生成密钥的方法（静态方法，可单独调用）
    public static String generateJwtSecret() {
        byte[] keyBytes = new byte[32]; // HS256需要32字节
        SecureRandom random = new SecureRandom();
        random.nextBytes(keyBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
    }

    // 从Base64字符串创建SecretKey
    private SecretKey getSigningKey() {
        try {
            byte[] decodedKey = Base64.getUrlDecoder().decode(secret);
            return new SecretKeySpec(decodedKey, "HmacSHA256");
        } catch (IllegalArgumentException e) {
            // 如果是普通字符串，直接使用
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            // 确保密钥长度至少256位
            if (keyBytes.length < 32) {
                // 如果不够长，扩展到32字节
                byte[] paddedKey = new byte[32];
                System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 32));
                return new SecretKeySpec(paddedKey, "HmacSHA256");
            }
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        }
    }

    // 生成token -
    public String createToken(String username, String userId) {
        long time = System.currentTimeMillis();
        Date nowDate = new Date(time);
        long expireTime = time + expiration;
        Date expireDate = new Date(expireTime);

        return Jwts.builder()
                .id(userId)                     // 新版API
                .subject(username)              // 新版API
                .issuer(issuer)                 // 新版API
                .issuedAt(nowDate)              // 新版API
                .expiration(expireDate)         // 新版API
                .claim("userId", userId)
                .claim("username", username)
                .signWith(getSigningKey(), Jwts.SIG.HS256)  // 新版API
                .compact();
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("不支持的Token格式: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Token格式错误: {}", e.getMessage());
        } catch (SecurityException e) {
            log.error("签名验证失败: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Token参数错误: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Token验证异常: {}", e.getMessage());
        }
        return false;
    }
}
