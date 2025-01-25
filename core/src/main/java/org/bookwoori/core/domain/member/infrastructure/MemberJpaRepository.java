package org.bookwoori.core.domain.member.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByKakaoId(Long kakaoId);

    @Query("SELECT m.id FROM MemberEntity m WHERE m.kakaoId = :kakaoId")
    Long findIdByKakaoId(@Param("kakaoId") Long kakaoId);

    boolean existsByKakaoId(Long kakaoId);

    boolean existsByNickname(String newNickname);
}
