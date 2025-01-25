package org.bookwoori.core.domain.climbing.facade;

import java.util.Collections;
import java.util.EnumMap;
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
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;
import org.bookwoori.core.domain.climbing.service.ClimbingServiceImpl;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingMember.infrastructure.ClimbingMemberEntity;
import org.bookwoori.core.domain.climbingMember.service.ClimbingMemberServiceImpl;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.member.service.MemberServiceImpl;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.infrastructure.RecordEntity;
import org.bookwoori.core.domain.record.service.RecordServiceImpl;
import org.bookwoori.core.domain.review.infrastructure.ReviewEntity;
import org.bookwoori.core.domain.review.service.ReviewServiceImpl;
import org.bookwoori.core.domain.reviewEmoji.entity.EmojiType;
import org.bookwoori.core.domain.reviewEmoji.infrastructure.ReviewEmojiEntity;
import org.bookwoori.core.domain.reviewEmoji.service.ReviewEmojiServiceImpl;
import org.bookwoori.core.domain.serverMember.service.ServerMemberServiceImpl;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ClimbingMemberFacade {

    private final MemberServiceImpl memberService;
    private final ClimbingServiceImpl climbingService;
    private final ClimbingMemberServiceImpl climbingMemberService;
    private final RecordServiceImpl recordService;
    private final ReviewServiceImpl reviewService;
    private final ReviewEmojiServiceImpl reviewEmojiService;
    private final ServerMemberServiceImpl serverMemberService;


    public boolean toggleParticipation(Long climbingId) {
        MemberEntity currentMember = memberService.getCurrentMember();
        ClimbingEntity climbing = climbingService.getClimbingById(climbingId);
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
        ClimbingEntity climbing = climbingService.getClimbingById(climbingId);
        MemberEntity currentMember = memberService.getCurrentMember();
        if (!serverMemberService.isJoined(currentMember, climbing.getServer())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        List<ClimbingMemberEntity> climbingMemberList = climbingMemberService.getMembersByClimbing(
            climbing);
        List<ClimbingMemberUnitDto> climbingMembers = climbingMemberList.stream()
            .map(member -> {
                Optional<RecordEntity> record = recordService.getClimbingMemberRecordOpt(member,
                    climbing.getBook());
                ReadingStatus status = record.map(RecordEntity::getStatus)
                    .orElse(ReadingStatus.UNREAD);
                boolean isMine = member.getMember().equals(currentMember);
                int currentPage = record.map(RecordEntity::getCurrentPage).orElse(0);
                return ClimbingMemberUnitDto.from(isMine, member, status, currentPage);
            })
            .collect(Collectors.toList());

        return new ClimbingMemberResponseDto(climbingMembers);
    }

    public void updateClimbingMemberMemo(Long climbingId, ClimbingMemoUpdateRequestDto requestDto) {
        climbingService.isRunning(climbingId);
        MemberEntity currentMember = memberService.getCurrentMember();
        ClimbingMemberEntity climbingMember = climbingMemberService.getMemberInClimbing(
            currentMember,
            climbingId);
        climbingMember.updateMemo(requestDto.memo());
    }

    public void delegateClimbingRole(Long climbingId, ClimbingRoleDelegateRequestDto requestDto) {
        MemberEntity currentMember = memberService.getCurrentMember();
        MemberEntity newOwner = memberService.getMemberById(requestDto.memberId());
        climbingMemberService.delegateClimbingRole(climbingId, currentMember, newOwner);
    }

    public void shareReviewToClimbing(Long climbingId) {
        MemberEntity currentMember = memberService.getCurrentMember();
        ClimbingMemberEntity climbingMember = climbingMemberService.getMemberInClimbing(
            currentMember,
            climbingId);
        if (climbingMember.isHasShared()) {
            throw new CustomException(ErrorCode.REVIEW_ALREADY_SHARED);
        }
        climbingMember.updateShared(true);
    }

    @Transactional(readOnly = true)
    public boolean getHasShared(Long climbingId) {
        MemberEntity currentMember = memberService.getCurrentMember();
        ClimbingMemberEntity climbingMember = climbingMemberService.getMemberInClimbing(
            currentMember,
            climbingId);
        return climbingMember.isHasShared();
    }

    @Transactional(readOnly = true)
    public boolean isShareable(Long climbingId) {
        MemberEntity currentMember = memberService.getCurrentMember();
        ClimbingEntity climbing = climbingService.getClimbingById(climbingId);
        return reviewService.existsReviewByMemberAndBook(currentMember, climbing.getBook());
    }

    @Transactional(readOnly = true)
    public ClimbingReviewListResponseDto getClimbingReviewList(Long climbingId) {
        ClimbingEntity climbing = climbingService.getClimbingById(climbingId);
        // sharedReviews: hasShared true인 ClimbingMember의 Review 리스트
        // sharedReviewEmojis: sharedReviews의 ReviewEmoji 리스트
        List<Long> sharedMemberIds = climbingMemberService.getSharedMemberIds(climbing);
        List<ReviewEntity> sharedReviews = reviewService.getReviewsByMembersAndBook(sharedMemberIds,
            climbing.getBook());
        if (sharedReviews == null || sharedReviews.isEmpty()) {
            return new ClimbingReviewListResponseDto(true, Collections.emptyList());
        }
        // reviewMap: memberId(key), Review 객체(value)
        Map<Long, ReviewEntity> reviewMap = sharedReviews.stream()
            .collect(Collectors.toMap(
                review -> review.getRecord().getMember().getMemberId(),
                review -> review
            ));
        // sharedReviewEmoji
        List<ReviewEmojiEntity> sharedReviewEmojis = reviewEmojiService.getEmojisByClimbingAndReviews(
            climbing,
            sharedReviews);
        // reviewEmojiCounts: Emoij와 reviewId 기준으로 그룹화
        Map<Long, Map<EmojiType, Long>> reviewEmojiCounts = sharedReviewEmojis.stream()
            .collect(Collectors.groupingBy(
                reviewEmoji -> reviewEmoji.getReview().getReviewId(),
                Collectors.groupingBy(
                    ReviewEmojiEntity::getEmoji,
                    Collectors.counting()
                )
            ));
        // climbingReviews: sharedMemberIds를 순회하면서 ClimbingMemberReviewUnitDto 생성
        List<ClimbingMemberReviewUnitDto> climbingReviews = sharedMemberIds.stream()
            .map(memberId -> {
                ReviewEntity review = reviewMap.get(memberId);
                Map<EmojiType, Long> emojiCounts = reviewEmojiCounts.getOrDefault(
                    review.getReviewId(),
                    Collections.emptyMap());
                List<ReviewEmojiListCountDto> reviewEmojiList = emojiCounts.entrySet().stream()
                    .map(entry -> new ReviewEmojiListCountDto(entry.getKey(),
                        entry.getValue().intValue()))
                    .collect(Collectors.toList());
                ClimbingMemberEntity climbingMember = climbingMemberService.getClimbingMemberWithMember(
                    climbingId, memberId);
                return ClimbingMemberReviewUnitDto.from(climbingMember, review, reviewEmojiList);
            })
            .collect(Collectors.toList());
        return new ClimbingReviewListResponseDto(true, climbingReviews);
    }

    @Transactional(readOnly = true)
    public ClimbingReviewWithShareResponseDto getReviewWithAllowShare(Long climbingId) {
        MemberEntity currentMember = memberService.getCurrentMember();
        ClimbingEntity climbing = climbingService.getClimbingById(climbingId);
        ReviewEntity review = reviewService.getReviewByMemberAndBook(currentMember,
            climbing.getBook());
        return ClimbingReviewWithShareResponseDto.from(climbing, review);
    }

    @Transactional(readOnly = true)
    public ClimbingReviewWithoutShareResponseDto getReviewWithoutAllowShare(Long climbingId) {
        ClimbingEntity climbing = climbingService.getClimbingById(climbingId);
        return ClimbingReviewWithoutShareResponseDto.from(climbing);
    }

    public boolean toggleReviewReaction(Long climbingId, Long reviewId, EmojiType emoji) {
        MemberEntity currentMember = memberService.getCurrentMember();
        ClimbingEntity climbing = climbingService.getClimbingById(climbingId);
        ReviewEntity review = reviewService.getReviewById(reviewId);
        Optional<ReviewEmojiEntity> reviewEmoji = reviewEmojiService.getEmojisOpt(
            currentMember, climbing, review, emoji);
        if (reviewEmoji.isPresent()) {
            reviewEmojiService.deleteEmoji(currentMember, climbing, review, emoji);
            return false;
        } else {
            ReviewEmojiEntity newEmoji = ReviewEmojiEntity.builder()
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
        ReviewEntity review = reviewService.getReviewById(reviewId);
        // emojiMemberMap: emoji별로 그룹화
        EnumMap<EmojiType, List<ReviewEmojiMemberUnitDto>> emojiMemberMap = reviewEmojiService.getEmojisByReview(
                review)
            .stream()
            .collect(Collectors.groupingBy(
                ReviewEmojiEntity::getEmoji,
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
