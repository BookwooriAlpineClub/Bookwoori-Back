package org.bookwoori.core.domain.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.record.dto.request.RecordRequestDto;
import org.bookwoori.core.domain.record.facade.RecordFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "책 기록 수정", description = "수정 페이지_책기록 에서 기존 책기록을 수정합니다.")
    @PutMapping("/{recordId}")
    public ResponseEntity<?> updateRecord(@PathVariable Long recordId,
        @RequestBody RecordRequestDto requestDto) {
        return ResponseEntity.ok(recordFacade.updateRecord(recordId, requestDto));
    }

    @Operation(summary = "책 기록 삭제", description = "책기록을 삭제합니다.")
    @DeleteMapping("/{recordId}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long recordId) {
        recordFacade.deleteRecord(recordId);
        return ResponseEntity.ok().build();
    }
}
