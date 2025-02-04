package org.bookwoori.core.domain.exp.repository;

import java.util.List;
import org.bookwoori.core.domain.exp.entity.Exp;
import org.bookwoori.core.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpRepository extends JpaRepository<Exp, Long> {

  List<Exp> findAllByMember(Member member);

}
