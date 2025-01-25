package org.bookwoori.core.domain.climbing.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.infrastructure.BookEntity;
import org.bookwoori.core.domain.book.service.BookServiceImpl;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingDetailsResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.MyClimbingListResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ReadyClimbingListResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ServerClimbingListDto;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;
import org.bookwoori.core.domain.climbing.service.ClimbingServiceImpl;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingMember.infrastructure.ClimbingMemberEntity;
import org.bookwoori.core.domain.climbingMember.service.ClimbingMemberServiceImpl;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.member.service.MemberServiceImpl;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.service.RecordServiceImpl;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;
import org.bookwoori.core.domain.server.service.ServerServiceImpl;
import org.bookwoori.core.domain.serverMember.service.ServerMemberServiceImpl;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ClimbingFacade {

    private final MemberServiceImpl memberService;
    private final ClimbingServiceImpl climbingService;
    private final ServerServiceImpl serverService;
    private final BookServiceImpl bookService;
    private final ClimbingMemberServiceImpl climbingMemberService;
    private final RecordServiceImpl recordService;
    private final ServerMemberServiceImpl serverMemberService;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateClimbingStatus() {
        LocalDate today = LocalDate.now();
        List<ClimbingEntity> climbingList = climbingService.getAllClimbings();

        for (ClimbingEntity climbing : climbingList) {
            List<ClimbingMemberEntity> climbingMemberList = climbingMemberService.getMembersByClimbing(
                climbing);
            // 2명 이상 참여자일 때만 RUNNING으로 변경
            if (climbingMemberList.size() < 2) {
                climbing.updateStatus(ClimbingStatus.FAILED);
                continue;
            }
            if (!climbing.getStartDate().isAfter(today) && climbing.getEndDate().isAfter(today)) {
                climbing.updateStatus(ClimbingStatus.RUNNING);
                continue;
            }
            // 종료 날짜가 지난 경우 상태 변경 (FINISHED/FAILED)
            if (climbing.getEndDate().isBefore(today)) {
                boolean allFinished = climbingMemberList.stream()
                    .allMatch(member -> recordService.getClimbingMemberRecordOpt(member,
                            climbing.getBook())
                        .map(record -> record.getStatus() == ReadingStatus.FINISHED)
                        .orElse(false));
                ClimbingStatus newStatus =
                    allFinished ? ClimbingStatus.FINISHED : ClimbingStatus.FAILED;
                climbing.updateStatus(newStatus);
            }
            climbingService.saveClimbingChannel(climbing);
        }
    }


    public void createClimbing(ClimbingChannelCreateRequestDto requestDto) {
        MemberEntity currentMember = memberService.getCurrentMember();
        ServerEntity server = serverService.getServerById(requestDto.serverId());
        BookEntity book = bookService.getOrCreateBookByIsbn(requestDto.isbn());
        ClimbingEntity climbing = requestDto.toEntity(server, book, ClimbingStatus.READY);
        climbingService.saveClimbingChannel(climbing);
        climbingMemberService.saveMember(currentMember, climbing, ClimbingRole.OWNER);
    }

    public void updateClimbing(Long climbingId, ClimbingChannelUpdateRequestDto requestDto) {
        MemberEntity currentMember = memberService.getCurrentMember();
        ClimbingEntity climbing = climbingService.getClimbingById(climbingId);
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
        MemberEntity currentMember = memberService.getCurrentMember();
        ClimbingEntity climbing = climbingService.getClimbingById(climbingId);
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
        MemberEntity currentMember = memberService.getCurrentMember();
        ServerEntity server = serverService.getServerById(serverId);
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
        MemberEntity currentMember = memberService.getCurrentMember();
        ServerEntity server = serverService.getServerById(serverId);
        if (!serverMemberService.isJoined(currentMember, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        List<ClimbingEntity> myClimbings = climbingService.getMyClimbings(currentMember, serverId);
        List<ClimbingDetailsResponseDto> myClimbingList = convertToDto(myClimbings);
        return new MyClimbingListResponseDto(myClimbingList);
    }

    @Transactional(readOnly = true)
    public ReadyClimbingListResponseDto getReadyClimbingList(Long serverId) {
        MemberEntity currentMember = memberService.getCurrentMember();
        ServerEntity server = serverService.getServerById(serverId);
        if (!serverMemberService.isJoined(currentMember, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        List<ClimbingEntity> readyClimbings = climbingService.getReadyClimbings(serverId);
        List<ClimbingDetailsResponseDto> readyClimbingList = convertToDto(readyClimbings);
        return new ReadyClimbingListResponseDto(readyClimbingList);
    }

    @Transactional(readOnly = true)
    protected List<ClimbingDetailsResponseDto> convertToDto(List<ClimbingEntity> climbings) {
        MemberEntity currentMember = memberService.getCurrentMember();
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
        ClimbingEntity climbing = climbingService.getClimbingById(climbingId);
        MemberEntity currentMember = memberService.getCurrentMember();
        if (climbing.getStatus() != ClimbingStatus.READY) {
            throw new CustomException(ErrorCode.CLIMBING_NOT_READY);
        }
        if (!climbingMemberService.isOwner(currentMember, climbing)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);  // OWNER만 편집 가능
        }
        climbingService.deleteClimbing(climbing);
    }
}
