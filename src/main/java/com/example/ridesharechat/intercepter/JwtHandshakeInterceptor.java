package com.example.ridesharechat.intercepter;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        URI uri = request.getURI();
        String query = uri.getQuery(); // token=dummy-token-user-1

        if (query != null && query.contains("token=")) {
            String token = query.split("token=")[1];

            // 🔥 TEST PARSING LOGIC
            // token format: dummy-token-user-1
            String userId = token.substring(token.lastIndexOf("-") + 1);

            attributes.put("userId", userId);
            return true;
        }

        return false; // reject if no token
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {}
}
