package com.zifan.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxpemlmYW4yQHFxLmNvbSIsImV4cCI6MTczNjkxODQ1OH0.x8VJZ57CNh83TYWTV6lxEdBFLwIHOU5IpuY4H32rGG4";

    @Test
    public void testGenerateToken() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", "lizifan@qq.com");
        String token = jwtUtil.generateToken(hashMap);
        System.out.println(token);
    }

    @Test
    public void testGenerateSecret() throws NoSuchFieldException, IllegalAccessException {
        System.out.println(JwtUtil.generateSecret());
    }

    @Test
    public void testValidateToken() {
        boolean success = jwtUtil.validateToken(this.token);
        System.out.println(success);
    }

    @Test
    public void testExtractUsername() {

    }

    @Test
    public void testExtractEmail() {
        System.out.println(jwtUtil.extractEmail(this.token));

        System.out.println(jwtUtil.extractField(this.token, "email", String.class));
    }
}