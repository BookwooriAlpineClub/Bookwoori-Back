package org.bookwoori.core.domain.climbing.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.book.service.BookService;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingDetailsResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.MyClimbingListResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ReadyClimbingListResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ServerClimbingListDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.climbing.service.ClimbingService;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingMember.service.ClimbingMemberService;
import org.bookwoori.core.domain.exp.annotation.GrantExp;
import org.bookwoori.core.domain.exp.entity.ExpType;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.service.RecordService;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.server.service.ServerService;
import org.bookwoori.core.domain.serverMember.service.ServerMemberService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final RecordService recordService;
    private final ServerMemberService serverMemberService;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateClimbingStatus() {
        LocalDate today = LocalDate.now();
        List<Climbing> climbingList = climbingService.getAllClimbings();
        for (Climbing climbing : climbingList) {
            List<ClimbingMember> climbingMemberList = climbingMemberService.getMembersByClimbing(climbing);
            // 참여자가 2명 미만일 때 FAILED로 변경
            if (climbingMemberList.size() < 2) {
                climbing.updateStatus(ClimbingStatus.FAILED);
                continue;
            }
            // 상태를 RUNNING으로 변경
            if (!climbing.getStartDate().isAfter(today) && climbing.getEndDate().isAfter(today)) {
                climbing.updateStatus(ClimbingStatus.RUNNING);
                continue;
            }
            // 종료 날짜가 지난 경우 상태 변경
            if (climbing.getEndDate().isBefore(today)) {
                boolean allFinished = climbingMemberList.stream()
                    .allMatch(member -> recordService.getClimbingMemberRecordOpt(member, climbing.getBook())
                        .map(record -> record.getStatus() == ReadingStatus.FINISHED)
                        .orElse(false));
                if (allFinished) {
                    updateFinishedClimbingStatus(climbing);
                }
                else {
                    climbing.updateStatus(ClimbingStatus.FAILED);
                }
            }
            climbingService.saveClimbingChannel(climbing);
        }
    }

    @Transactional
    @GrantExp(type = ExpType.FINISHED_CLIMBING)
    public void updateFinishedClimbingStatus(Climbing climbing){
        climbing.updateStatus(ClimbingStatus.FINISHED);
    }

    public void createClimbing(ClimbingChannelCreateRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Server server = serverService.getServerById(requestDto.serverId());
        Book book = bookService.getOrCreateBookByIsbn(requestDto.isbn());
        Climbing climbing = requestDto.toEntity(server, book, ClimbingStatus.READY);
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

    @Transactional(readOnly = true)
    public ClimbingDetailsResponseDto getClimbingDetails(Long climbingId) {
        Member currentMember = memberService.getCurrentMember();
        Climbing climbing = climbingService.getClimbingById(climbingId);
        if (!serverMemberService.isJoined(currentMember, climbing.getServer())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        int memberCount = climbingMemberService.getMemberCount(climbing);
        boolean isJoined = climbingMemberService.isJoined(currentMember, climbing);
        boolean isOwner = climbingMemberService.isOwner(currentMember, climbing);
        return ClimbingDetailsResponseDto.from(climbing, memberCount, isJoined, isOwner);
    }

    @Transactional(readOnly = true)
    public ServerClimbingListDto getClimbingList(Long serverId) {
        Member currentMember = memberService.getCurrentMember();
        Server server = serverService.getServerById(serverId);
        if (!serverMemberService.isJoined(currentMember, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        // myClimbs
        List<ServerClimbingListDto.ClimbingUnitDto> myClimbings = climbingService.getMyClimbings(
                currentMember, serverId).stream()
            .map(climbing -> new ServerClimbingListDto.ClimbingUnitDto(climbing.getClimbingId(),
                climbing.getName(), climbing.getBook().getCoverImg()))
            .collect(Collectors.toList());
        // readyClimbs
        List<ServerClimbingListDto.ClimbingUnitDto> readyClimbs = climbingService.getReadyClimbings(
                serverId).stream()
            .map(climbing -> new ServerClimbingListDto.ClimbingUnitDto(climbing.getClimbingId(),
                climbing.getName(), climbing.getBook().getCoverImg()))
            .collect(Collectors.toList());
        // runningClimbs
        List<ServerClimbingListDto.ClimbingUnitDto> runningClimbs = climbingService.getRunningClimbs(
                serverId).stream()
            .map(climbing -> new ServerClimbingListDto.ClimbingUnitDto(climbing.getClimbingId(),
                climbing.getName(), null))
            .collect(Collectors.toList());
        // endClimbs
        List<ServerClimbingListDto.ClimbingUnitDto> finishedClimbs = climbingService.getEndClimbs(
                serverId).stream()
            .map(climbing -> new ServerClimbingListDto.ClimbingUnitDto(climbing.getClimbingId(),
                climbing.getName(), null))
            .collect(Collectors.toList());
        return new ServerClimbingListDto(myClimbings, readyClimbs, runningClimbs, finishedClimbs);
    }

    @Transactional(readOnly = true)
    public MyClimbingListResponseDto getMyClimbingList(Long serverId) {
        Member currentMember = memberService.getCurrentMember();
        Server server = serverService.getServerById(serverId);
        if (!serverMemberService.isJoined(currentMember, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        List<Climbing> myClimbings = climbingService.getMyClimbings(currentMember, serverId);
        List<ClimbingDetailsResponseDto> myClimbingList = convertToDto(myClimbings);
        return new MyClimbingListResponseDto(myClimbingList);
    }

    @Transactional(readOnly = true)
    public ReadyClimbingListResponseDto getReadyClimbingList(Long serverId) {
        Member currentMember = memberService.getCurrentMember();
        Server server = serverService.getServerById(serverId);
        if (!serverMemberService.isJoined(currentMember, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        List<Climbing> readyClimbings = climbingService.getReadyClimbings(serverId);
        List<ClimbingDetailsResponseDto> readyClimbingList = convertToDto(readyClimbings);
        return new ReadyClimbingListResponseDto(readyClimbingList);
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

    public void deleteClimbing(Long climbingId) {
        Climbing climbing = climbingService.getClimbingById(climbingId);
        Member currentMember = memberService.getCurrentMember();
        if (climbing.getStatus() != ClimbingStatus.READY) {
            throw new CustomException(ErrorCode.CLIMBING_NOT_READY);
        }
        if (!climbingMemberService.isOwner(currentMember, climbing)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);  // OWNER만 편집 가능
        }
        climbingService.deleteClimbing(climbing);
    }
}
