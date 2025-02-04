package org.bookwoori.auth.global.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class CookieUtil {

    public static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60;  // 7일

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        StringBuilder cookieHeader = new StringBuilder();
        cookieHeader.append(name).append("=").append(value).append(";");
        cookieHeader.append("Max-Age=").append(maxAge).append(";");
        cookieHeader.append("Expires=").append(new java.util.Date(System.currentTimeMillis() + maxAge * 1000L)).append(";");
        cookieHeader.append("Path=/;");
        cookieHeader.append("HttpOnly;");
        cookieHeader.append("Secure;");
        cookieHeader.append("SameSite=None;");
        response.addHeader("Set-Cookie", cookieHeader.toString());
    }

    public void addCookieAndSetDomain(HttpServletResponse response, String name, String value, int maxAge, String domain) {
        StringBuilder cookieHeader = new StringBuilder();
        cookieHeader.append(name).append("=").append(value).append(";");
        cookieHeader.append("Max-Age=").append(maxAge).append(";");
        cookieHeader.append("Expires=").append(new java.util.Date(System.currentTimeMillis() + maxAge * 1000L)).append(";");
        cookieHeader.append("Domain=").append(domain).append(";");
        cookieHeader.append("Path=/;");
        cookieHeader.append("HttpOnly;");
        cookieHeader.append("Secure;");
        cookieHeader.append("SameSite=None;");

        response.addHeader("Set-Cookie", cookieHeader.toString());
    }

    public Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    public void deleteCookie(HttpServletRequest request, HttpServletResponse response,
        String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
