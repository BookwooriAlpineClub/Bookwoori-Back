package org.bookwoori.core.global.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60;  // 7일

//    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
//        Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(maxAge);
//        response.addCookie(cookie);
//
//        // SameSite 설정 추가
//        String sameSiteValue = "None";
//        response.addHeader("Set-Cookie",
//            String.format("%s=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=None",
//                name, value, maxAge, sameSiteValue, cookie.getSecure() ? "; Secure" : ""));
//    }

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        String cookie = String.format(
            "%s=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=None; Secure",
            name, value, maxAge
        );
        response.addHeader("Set-Cookie", cookie);
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
