package org.bookwoori.core.domain.xp;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.climbingMember.service.ClimbingMemberService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.record.dto.request.RecordRequestDto;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.service.RecordService;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.review.service.ReviewService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GrantXpAspect {

  private final RecordService recordService;
  private final MemberService memberService;
  private final ReviewService reviewService;
  private final ClimbingMemberService climbingMemberService;


  @Around("@annotation(grantXp) || @annotation(grantXpContainer)")
  public Object handleGrantXp(ProceedingJoinPoint joinPoint, GrantXp grantXp, GrantXpContainer grantXpContainer) throws Throwable {

    // 메서드 실행
    Object result = joinPoint.proceed();

    // @GrantXp가 여러 개일 경우 처리
    GrantXp[] grantXpAnnotations = grantXpContainer != null ? grantXpContainer.value() : new GrantXp[]{grantXp};

    for (GrantXp annotation : grantXpAnnotations) {
      XpType xpType = annotation.type();
      double xp = calculateXp(xpType, joinPoint.getArgs());

      if (xpType == XpType.FINISHED_CLIMBING){
        Climbing climbing = extractClimbing(joinPoint.getArgs());
        List<ClimbingMember> climbingMembers = climbingMemberService.getMembersByClimbing(climbing);
        for (ClimbingMember climbingMember : climbingMembers) {
          grantXpToMember(climbingMember.getMember(), xp);
        }
        log.info("[GrantXpAspect] FINISHED_CLIMBING 경험치 {}m 부여 완료. 참여 멤버 수: {}", xp, climbingMembers.size());
      }
      else{
        Member currentMember = memberService.getCurrentMember();
        grantXpToMember(currentMember, xp);
        log.info("[GrantXpAspect] 경험치 {}m 부여 완료. 현재 높이: {}m", xp, currentMember.getGrade().getHeight());
      }
    }

    return result;
  }

  public void grantXpToMember(Member member, double xp) {
    if (member == null) {
      throw new IllegalArgumentException("Member cannot be null");
    }
    member.updateHeight(xp);
  }


  private double calculateXp(XpType xpType, Object[] args) throws CustomException {
    switch (xpType) {
      case FINISHED_CLIMBING: {
        Climbing climbing = extractClimbing(args);
        if (climbing != null && climbing.getStatus() == ClimbingStatus.FINISHED) {
          int totalPages = climbing.getBook().getItemPage();
          return totalPages * 0.1;
        }
        break;
      }
      case READ_PAGE: {
        RecordRequestDto requestDto = extractRecordRequestDto(args);
        Long recordId = extractRecordId(args);
        if (recordId == null) {
          // createRecord
          return requestDto.currentPage() * 0.1;
        } else {
          // updateRecord
          Record existingRecord = recordService.getRecordById(recordId);
          int previousPage = existingRecord.getMaxPage();
          int newPage = requestDto.currentPage();
          return Math.max(newPage - previousPage, 0) * 0.1;
        }
      }
      case ADD_STAR: {
        RecordRequestDto requestDto = extractRecordRequestDto(args);
        Long recordId = extractRecordId(args);
        if (recordId == null) {
          if (requestDto.star() > 0) return 3.0;
        } else {
          Record existingRecord = recordService.getRecordById(recordId);
          int previousStar = existingRecord.getStar();
          int newStar = requestDto.star();
          if (previousStar == 0 && newStar > 0) return 3.0;
        }
        break;
      }
      case WRITE_REVIEW: {
        RecordRequestDto requestDto = extractRecordRequestDto(args);
        Long recordId = extractRecordId(args);

        if (recordId == null) {
          if (!requestDto.reviewContent().isBlank()) return 2.0;
        } else {
          Optional<Review> existingReview = reviewService.getReviewByRecordId(recordId);
          if (!existingReview.isPresent() && !requestDto.reviewContent().isBlank()) return 2.0;
        }
        break;
      }
      default:
        throw new CustomException(ErrorCode.INVALID_XPTYPE);
    }
    return 0.0;
  }

  private RecordRequestDto extractRecordRequestDto(Object[] args) {
    for (Object arg : args) {
      if (arg instanceof RecordRequestDto) {
        return (RecordRequestDto) arg;
      }
    }
    throw new CustomException(ErrorCode.INVALID_ARGUMENT);
  }

  private Long extractRecordId(Object[] args) {
    for (Object arg : args) {
      if (arg instanceof Long) {
        return (Long) arg;
      }
    }
    return null;
  }

  private Climbing extractClimbing(Object[] args) {
    for (Object arg : args) {
      if (arg instanceof Climbing) {
        return (Climbing) arg;
      }
    }
    throw new CustomException(ErrorCode.INVALID_ARGUMENT);
  }
}
