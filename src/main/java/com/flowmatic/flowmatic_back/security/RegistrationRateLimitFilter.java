package com.flowmatic.flowmatic_back.security;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegistrationRateLimitFilter implements Filter {

  private static final int MAX_REQUESTS = 5;
  private static final long WINDOW_MS = 60_000;

  private final ConcurrentHashMap<String, Deque<Long>> requestLog = new ConcurrentHashMap<>();

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if ("POST".equals(request.getMethod()) && "/api/auth/register".equals(request.getRequestURI())) {
      String ip = request.getRemoteAddr();
      long now = System.currentTimeMillis();

      requestLog.putIfAbsent(ip, new ArrayDeque<>());
      Deque<Long> timestamps = requestLog.get(ip);

      synchronized (timestamps) {
        while (!timestamps.isEmpty() && (now - timestamps.peekFirst()) > WINDOW_MS) {
          timestamps.pollFirst();
        }
        if (timestamps.size() >= MAX_REQUESTS) {
          response.setStatus(429);
          response.setContentType("application/json;charset=UTF-8");
          response.getWriter().write(
              "{\"status\":429,\"error\":\"Too Many Requests\","
                  + "\"message\":\"Trop de tentatives. Réessayez dans une minute.\"}");
          return;
        }
        timestamps.addLast(now);
      }
    }

    chain.doFilter(request, response);
  }
}
