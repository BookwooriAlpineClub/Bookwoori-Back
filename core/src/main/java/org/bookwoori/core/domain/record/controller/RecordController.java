package org.bookwoori.core.domain.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.record.dto.request.RecordRequestDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.facade.RecordFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Record")
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {
    private final RecordFacade recordFacade;

    @Operation(summary = "책 기록 추가", description = "수정 페이지_책기록 에서 새로운 책기록을 추가합니다.")
    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody RecordRequestDto requestDto) {
        recordFacade.createRecord(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "책 감상평 추가", description = "수정 페이지_책기록 에서 새로운 감상평을 추가합니다.")
    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@RequestBody RecordRequestDto requestDto) {
        recordFacade.createReview(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "책 기록 수정", description = "수정 페이지_책기록 에서 기존 책기록을 수정합니다.")
    @PutMapping("/{recordId}")
    public ResponseEntity<?> updateRecord(@PathVariable Long recordId,
                                          @RequestBody RecordRequestDto requestDto) {
        recordFacade.updateRecord(recordId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "책 감상평 수정", description = "수정 페이지_책기록 에서 기존 감상평을 수정합니다.")
    @PutMapping("/reviews/{recordId}")
    public ResponseEntity<?> updateReview(@PathVariable Long recordId,
                                          @RequestBody RecordRequestDto requestDto) {
        recordFacade.updateReview(recordId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "책 기록 삭제", description = "책기록을 삭제합니다.")
    @DeleteMapping("/{recordId}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long recordId) {
        recordFacade.deleteRecordAndReview(recordId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "책 기록 목록 조회", description = "내 서재의 책기록 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getRecordsByStatus(@RequestParam ReadingStatus status) {
        return ResponseEntity.ok(recordFacade.getRecordsByStatus(status));
    }

    @Operation(summary = "책 평가 목록 조회", description = "내 서재의 책평가 목록을 조회합니다.")
    @GetMapping("/reviews")
    public ResponseEntity<?> getReviews() {
        return ResponseEntity.ok(recordFacade.getReviews());
    }
}