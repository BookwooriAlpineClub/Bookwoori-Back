package org.bookwoori.core.domain.exp.service;

import java.util.List;
import org.bookwoori.core.domain.exp.entity.Exp;
import org.bookwoori.core.domain.exp.entity.ExpType;
import org.bookwoori.core.domain.member.entity.Member;

public interface ExpService {

  List<Exp> getExpLogListByMember(Member currentmember);

  void grantExpToMember(Member member, ExpType expType, double amount);
}
