package org.bookwoori.core.domain.climbing.facade;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingMemoUpdateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingRoleDelegateRequestDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingMemberUnitDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.service.ClimbingService;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.climbingMember.service.ClimbingMemberService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.service.RecordService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ClimbingMemberFacade {

    private final MemberService memberService;
    private final ClimbingService climbingService;
    private final ClimbingMemberService climbingMemberService;
    private final RecordService recordService;

    @Transactional(readOnly = true)
    public List<ClimbingMemberUnitDto> getClimbingMembers(Long climbingId) {
        Climbing climbing = climbingService.getClimbingById(climbingId);
        List<ClimbingMember> climbingMemberList = climbingMemberService.findMembersByClimbing(
            climbing);
        return climbingMemberList.stream()
            .map(member -> {
                Optional<Record> record = recordService.getClimbingMemberRecord(member,
                    climbing.getBook());
                ReadingStatus status = record.map(Record::getStatus).orElse(ReadingStatus.UNREAD);
                int currentPage = record.map(Record::getCurrentPage).orElse(0);
                return ClimbingMemberUnitDto.from(member, status, currentPage);
            })
            .collect(Collectors.toList());
    }


    public void updateClimbingMemberMemo(Long climbingId, ClimbingMemoUpdateRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        ClimbingMember climbingMember = climbingMemberService.findByClimbing(currentMember,
            climbingId);
        climbingMember.updateMemo(requestDto.memo());
    }

    public void delegateClimbingRole(Long climbingId, ClimbingRoleDelegateRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Member newOwner = memberService.getMemberById(requestDto.memberId());
        climbingMemberService.delegateClimbingRole(climbingId, currentMember, newOwner);
    }
}
