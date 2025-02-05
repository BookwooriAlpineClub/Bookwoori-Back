package org.bookwoori.core.domain.climbing.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingMemoUpdateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingRoleDelegateRequestDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingMemberResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingMemberReviewUnitDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingMemberUnitDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingReviewListResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingReviewWithShareResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingReviewWithoutShareResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ReviewEmojiListCountDto;
import org.bookwoori.core.domain.climbing.dto.response.ReviewEmojiListDto;
import org.bookwoori.core.domain.climbing.dto.response.ReviewEmojiMemberListResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ReviewEmojiMemberUnitDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.service.ClimbingService;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingMember.service.ClimbingMemberService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.review.dto.response.ReviewListResponseDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.service.RecordService;
import org.bookwoori.core.domain.review.dto.response.ReviewUnitDto;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.review.service.ReviewService;
import org.bookwoori.core.domain.reviewEmoji.entity.EmojiType;
import org.bookwoori.core.domain.reviewEmoji.entity.ReviewEmoji;
import org.bookwoori.core.domain.reviewEmoji.service.ReviewEmojiService;
import org.bookwoori.core.domain.serverMember.service.ServerMemberService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
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
    private final ReviewService reviewService;
    private final ReviewEmojiService reviewEmojiService;
    private final ServerMemberService serverMemberService;

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
    public ClimbingMemberResponseDto getClimbingMembers(Long climbingId) {
        // INACTIVE인 멤버 예외 처리 필요 
        Climbing climbing = climbingService.getClimbingById(climbingId);
        Member currentMember = memberService.getCurrentMember();
        if (!serverMemberService.isJoined(currentMember, climbing.getServer())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        List<ClimbingMember> climbingMemberList = climbingMemberService.getMembersByClimbing(
            climbing);
        List<ClimbingMemberUnitDto> climbingMembers = climbingMemberList.stream()
            .map(member -> {
                Optional<Record> record = recordService.getClimbingMemberRecordOpt(member,
                    climbing.getBook());
                ReadingStatus status = record.map(Record::getStatus).orElse(ReadingStatus.UNREAD);
                boolean isMine = member.getMember().equals(currentMember);
                int currentPage = record.map(Record::getCurrentPage).orElse(0);
                return ClimbingMemberUnitDto.from(isMine, member, status, currentPage);
            })
            .collect(Collectors.toList());

        return new ClimbingMemberResponseDto(climbingMembers);
    }

    public void updateClimbingMemberMemo(Long climbingId, ClimbingMemoUpdateRequestDto requestDto) {
        climbingService.isRunning(climbingId);
        Member currentMember = memberService.getCurrentMember();
        ClimbingMember climbingMember = climbingMemberService.getMemberInClimbing(currentMember,
            climbingId);
        climbingMember.updateMemo(requestDto.memo());
    }

    public void delegateClimbingRole(Long climbingId, ClimbingRoleDelegateRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Member newOwner = memberService.getMemberById(requestDto.memberId());
        climbingMemberService.delegateClimbingRole(climbingId, currentMember, newOwner);
    }

    @Transactional(readOnly = true)
    public boolean getHasShared(Long climbingId) {
        Member currentMember = memberService.getCurrentMember();
        ClimbingMember climbingMember = climbingMemberService.getMemberInClimbing(currentMember,
            climbingId);
        return climbingMember.isHasShared();
    }

    @Transactional(readOnly = true)
    public boolean isShareable(Long climbingId) {
        Member currentMember = memberService.getCurrentMember();
        Climbing climbing = climbingService.getClimbingById(climbingId);
        return reviewService.existsReviewByMemberAndBook(currentMember, climbing.getBook());
    }

    @Transactional(readOnly = true)
    public ClimbingReviewWithShareResponseDto getReviewWithAllowShare(Long climbingId) {
        Climbing climbing = climbingService.getClimbingById(climbingId);
        List<ReviewUnitDto> reviewList = getReviewListToClimbing(climbingId);
        return ClimbingReviewWithShareResponseDto.from(climbing, reviewList);
    }

    public List<ReviewUnitDto> getReviewListToClimbing(Long climbingId) {
        Member currentMember = memberService.getCurrentMember();
        Climbing climbing = climbingService.getClimbingById(climbingId);
        return reviewService.getReviewListByMemberAndBook(currentMember, climbing.getBook()).stream()
            .map(ReviewUnitDto::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClimbingReviewWithoutShareResponseDto getReviewWithoutAllowShare(Long climbingId) {
        Climbing climbing = climbingService.getClimbingById(climbingId);
        return ClimbingReviewWithoutShareResponseDto.from(climbing);
    }

    public boolean toggleReviewReaction(Long climbingId, Long reviewId, EmojiType emoji) {
        Member currentMember = memberService.getCurrentMember();
        Climbing climbing = climbingService.getClimbingById(climbingId);
        Review review = reviewService.getReviewById(reviewId);
        Optional<ReviewEmoji> reviewEmoji = reviewEmojiService.getEmojisOpt(
            currentMember, climbing, review, emoji);
        if (reviewEmoji.isPresent()) {
            reviewEmojiService.deleteEmoji(currentMember, climbing, review, emoji);
            return false;
        } else {
            ReviewEmoji newEmoji = ReviewEmoji.builder()
                .member(currentMember)
                .climbing(climbing)
                .review(review)
                .emoji(emoji)
                .build();
            reviewEmojiService.save(newEmoji);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public ReviewEmojiMemberListResponseDto getEmojiMemberList(Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        // emojiMemberMap: emoji별로 그룹화
        EnumMap<EmojiType, List<ReviewEmojiMemberUnitDto>> emojiMemberMap = reviewEmojiService.getEmojisByReview(
                review)
            .stream()
            .collect(Collectors.groupingBy(
                ReviewEmoji::getEmoji,
                () -> new EnumMap<>(EmojiType.class),
                Collectors.mapping(
                    reviewEmoji -> ReviewEmojiMemberUnitDto.from(reviewEmoji.getMember()),
                    Collectors.toList()
                )
            ));
        // emoji별로 그룹화된 항목을 ReviewEmojiListDto로 변환
        List<ReviewEmojiListDto> emojiLists = emojiMemberMap.entrySet().stream()
            .map(entry -> new ReviewEmojiListDto(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
        return new ReviewEmojiMemberListResponseDto(emojiLists);
    }
}
