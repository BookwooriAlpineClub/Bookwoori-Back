package org.bookwoori.core.domain.exp.facade;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.exp.dto.response.ExpLogListResponseDto;
import org.bookwoori.core.domain.exp.dto.response.ExpLogResponseDto;
import org.bookwoori.core.domain.exp.entity.Exp;
import org.bookwoori.core.domain.exp.service.ExpService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.server.dto.response.ServerItemDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ExpFacade {

  private final MemberService memberService;
  private final ExpService expService;

  public ExpLogListResponseDto getExpLogs() {
    Member currentmember = memberService.getCurrentMember();
    List<Exp> expLogs = expService.getExpLogListByMember(currentmember);
    List<ExpLogResponseDto> expDtoList = expLogs.stream().map(ExpLogResponseDto::from).toList();
    return new ExpLogListResponseDto(expDtoList);
  }
}