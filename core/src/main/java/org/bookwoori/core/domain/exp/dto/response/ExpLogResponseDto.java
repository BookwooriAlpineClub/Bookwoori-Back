package org.bookwoori.core.domain.exp.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.core.domain.exp.entity.Exp;
import org.bookwoori.core.domain.exp.entity.ExpType;

@Builder
public record ExpLogResponseDto(
  Long expLogId,
  double height,
  double amount,
  ExpType expType,
  String title,
  LocalDateTime createdAt) {

    public static ExpLogResponseDto from(Exp exp) {
      return ExpLogResponseDto.builder()
          .expLogId(exp.getExpLogId())
          .height(exp.getHeight())
          .amount(exp.getAmount())
          .expType(exp.getExpType())
          .title(exp.getTitle())
          .createdAt(exp.getCreatedAt())
          .build();
    }
}
