package org.bookwoori.core.domain.exp.aspect;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.book.service.BookService;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.climbingMember.service.ClimbingMemberService;
import org.bookwoori.core.domain.exp.entity.ExpType;
import org.bookwoori.core.domain.exp.annotation.GrantExp;
import org.bookwoori.core.domain.exp.annotation.GrantExpContainer;
import org.bookwoori.core.domain.exp.service.ExpService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.record.dto.request.RecordRequestDto;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.service.RecordService;
import org.bookwoori.core.domain.review.dto.request.ReviewRequestDto;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.review.service.ReviewService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GrantExpAspect {

  private final RecordService recordService;
  private final MemberService memberService;
  private final ReviewService reviewService;
  private final ClimbingMemberService climbingMemberService;
  private final ExpService expService;
  private final BookService bookService;

  @Around("@annotation(grantExpContainer)")
  public Object handleGrantXp(ProceedingJoinPoint joinPoint, GrantExpContainer grantExpContainer) throws Throwable {

    // @GrantXp가 여러 개일 경우 처리
    GrantExp[] grantExpAnnotations = grantExpContainer.value();

    for (GrantExp annotation : grantExpAnnotations) {
      ExpType expType = annotation.type();
      double exp = calculateXp(expType, joinPoint.getArgs());
      if(exp != 0) {
          if (expType == ExpType.FINISHED_CLIMBING) {
            Climbing climbing = extractClimbing(joinPoint.getArgs());
            List<ClimbingMember> climbingMembers = climbingMemberService.getMembersByClimbing(
                climbing);
            for (ClimbingMember climbingMember : climbingMembers) {
              expService.grantExpToMember(climbingMember.getMember(), expType, exp, climbing.getBook().getTitle());
            }
          } else if (expType == ExpType.READ_PAGE) {
            RecordRequestDto requestDto = extractRecordRequestDto(joinPoint.getArgs());
            Book book = bookService.getOrCreateBookByIsbn(requestDto.isbn13());
            Member currentMember = memberService.getCurrentMember();
            expService.grantExpToMember(currentMember, expType, exp, book.getTitle());
            currentMember.updatePage(exp*10);
          } else {
            ReviewRequestDto requestDto = extractReviewRequestDto(joinPoint.getArgs());
            Record record = recordService.getRecordById(requestDto.recordId());
            Member currentMember = memberService.getCurrentMember();
            expService.grantExpToMember(currentMember, expType, exp, record.getBook().getTitle());
          }
      }
    }
    // 메서드 실행
    Object result = joinPoint.proceed();
    return result;
  }

  private double calculateXp(ExpType expType, Object[] args) throws CustomException {
    switch (expType) {
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
          Record record = recordService.getRecordById(recordId);
          int previousPage = record.getMaxPage();
          int newPage = requestDto.currentPage();
          return Math.max(newPage - previousPage, 0) * 0.1;
        }
      }
      case ADD_STAR: {
        ReviewRequestDto requestDto = extractReviewRequestDto(args);
        Long reviewId = extractReviewId(args);
        if (reviewId == null){
          if (requestDto.star() > 0) return 3.0;
        } else{
          Review review = reviewService.getReviewById(reviewId);
          int previousStar = review.getStar();
          int newStar = requestDto.star();
          if (previousStar == 0 && newStar > 0) return 3.0;
        }
        break;
      }
      case WRITE_REVIEW: {
        ReviewRequestDto requestDto = extractReviewRequestDto(args);
        Long reviewId = extractReviewId(args);
        if (reviewId == null) {
          if (requestDto.content() != null) return 2.0;
        } else {
          Review review = reviewService.getReviewById(reviewId);
          if (review.getContent().isEmpty() && requestDto.content() != null) return 2.0;
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

  private ReviewRequestDto extractReviewRequestDto(Object[] args) {
    for (Object arg : args) {
      if (arg instanceof ReviewRequestDto) {
        return (ReviewRequestDto) arg;
      }
    }
    throw new CustomException(ErrorCode.INVALID_ARGUMENT);
  }

  private Long extractReviewId(Object[] args) {
    for (Object arg : args) {
      if (arg instanceof Long) {
        return (Long) arg;
      }
    }
    return null;
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
