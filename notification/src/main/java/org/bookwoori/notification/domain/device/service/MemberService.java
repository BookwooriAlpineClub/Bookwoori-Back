package org.bookwoori.notification.domain.device.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bookwoori.notification.global.exception.CustomException;
import org.bookwoori.notification.global.exception.CustomExceptionStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    public Long getCurrentMemberId(HttpServletRequest request) {
        String memberIdHeader = request.getHeader("memberId");
        if (memberIdHeader == null || memberIdHeader.isEmpty()) {
            throw new CustomException(CustomExceptionStatus.UNAUTHORIZED);
        }
        return Long.valueOf(memberIdHeader);
    }

}
