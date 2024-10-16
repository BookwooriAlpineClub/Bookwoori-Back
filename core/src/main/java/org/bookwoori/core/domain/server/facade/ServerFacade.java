package org.bookwoori.core.domain.server.facade;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.category.service.CategoryService;
import org.bookwoori.core.domain.channel.service.ChannelService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.server.dto.request.ServerCreateRequestDto;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.server.service.ServerService;
import org.bookwoori.core.domain.serverMember.entity.ServerRole;
import org.bookwoori.core.domain.serverMember.service.ServerMemberService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ServerFacade {

    private final ServerService serverService;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final ChannelService channelService;
    private final ServerMemberService serverMemberService;

    @Transactional
    public void createServer(ServerCreateRequestDto requestDto) {

        //서버 이미지 저장 - 추가 예정

        //서버 저장
        Server server = requestDto.toEntity();
        serverService.saveServer(server);

        //로그인한 유저 정보 불러오기 - 임시로 작성, 이후 수정 필요
        Member member = memberService.getMemberById(1L);
        //서버장을 ServerMember 테이블에 추가
        serverMemberService.saveServerMember(member, server, ServerRole.OWNER);

        //DEFAULT 카테고리/채널 생성 및 저장
        Category category = categoryService.makeDefaultCategory(server);
        channelService.makeDefaultChannels(category);

    }
}
