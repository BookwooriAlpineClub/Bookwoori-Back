package org.bookwoori.core.domain.server.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.category.dto.response.CategoryResponseDto;
import org.bookwoori.core.domain.category.infrastructure.CategoryEntity;
import org.bookwoori.core.domain.category.service.CategoryServiceImpl;
import org.bookwoori.core.domain.channel.dto.response.ChannelResponseDto;
import org.bookwoori.core.domain.channel.infrastructure.ChannelEntity;
import org.bookwoori.core.domain.channel.service.ChannelServiceImpl;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.member.service.MemberServiceImpl;
import org.bookwoori.core.domain.server.dto.request.ServerCreateRequestDto;
import org.bookwoori.core.domain.server.dto.request.ServerInfoUpdateRequestDto;
import org.bookwoori.core.domain.server.dto.request.ServerRoleDelegateRequestDto;
import org.bookwoori.core.domain.server.dto.response.InviteCodeServerResponseDto;
import org.bookwoori.core.domain.server.dto.response.ServerCategoryListResponseDto;
import org.bookwoori.core.domain.server.dto.response.ServerCreateResponseDto;
import org.bookwoori.core.domain.server.dto.response.ServerDetailsResponseDto;
import org.bookwoori.core.domain.server.dto.response.ServerItemDto;
import org.bookwoori.core.domain.server.dto.response.ServerListResponseDto;
import org.bookwoori.core.domain.server.dto.response.ServerMemberListResponseDto;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;
import org.bookwoori.core.domain.server.service.ServerServiceImpl;
import org.bookwoori.core.domain.serverMember.entity.ServerRole;
import org.bookwoori.core.domain.serverMember.service.ServerMemberServiceImpl;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.bookwoori.core.global.s3.S3Util;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Log4j2
public class ServerFacade {

    private final S3Util s3Util;
    private final StringRedisTemplate redisTemplate;

    private final ServerServiceImpl serverService;
    private final MemberServiceImpl memberService;
    private final CategoryServiceImpl categoryService;
    private final ChannelServiceImpl channelService;
    private final ServerMemberServiceImpl serverMemberService;

    @Transactional
    public ServerCreateResponseDto createServer(ServerCreateRequestDto requestDto) {
        //서버 저장
        ServerEntity server = requestDto.toEntity(
            s3Util.uploadImage(requestDto.serverImg(), "server"));
        serverService.saveServer(server);

        //로그인한 유저 정보 불러오기 - 임시로 작성, 이후 수정 필요
        MemberEntity member = memberService.getCurrentMember();
        //서버장을 ServerMember 테이블에 추가
        serverMemberService.saveServerMember(member, server, ServerRole.OWNER);

        //DEFAULT 카테고리/채널 생성 및 저장
        CategoryEntity category = categoryService.makeDefaultCategory(server);
        channelService.makeDefaultChannels(category);
        return ServerCreateResponseDto.from(server);
    }

