package org.bookwoori.core.domain.climbing.facade;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.book.service.BookService;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingDetailsResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ServerClimbingListDto;
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
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
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

    public boolean toggleParticipation(Long climbingId) {
        Member currentMember = memberService.getCurrentMember();
        Climbing climbing = climbingService.getClimbingById(climbingId);
        boolean isJoined = climbingMemberService.isJoined(currentMember, climbing);
        if (isJoined) {
            if (climbingMemberService.isOwner(currentMember, climbing)) {
                throw new CustomException(ErrorCode.OWNER_CANNOT_LEAVE);
            }
            climbingMemberService.removeMember(currentMember, climbing);
            return false;
        } else {
            climbingMemberService.saveMember(currentMember, climbing, ClimbingRole.MEMBER);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public ClimbingDetailsResponseDto getClimbingDetails(Long climbingId) {
        Member currentMember = memberService.getCurrentMember();
        Climbing climbing = climbingService.getClimbingById(climbingId);
        int memberCount = climbingMemberService.getMemberCount(climbing);
        boolean isJoined = climbingMemberService.isJoined(currentMember, climbing);
        boolean isOwner = climbingMemberService.isOwner(currentMember, climbing);
        return ClimbingDetailsResponseDto.from(climbing, memberCount, isJoined, isOwner);
    }

    @Transactional(readOnly = true)
    public ServerClimbingListDto getClimbingList(Long serverId) {
        Member currentMember = memberService.getCurrentMember();
        // myClimbings
        List<ServerClimbingListDto.ClimbingUnitDto> myClimbings = climbingService.getMyClimbings(
                currentMember, serverId).stream()
            .limit(3)
            .map(climbing -> new ServerClimbingListDto.ClimbingUnitDto(climbing.getClimbingId(),
                climbing.getBook().getCoverImg()))
            .collect(Collectors.toList());
        // readyClimbs
        List<ServerClimbingListDto.ClimbingUnitDto> readyClimbs = climbingService.getReadyClimbings(
                serverId).stream()
            .limit(3)
            .map(climbing -> new ServerClimbingListDto.ClimbingUnitDto(climbing.getClimbingId(),
                climbing.getBook().getCoverImg()))
            .collect(Collectors.toList());
        return new ServerClimbingListDto(myClimbings, readyClimbs);
    }

    @Transactional(readOnly = true)
    public List<ClimbingDetailsResponseDto> getMyClimbingList(Long serverId) {
        Member currentMember = memberService.getCurrentMember();
        List<Climbing> myClimbings = climbingService.getMyClimbings(currentMember, serverId);
        return convertToDto(myClimbings);
    }

    @Transactional(readOnly = true)
    public List<ClimbingDetailsResponseDto> getReadyClimbingList(Long serverId) {
        List<Climbing> readyClimbs = climbingService.getReadyClimbings(serverId);
        return convertToDto(readyClimbs);
    }

    @Transactional(readOnly = true)
    protected List<ClimbingDetailsResponseDto> convertToDto(List<Climbing> climbings) {
        Member currentMember = memberService.getCurrentMember();
        return climbings.stream()
            .map(climbing -> {
                int memberCount = climbingMemberService.getMemberCount(climbing);
                boolean isJoined = climbingMemberService.isJoined(currentMember, climbing);
                boolean isOwner = climbingMemberService.isOwner(currentMember, climbing);
                return ClimbingDetailsResponseDto.from(climbing, memberCount, isJoined, isOwner);
            })
            .collect(Collectors.toList());
    }
}
