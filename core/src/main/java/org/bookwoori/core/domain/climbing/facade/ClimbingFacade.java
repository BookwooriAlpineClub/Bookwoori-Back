package org.bookwoori.core.domain.climbing.facade;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.book.service.BookService;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.climbing.service.ClimbingService;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingMember.service.ClimbingMemberService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.server.service.ServerService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ClimbingFacade {

    private final MemberService memberService;
    private final ClimbingService climbingService;
    private final ServerService serverService;
    private final BookService bookService;
    private final ClimbingMemberService climbingMemberService;

    public void createClimbing(ClimbingChannelCreateRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Server server = serverService.getServerById(requestDto.serverId());
        Book book = bookService.getOrCreateBookByIsbn(requestDto.isbn());
        Climbing climbing = requestDto.toEntity(server, book);
        climbingService.saveClimbingChannel(climbing);
        climbingMemberService.saveMember(currentMember, climbing, ClimbingRole.OWNER);
    }

    public void updateClimbing(Long climbingId, ClimbingChannelUpdateRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Climbing climbing = climbingService.getClimbingById(climbingId);
        if (climbing.getStatus() != ClimbingStatus.READY) {
            throw new CustomException(ErrorCode.CLIMBING_NOT_READY);
        }
        if (!climbingMemberService.isOwner(currentMember, climbing)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);  // OWNER만 편집 가능
        }
        climbing.updateClimbing(requestDto.name(), requestDto.description(), requestDto.endDate());
    }

    public boolean toggleClimbing(Long climbingId) {
        Member currentMember = memberService.getCurrentMember();
        Climbing climbing = climbingService.getClimbingById(climbingId);
        boolean isJoined = climbingMemberService.isJoined(currentMember, climbing);
        if (isJoined) {
            climbingMemberService.removeMember(currentMember, climbing);
            return false;
        } else {
            climbingMemberService.saveMember(currentMember, climbing, ClimbingRole.MEMBER);
            return true;
        }
    }

}
