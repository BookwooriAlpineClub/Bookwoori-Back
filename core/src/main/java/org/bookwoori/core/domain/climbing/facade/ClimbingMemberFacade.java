package org.bookwoori.core.domain.climbing.facade;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingMemoUpdateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingReviewAddRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingRoleDelegateRequestDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingMemberResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingMemberReviewUnitDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingMemberUnitDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingReviewListResponseDto;
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
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.service.RecordService;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.review.service.ReviewService;
import org.bookwoori.core.domain.reviewEmoji.entity.Emoji;
import org.bookwoori.core.domain.reviewEmoji.entity.ReviewEmoji;
import org.bookwoori.core.domain.reviewEmoji.service.ReviewEmojiService;
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
        Climbing climbing = climbingService.getClimbingById(climbingId);
        List<ClimbingMember> climbingMemberList = climbingMemberService.findByClimbing(climbing);
        List<ClimbingMemberUnitDto> climbingMembers = climbingMemberList.stream()
            .map(member -> {
                Optional<Record> record = recordService.getClimbingMemberRecordOpt(member,
                    climbing.getBook());
                ReadingStatus status = record.map(Record::getStatus).orElse(ReadingStatus.UNREAD);
                int currentPage = record.map(Record::getCurrentPage).orElse(0);
                return ClimbingMemberUnitDto.from(member, status, currentPage);
            })
            .collect(Collectors.toList());

        return new ClimbingMemberResponseDto(climbingMembers);
    }

    public void updateClimbingMemberMemo(Long climbingId, ClimbingMemoUpdateRequestDto requestDto) {
        climbingService.isRunning(climbingId);
        Member currentMember = memberService.getCurrentMember();
        ClimbingMember climbingMember = climbingMemberService.findByMemberAndClimbing(currentMember,
            climbingId);
        climbingMember.updateMemo(requestDto.memo());
    }

    public void delegateClimbingRole(Long climbingId, ClimbingRoleDelegateRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Member newOwner = memberService.getMemberById(requestDto.memberId());
        climbingMemberService.delegateClimbingRole(climbingId, currentMember, newOwner);
    }

    public void shareReviewWithClimbing(Long climbingId) {
        Member currentMember = memberService.getCurrentMember();
        ClimbingMember climbingMember = climbingMemberService.findByMemberAndClimbing(currentMember,
            climbingId);
        if (climbingMember.isHasShared()) {
            throw new CustomException(ErrorCode.REVIEW_ALREADY_SHARED);
        }
        climbingMember.updateShared(true);
    }

    @Transactional(readOnly = true)
    public ClimbingReviewListResponseDto getClimbingReviewList(Long climbingId) {
        Climbing climbing = climbingService.getClimbingById(climbingId);
        List<ClimbingMember> climbingMembers = climbingMemberService.findByClimbing(climbing);
        // members: ClimbingMember의 memberId 목록
        List<Long> members = climbingMembers.stream()
            .map(climbingMember -> climbingMember.getMember().getMemberId())
            .collect(Collectors.toList());
        // reviews: memberId들과 book에 해당하는 Review 목록
        List<Review> reviews = reviewService.findByMembersAndBook(members, climbing.getBook());
        Map<Long, Review> reviewMap = reviews.stream()
            .collect(Collectors.toMap(review -> review.getRecord().getMember().getMemberId(),
                review -> review));
        // climbingReviews
        List<ClimbingMemberReviewUnitDto> climbingReviews = climbingMembers.stream()
            .filter(ClimbingMember::isHasShared)
            .map(climbingMember -> {
                Review review = reviewMap.get(climbingMember.getMember().getMemberId());
                // ReviewEmojiList
                List<ReviewEmoji> reviewEmojis = reviewEmojiService.findByReview(review);
                Map<Emoji, Long> emojiCounts = reviewEmojis.stream()
                    .collect(Collectors.groupingBy(ReviewEmoji::getEmoji, Collectors.counting()));
                List<ReviewEmojiListDto> reviewEmojiList = emojiCounts.entrySet().stream()
                    .map(entry -> new ReviewEmojiListDto(entry.getKey(),
                        entry.getValue().intValue()))
                    .collect(Collectors.toList());
                return ClimbingMemberReviewUnitDto.from(climbingMember, review, reviewEmojiList);
            })
            .collect(Collectors.toList());
        return new ClimbingReviewListResponseDto(climbingReviews);
    }


    public void addClimbingReviewEmoji(Long climbingId, Long reviewId,
        ClimbingReviewAddRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Climbing climbing = climbingService.getClimbingById(climbingId);
        Review review = reviewService.getReviewById(reviewId);
        ReviewEmoji reviewEmoji = requestDto.toEntity(currentMember, climbing, review);
        reviewEmojiService.save(reviewEmoji);
    }

    @Transactional(readOnly = true)
    public ReviewEmojiMemberListResponseDto getEmojiMemberList(Long climbingId, Long reviewId,
        Emoji emoji) {
        Review review = reviewService.getReviewById(reviewId);
        List<ReviewEmoji> reviewEmojis = reviewEmojiService.findByReviewAndEmoji(review, emoji);
        List<ReviewEmojiMemberUnitDto> reviewEmojiMembers = reviewEmojis.stream()
            .map(reviewEmoji -> ReviewEmojiMemberUnitDto.from(reviewEmoji.getMember()))
            .collect(Collectors.toList());
        return new ReviewEmojiMemberListResponseDto(reviewEmojiMembers);
    }
}
