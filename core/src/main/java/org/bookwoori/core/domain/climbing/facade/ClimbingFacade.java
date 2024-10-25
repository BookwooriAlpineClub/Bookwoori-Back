package org.bookwoori.core.domain.climbing.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.book.service.BookService;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.service.ClimbingService;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingMember.service.ClimbingMemberService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.server.service.ServerService;
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

    public void updateClimbing(ClimbingChannelUpdateRequestDto requestDto) {
        Climbing climbing = climbingService.getClimbingById(requestDto.climbingId());
        Book book = bookService.getBookById(requestDto.bookId());
        climbing.updateClimbing(book, requestDto.name(), requestDto.description(), requestDto.startDate(), requestDto.endDate());
    }
}