    @Transactional(readOnly = true)
    public ServerDetailsResponseDto getServerDetails(Long serverId) {
        ServerEntity server = serverService.getServerById(serverId);
        MemberEntity owner = serverMemberService.getOwner(server);
        int memberCount = serverMemberService.getMemberCount(server);
        MemberEntity currentMember = memberService.getCurrentMember();

        if (!serverMemberService.isJoined(currentMember, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        return ServerDetailsResponseDto.from(server, owner.getNickname(), memberCount,
            currentMember.equals(owner));
    }

    @Transactional(readOnly = true)
    public ServerMemberListResponseDto getServerMemberList(Long serverId) {
        ServerEntity server = serverService.getServerById(serverId);
        return new ServerMemberListResponseDto(serverMemberService.getAllMembersByServer(server));
    }

    @Transactional(readOnly = true)
    public ServerCategoryListResponseDto getServerCategoryList(Long serverId) {
        ServerEntity server = serverService.getServerById(serverId);
        List<CategoryEntity> categories = sortCategory(
            categoryService.getCategoriesWithChannels(server));
        List<CategoryResponseDto> categoryDtoList = categories.stream()
            .map(category -> {
                List<ChannelResponseDto> channelDtoList = sortChannel(
                    category.getChannels()).stream()
                    .map(ChannelResponseDto::from).toList();
                return CategoryResponseDto.from(category, channelDtoList);
            }).collect(Collectors.toList());
        return new ServerCategoryListResponseDto(categoryDtoList);
    }

    private List<CategoryEntity> sortCategory(List<CategoryEntity> categories) {
        List<CategoryEntity> sortedList = new ArrayList<>();
        CategoryEntity currentCategory = categories.stream()
            .filter(category -> category.getBeforeNode() == null).findFirst().orElse(null);
        while (currentCategory != null) {
            sortedList.add(currentCategory);
            currentCategory = currentCategory.getNextNode();
        }
        return sortedList;
    }

    private List<ChannelEntity> sortChannel(List<ChannelEntity> channels) {
        List<ChannelEntity> sortedList = new ArrayList<>();
        ChannelEntity currentChannel = channels.stream()
            .filter(channel -> channel.getBeforeNode() == null).findFirst().orElse(null);
        while (currentChannel != null) {
            sortedList.add(currentChannel);
            currentChannel = currentChannel.getNextNode();
        }
        return sortedList;
    }

    @Transactional
    public Object createInviteCode(Long serverId) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String uuid = UUID.randomUUID().toString().replace("-", "");

        Random random = new Random();
        int length = 10 + random.nextInt(3); // 길이 10-12
        int startIndex = random.nextInt(uuid.length() - length);
        int endIndex = startIndex + length;

        String inviteCode = uuid.substring(startIndex, endIndex);

        ops.set("server:invitation:" + inviteCode, String.valueOf(serverId), 1,
            TimeUnit.DAYS); // Redis에 저장, TTL 1일
        return inviteCode;

    }

    @Transactional(readOnly = true)
    public Object getServerByInviteCode(String inviteCode) {

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String value = ops.get("server:invitation:" + inviteCode);
        if (value == null) {
            throw new CustomException(ErrorCode.INVALID_INVITE_CODE);
        }
        Long serverId = Long.valueOf(value);

        ServerEntity server = serverService.getServerById(serverId);
        MemberEntity owner = serverMemberService.getOwner(server);
        int memberCount = serverMemberService.getMemberCount(server);

        return InviteCodeServerResponseDto.from(server, owner.getNickname(), memberCount);

    }

    @Transactional
    public void createServerMember(String inviteCode) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String value = ops.get("server:invitation:" + inviteCode);
        if (value == null) {
            throw new CustomException(ErrorCode.INVALID_INVITE_CODE);
        }
        Long serverId = Long.valueOf(value);

        ServerEntity server = serverService.getServerById(serverId);
        MemberEntity currentMember = memberService.getCurrentMember();

        boolean isJoined = serverMemberService.isJoined(currentMember, server);

        if (isJoined) {
            throw new CustomException(ErrorCode.ALREADY_JOINED_SERVER);
        } else {
            serverMemberService.saveServerMember(currentMember, server,
                ServerRole.MEMBER); // 서버멤버 생성
        }
    }

    @Transactional(readOnly = true)
    public ServerListResponseDto getServerList() {
        MemberEntity member = memberService.getCurrentMember();
        List<ServerEntity> servers = serverMemberService.getServerListByMember(member);
        List<ServerItemDto> serverDtoList = servers.stream().map(ServerItemDto::from).toList();
        return new ServerListResponseDto(serverDtoList);
    }

    @Transactional
    public void leaveServer(Long serverId) {
        MemberEntity member = memberService.getCurrentMember();
        ServerEntity server = serverService.getServerById(serverId);

        if (serverMemberService.isOwner(member, server)) {
            throw new CustomException(ErrorCode.DELEGATION_REQUIRED);
        }

        serverMemberService.deleteServerMember(server, member);
    }

    @Transactional
    public void updateServerInfo(Long serverId, ServerInfoUpdateRequestDto requestDto) {
        MemberEntity member = memberService.getCurrentMember();
        ServerEntity server = serverService.getServerById(serverId);

        if (!serverMemberService.isOwner(member, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        server.updateInfo(requestDto.name(), requestDto.description());
    }

    @Transactional
    public void updateServerImage(Long serverId, MultipartFile newImage) {
        MemberEntity member = memberService.getCurrentMember();
        ServerEntity server = serverService.getServerById(serverId);

        if (!serverMemberService.isOwner(member, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        s3Util.deleteImage(server.getServerImg());

        server.updateServerImg(s3Util.uploadImage(newImage, "server"));
    }

    @Transactional
    public void delegateServerRole(Long serverId, ServerRoleDelegateRequestDto requestDto) {
        ServerEntity server = serverService.getServerById(serverId);
        MemberEntity currentMember = memberService.getCurrentMember();
        MemberEntity newOwner = memberService.getMemberById(requestDto.memberId());
        serverMemberService.delegateServerRole(server, currentMember, newOwner);
    }

    @Transactional
    public void deleteServer(Long serverId) {
        ServerEntity server = serverService.getServerById(serverId);
        MemberEntity currentMember = memberService.getCurrentMember();

        if (!serverMemberService.isOwner(currentMember, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        s3Util.deleteImage(server.getServerImg());
        serverService.deleteServer(server);
    }
}
