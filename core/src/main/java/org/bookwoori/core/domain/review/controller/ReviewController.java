package org.bookwoori.core.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.review.dto.request.ReviewRequestDto;
import org.bookwoori.core.domain.review.facade.ReviewFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Review")
@RequiredArgsConstructor
@RequestMapping("/records/{recordId}/reviews")
public class ReviewController {

  private final ReviewFacade reviewFacade;

  @Operation(summary = "책 감상평 추가", description = "수정 페이지_책기록 에서 새로운 감상평을 추가합니다.")
  @PostMapping
  public ResponseEntity<?> createReview(@PathVariable Long recordId, @RequestBody @Valid ReviewRequestDto requestDto) {
    reviewFacade.createReview(recordId, requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "책 감상평 수정", description = "수정 페이지_책기록 에서 기존 감상평을 수정합니다.")
  @PatchMapping("/{reviewId}")
  public ResponseEntity<?> updateReview(@PathVariable Long reviewId,
      @RequestBody @Valid ReviewRequestDto requestDto) {
    reviewFacade.updateReview(reviewId, requestDto);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "책 감상평 삭제", description = "수정 페이지_책기록 에서 기존 감상평을 삭제합니다.")
  @DeleteMapping
  public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
    reviewFacade.deleteReview(reviewId);
    return ResponseEntity.ok().build();
  }

}
