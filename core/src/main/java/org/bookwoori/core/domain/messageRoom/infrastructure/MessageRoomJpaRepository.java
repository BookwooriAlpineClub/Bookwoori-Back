package org.bookwoori.core.domain.messageRoom.infrastructure;

import java.util.Optional;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRoomJpaRepository extends JpaRepository<MessageRoomEntity, Long> {

    @Query("SELECT m FROM MessageRoomEntity m WHERE (m.sender = :member1 AND m.receiver = :member2) OR (m.sender = :member2 AND m.receiver = :member1)")
    Optional<MessageRoomEntity> getMessageRoomByMembers(MemberEntity member1, MemberEntity member2);

    @Query("SELECT m FROM MessageRoomEntity m WHERE m.sender = :member or m.receiver = :member")
    Page<MessageRoomEntity> getMessageRoomsByMember(MemberEntity member, Pageable pageable);
}
