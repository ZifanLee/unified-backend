package com.zifan.security;

import com.zifan.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private int EXPIRATION_TIME;

    public String generateToken(@NotNull HashMap<String, Object> hashmap) {
        JwtBuilder builder = Jwts.builder();

        // 设置默认的 Header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT"); // 类型
        header.put("alg", "HS256"); // 签名算法
        builder.setHeader(header);

        // 添加自定义的声明
        hashmap.forEach(builder::claim);

        // 设置过期时间
        Calendar timenow = Calendar.getInstance();
        timenow.add(Calendar.MILLISECOND, this.EXPIRATION_TIME);
        builder.setExpiration(timenow.getTime());

        // 设置签名
        builder.signWith(SignatureAlgorithm.HS256, this.SECRET_KEY);

        return builder.compact();
    }

    public String extractUsername(@NotNull String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(this.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("username", String.class);
        } catch (Exception e) {
            // 处理异常，例如记录日志
            return null; // 或者抛出自定义异常
        }
    }

    public String extractEmail(@NotNull String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(this.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("email", String.class);
        } catch (Exception e) {
            // 处理异常，例如记录日志
            return null; // 或者抛出自定义异常
        }
    }

    // 获取指定字段内容，返回Object
    public <T> T extractField(@NotNull String token, @NotNull String fieldName, @NotNull Class<T> tClass) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(this.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return (T)claims.get(fieldName, tClass);
        } catch (Exception e) {
            // 处理异常，例如记录日志
            return null; // 或者抛出自定义异常
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token);
            return true; // Token 是有效的
        } catch (SignatureException e) {
            return false; // 签名无效
        } catch (ExpiredJwtException e) {
            return false; // Token 已过期
        } catch (MalformedJwtException e) {
            return false; // Token 格式错误
        } catch (UnsupportedJwtException e) {
            return false; // Token 不支持
        } catch (IllegalArgumentException e) {
            return false; // Token 为空或无效
        }
    }


    // 获取当前认证账户信息
    public static User getAuthenticatedUser() throws Exception {
        // 获取当前认证对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // 获取用户信息（Principal）
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return (User)principal;
            } else {
                throw new Exception("Unable to extract authenticated user from JWT");
            }
        } else {
            throw new Exception("Unable to extract authenticated user from JWT");
        }
    }


    // 检查给定邮箱参数是否是当前认证账户
    public static boolean AuthenticateEmail(String Email) {
        try {
            User user = getAuthenticatedUser();
            return Email.equals(user.getEmail());
        } catch (Exception e) {
            return false;
        }
    }


    public static String generateSecret() {
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

        // 将密钥转换为 Base64 编码的字符串
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        return base64Key;
    }
}