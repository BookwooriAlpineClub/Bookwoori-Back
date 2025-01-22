package org.bookwoori.core.domain.member.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UpdateMemberBackgroungImgRequestDto(
    MultipartFile backgroundImg
) {


}
