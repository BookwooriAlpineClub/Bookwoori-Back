package org.bookwoori.core.domain.exp.service;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.exp.entity.Exp;
import org.bookwoori.core.domain.exp.entity.ExpType;
import org.bookwoori.core.domain.exp.repository.ExpRepository;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpServiceImpl implements ExpService {

  private final ExpRepository expRepository;

  @Override
  public List<Exp> getExpLogListByMember(Member currentmember) {
    return expRepository.findAllByMember(currentmember);
  }

  @Override
  public void grantExpToMember(Member member, ExpType expType, double amount, String title) {
    if (member == null) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }
    double totalHeight = member.updateHeight(amount);
    Exp expLog = Exp.builder()
        .member(member)
        .height(totalHeight)
        .amount(amount)
        .expType(expType)
        .title(title)
        .build();
    expRepository.save(expLog);
  }
}
