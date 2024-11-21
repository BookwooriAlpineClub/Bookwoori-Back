package org.bookwoori.core.domain.messageRoom.repository;

import java.util.Optional;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.messageRoom.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {

    @Query("SELECT m FROM MessageRoom m WHERE (m.sender = :member1 AND m.receiver = :member2) OR (m.sender = :member2 AND m.receiver = :member1)")
    Optional<MessageRoom> getMessageRoomByMembers(Member member1, Member member2);


}
