package org.bookwoori.core.domain.exp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.exp.facade.ExpFacade;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Exp")
@RequiredArgsConstructor
@RequestMapping("/expLog")
public class ExpController {

  private final ExpFacade expFacade;

  @Operation(summary = "경험치 기록 목록 조회", description = "경험치 기록 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<?> getExpLogs() {
    return ResponseEntity.ok(expFacade.getExpLogs());
  }



}
