package com.zifan.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zifan.service.UserDetailsServiceImpl;
import com.zifan.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // 定义公开路由（不需要鉴权的路由）
    private static final String[] PUBLIC_ROUTES = {"/api/auth/login", "/api/auth/register"};

    // 检查请求路由是否是公开路由
    private boolean isPublicRoute(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        for (String publicRoute : PUBLIC_ROUTES) {
            if (publicRoute.equals(requestUri)) {
                return true;
            }
        }
        return false;
    }

    // 返回 JSON 格式的错误响应
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(status));
        errorResponse.put("message", message);

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            logger.info("Start Authentication Process....");

            // 检查是否是公开路由
            if (isPublicRoute(request)) {
                filterChain.doFilter(request, response);
                logger.info("Skip Authentication Process with public router, request detail: {}", request);
                return;
            }

            // 从请求头中提取 Token
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token is required");
                logger.info("Fail to Extract Token  or invalid token format, request detail: {}", request);
                return;
            }

            // 提取 Token 和用户邮箱
            String token = header.substring(7);
            String userEmail = jwtUtil.extractField(token, "email", String.class);

            // 验证 Token 合法性
            if (userEmail == null || !jwtUtil.validateToken(token)) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                logger.info("Invalid token, request detail: {}", request);
                return;
            }

            // 加载用户信息
            UserDetails userDetails = userDetailsService.loadUserByEmail(userEmail);
            User user = (User) userDetails;

            // 创建认证对象并存储到 SecurityContext 中
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            user, // 存储完整的 User 对象
                            null,
                            user.getAuthorities()
                    );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            logger.info("Pass Authentication, request detail: {}, User detail: {}", request, user);
            // 继续过滤器链
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 处理异常
            logger.error("Internal error in authentication, request detail: {}", request);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage());
        }
    }
}
