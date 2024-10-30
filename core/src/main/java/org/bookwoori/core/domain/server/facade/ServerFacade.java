package org.bookwoori.core.domain.server.facade;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.category.dto.response.CategoryResponseDto;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.category.service.CategoryService;
import org.bookwoori.core.domain.channel.dto.response.ChannelResponseDto;
import org.bookwoori.core.domain.channel.service.ChannelService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.server.dto.request.ServerCreateRequestDto;
import org.bookwoori.core.domain.server.dto.request.ServerInfoUpdateRequestDto;
import org.bookwoori.core.domain.server.dto.response.ServerCategoryListResponseDto;
import org.bookwoori.core.domain.server.dto.response.ServerDetailsResponseDto;
import org.bookwoori.core.domain.server.dto.response.ServerListResponseDto;
import org.bookwoori.core.domain.server.dto.response.ServerMemberListResponseDto;
import org.bookwoori.core.domain.server.dto.response.ServerResponseDto;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.server.service.ServerService;
import org.bookwoori.core.domain.serverMember.entity.ServerRole;
import org.bookwoori.core.domain.serverMember.repository.ServerMemberRepository;
import org.bookwoori.core.domain.serverMember.service.ServerMemberService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.bookwoori.core.global.s3.S3Util;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
public class ServerFacade {

    private final S3Util s3Util;
    private final StringRedisTemplate redisTemplate;

    private final ServerService serverService;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final ChannelService channelService;
    private final ServerMemberService serverMemberService;
    private final ServerMemberRepository serverMemberRepository;

    @Transactional
    public void createServer(ServerCreateRequestDto requestDto) {

        //서버 저장
        Server server = requestDto.toEntity(
            s3Util.uploadImage(requestDto.serverImg(), "server"));
        serverService.saveServer(server);

        //로그인한 유저 정보 불러오기 - 임시로 작성, 이후 수정 필요
        Member member = memberService.getCurrentMember();
        //서버장을 ServerMember 테이블에 추가
        serverMemberService.saveServerMember(member, server, ServerRole.OWNER);

        //DEFAULT 카테고리/채널 생성 및 저장
        Category category = categoryService.makeDefaultCategory(server);
        channelService.makeDefaultChannels(category);
    }

    public ServerDetailsResponseDto getServerDetails(Long serverId) {
        Server server = serverService.getServerById(serverId);
        Member owner = serverMemberService.getOwner(server);
        int memberCount = serverMemberService.getMemberCount(server);

        return ServerDetailsResponseDto.from(server, owner.getNickname(), memberCount);
    }

    public ServerMemberListResponseDto getServerMemberList(Long serverId) {
        Server server = serverService.getServerById(serverId);
        return new ServerMemberListResponseDto(serverMemberService.getAllMembersByServer(server));
    }

    public ServerCategoryListResponseDto getServerCategoryList(Long serverId) {
        Server server = serverService.getServerById(serverId);
        List<Category> categories = categoryService.getCategoriesWithChannels(server);
        List<CategoryResponseDto> categoryDtoList = categories.stream()
            .map(category -> {
                List<ChannelResponseDto> channelDtoList = category.getChannels().stream()
                    .map(ChannelResponseDto::from).toList();
                return CategoryResponseDto.from(category, channelDtoList);
            }).collect(Collectors.toList());
        return new ServerCategoryListResponseDto(categoryDtoList);
    }


    public String getOrCreateInviteCode(Long serverId) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String inviteCode = ops.get(String.valueOf(serverId));

        if (inviteCode != null) { // 이미 존재하는 경우
            return inviteCode;
        } else { // 존재하지 않는 경우
            inviteCode = UUID.randomUUID().toString(); // 새로 생성
            ops.set(String.valueOf(serverId), inviteCode, 7, TimeUnit.DAYS); // Redis에 저장, TTL 7일
            return inviteCode;
        }
    }

    public void createServerMember(String inviteCode) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
//        System.out.println(ops.get(inviteCode)); // 디버깅용
        Server server = serverService.getServerById(Long.valueOf(ops.get(inviteCode)));
        //로그인한 유저 정보 불러오기 - 임시로 작성, 이후 수정 필요
        Member currentMember = memberService.getCurrentMember();
//        Member member = memberService.getMemberById(1L); // 테스트용
        boolean isJoined = serverMemberRepository.findByMemberAndServer(currentMember, server)
            .isPresent();

        if (isJoined) {
            throw new CustomException(ErrorCode.ALREADY_JOINED_SERVER);
        } else {
            serverMemberService.saveServerMember(currentMember, server,
                ServerRole.MEMBER); // 서버멤버 생성

        }
    }


    public ServerListResponseDto getServerList() {
        Member member = memberService.getCurrentMember();
        List<Server> servers = serverMemberService.getServerListByMember(member);
        List<ServerResponseDto> serverDtoList = servers.stream().map(ServerResponseDto::from)
            .toList();
        return new ServerListResponseDto(serverDtoList);
    }

    @Transactional
    public void leaveServer(Long serverId) {
        Member member = memberService.getCurrentMember();
        Server server = serverService.getServerById(serverId);
        serverMemberService.deleteServerMember(server, member);
    }

    @Transactional
    public void updateServerInfo(Long serverId, ServerInfoUpdateRequestDto requestDto) {
        Member member = memberService.getCurrentMember();
        Server server = serverService.getServerById(serverId);

        if (!serverMemberService.isOwner(member, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        server.updateInfo(requestDto.name(), requestDto.description());
    }
}
