package org.bookwoori.core.domain.member.repository;

import java.util.Optional;
import org.bookwoori.core.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByKakaoId(Long kakaoId);

    @Query("SELECT m.id FROM Member m WHERE m.kakaoId = :kakaoId")
    Long findIdByKakaoId(@Param("kakaoId") Long kakaoId);

    boolean existsByKakaoId(Long kakaoId);
}
