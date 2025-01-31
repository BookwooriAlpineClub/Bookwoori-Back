package org.bookwoori.core.domain.exp.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.exp.entity.Exp;
import org.bookwoori.core.domain.exp.entity.ExpType;

@Builder
public record ExpLogResponseDto(
  Long expLogId,
  double height,
  double amount,
  ExpType expType) {

    public static ExpLogResponseDto from(Exp exp) {
      return ExpLogResponseDto.builder()
          .expLogId(exp.getExpLogId())
          .height(exp.getHeight())
          .amount(exp.getAmount())
          .expType(exp.getExpType())
          .build();
    }
}
