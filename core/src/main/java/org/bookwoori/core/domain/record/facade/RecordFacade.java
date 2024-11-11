package org.bookwoori.core.domain.record.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.record.dto.request.RecordRequestDto;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.service.RecordService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
public class RecordFacade {

    private final RecordService recordService;
    private final MemberService memberService;

    @Transactional
    public void createRecord(RecordRequestDto requestDto) {

        Member currentMember = memberService.getCurrentMember();
        Record record = requestDto.toEntity(currentMember);
        recordService.saveRecord(record);
    }

    @Transactional
    public Record updateRecord(Long recordId, RecordRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Record record = recordService.getRecordById(recordId);
        return record.updateRecord(requestDto.toEntity(currentMember));
    }

    @Transactional
    public void deleteRecord(Long recordId) {
        recordService.deleteRecord(recordId);
    }


}
