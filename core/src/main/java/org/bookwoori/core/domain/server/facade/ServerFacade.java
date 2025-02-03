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
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.category.service.CategoryServiceImpl;
import org.bookwoori.core.domain.channel.dto.response.ChannelResponseDto;
import org.bookwoori.core.domain.channel.entity.Channel;
import org.bookwoori.core.domain.channel.service.ChannelServiceImpl;
import org.bookwoori.core.domain.member.entity.Member;
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
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.server.service.ServerServiceImpl;
import org.bookwoori.core.domain.serverMember.entity.ServerMember;
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

    /* TODO @crHwang0822 리팩토링 */
    @Transactional
    public ServerCreateResponseDto createServer(ServerCreateRequestDto requestDto) {
        //서버 생성
        Server server = requestDto.toEntity(
            s3Util.uploadImage(requestDto.serverImg(), "server"));
        serverService.save(server);

        //서버장 생성
        Member currentMember = memberService.getCurrentMember();
        ServerMember serverMember = ServerMember.builder()
            .server(server)
            .member(currentMember)
            .role(ServerRole.OWNER)
            .build();
        serverMemberService.save(serverMember);

        //기본 카테고리 및 채널 생성
        Category category = categoryService.makeDefaultCategory(server);
        channelService.makeDefaultChannels(category);
        return ServerCreateResponseDto.from(server);
    }

    @Transactional(readOnly = true)
    public ServerDetailsResponseDto getServerDetails(Long serverId) {
        Server server = serverService.getServerById(serverId);
        Member owner = serverMemberService.getOwner(server);
        int memberCount = serverMemberService.getMemberCount(server);
        Member currentMember = memberService.getCurrentMember();

        if (!serverMemberService.isJoined(currentMember, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        return ServerDetailsResponseDto.from(server, owner.getNickname(), memberCount,
            currentMember.equals(owner));
    }

    @Transactional(readOnly = true)
    public ServerMemberListResponseDto getServerMemberList(Long serverId) {
        Server server = serverService.getServerById(serverId);
        return new ServerMemberListResponseDto(serverMemberService.getAllMembersByServer(server));
    }

    @Transactional(readOnly = true)
    public ServerCategoryListResponseDto getServerCategoryList(Long serverId) {
        Server server = serverService.getServerById(serverId);
        List<Category> categories = sortCategory(categoryService.getCategoriesWithChannels(server));
        List<CategoryResponseDto> categoryDtoList = categories.stream()
            .map(category -> {
                List<ChannelResponseDto> channelDtoList = sortChannel(
                    category.getChannels()).stream()
                    .map(ChannelResponseDto::from).toList();
                return CategoryResponseDto.from(category, channelDtoList);
            }).collect(Collectors.toList());
        return new ServerCategoryListResponseDto(categoryDtoList);
    }

    private List<Category> sortCategory(List<Category> categories) {
        List<Category> sortedList = new ArrayList<>();
        Category currentCategory = categories.stream()
            .filter(category -> category.getBeforeNode() == null).findFirst().orElse(null);
        while (currentCategory != null) {
            sortedList.add(currentCategory);
            currentCategory = currentCategory.getNextNode();
        }
        return sortedList;
    }

    private List<Channel> sortChannel(List<Channel> channels) {
        List<Channel> sortedList = new ArrayList<>();
        Channel currentChannel = channels.stream()
            .filter(channel -> channel.getBeforeNode() == null).findFirst().orElse(null);
        while (currentChannel != null) {
            sortedList.add(currentChannel);
            currentChannel = currentChannel.getNextNode();
        }
        return sortedList;
    }

    @Transactional
    public String createInviteCode(Long serverId) {
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
    public InviteCodeServerResponseDto getServerByInviteCode(String inviteCode) {

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String value = ops.get("server:invitation:" + inviteCode);
        if (value == null) {
            throw new CustomException(ErrorCode.INVALID_INVITE_CODE);
        }
        Long serverId = Long.valueOf(value);

        Server server = serverService.getServerById(serverId);
        Member owner = serverMemberService.getOwner(server);
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

        Server server = serverService.getServerById(serverId);
        Member currentMember = memberService.getCurrentMember();

        boolean isJoined = serverMemberService.isJoined(currentMember, server);

        if (isJoined) {
            throw new CustomException(ErrorCode.ALREADY_JOINED_SERVER);
        } else {
            // 서버멤버 생성
            ServerMember serverMember = ServerMember.builder()
                .server(server)
                .member(currentMember)
                .role(ServerRole.MEMBER)
                .build();
            serverMemberService.save(serverMember);
        }
    }

    @Transactional(readOnly = true)
    public ServerListResponseDto getServerList() {
        Member member = memberService.getCurrentMember();
        List<Server> servers = serverMemberService.getServerListByMember(member);
        List<ServerItemDto> serverDtoList = servers.stream().map(ServerItemDto::from).toList();
        return new ServerListResponseDto(serverDtoList);
    }

    @Transactional
    public void leaveServer(Long serverId) {
        Member member = memberService.getCurrentMember();
        Server server = serverService.getServerById(serverId);

        if (serverMemberService.isOwner(member, server)) {
            throw new CustomException(ErrorCode.DELEGATION_REQUIRED);
        }

        serverMemberService.delete(server, member);
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

    @Transactional
    public void updateServerImage(Long serverId, MultipartFile newImage) {
        Member member = memberService.getCurrentMember();
        Server server = serverService.getServerById(serverId);

        if (!serverMemberService.isOwner(member, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        s3Util.deleteImage(server.getServerImg());

        server.updateServerImg(s3Util.uploadImage(newImage, "server"));
    }

    /* TODO @crHwang0822 리팩토링 */
    @Transactional
    public void delegateServerRole(Long serverId, ServerRoleDelegateRequestDto requestDto) {
        Server server = serverService.getServerById(serverId);
        Member currentMember = memberService.getCurrentMember();
        Member newOwner = memberService.getMemberById(requestDto.memberId());
        serverMemberService.delegateServerRole(server, currentMember, newOwner);
    }

    @Transactional
    public void deleteServer(Long serverId) {
        Server server = serverService.getServerById(serverId);
        Member currentMember = memberService.getCurrentMember();

        if (!serverMemberService.isOwner(currentMember, server)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        s3Util.deleteImage(server.getServerImg());
        serverService.delete(server);
    }
}
