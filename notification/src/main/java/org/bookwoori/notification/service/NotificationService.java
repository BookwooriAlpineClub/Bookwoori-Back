package org.bookwoori.notification.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.notification.domain.Device;
import org.bookwoori.notification.domain.Notification;
import org.bookwoori.notification.dto.request.ChannelMessageRequestDto;
import org.bookwoori.notification.dto.request.ChatMessageRequestDto;
import org.bookwoori.notification.dto.request.DirectMessageRequestDto;
import org.bookwoori.notification.dto.request.EmojiMessageRequestDto;
import org.bookwoori.notification.dto.response.DeviceTokenResponseDto;
import org.bookwoori.notification.exception.CustomException;
import org.bookwoori.notification.repository.DeviceRepository;
import org.bookwoori.notification.util.FcmUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static org.bookwoori.notification.exception.CustomExceptionStatus.REDIS_DEVICE_TOKEN_PARSE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static ConcurrentHashMap<Long, List<MulticastMessage>> job = new ConcurrentHashMap<>();
    private final FcmUtil fcm;
    private final DeviceRepository deviceRepository;
    private final RedisTemplate redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;
    private final String REDIS_DEVICE_KEY_PREFIX = "USERID:";

    public void send(DirectMessageRequestDto request) {
        Map<Long, DeviceTokenResponseDto> deviceTokens = getDeviceTokens(request.target());
        Map<String, List<String>> targetTokensByPlatform = getTokensByPlatform(deviceTokens);
        for (Entry<String, List<String>> platform : targetTokensByPlatform.entrySet()) {
            sendMessage(platform.getValue(), request, platform.getKey());
        }
        saveLog(request, targetTokensByPlatform);
    }

    public void send(ChannelMessageRequestDto request) {
        Map<Long, DeviceTokenResponseDto> deviceTokens = getDeviceTokens(request.target());
        Map<String, List<String>> targetTokensByPlatform = getTokensByPlatform(deviceTokens);
        for (Entry<String, List<String>> platform : targetTokensByPlatform.entrySet()) {
            sendMessage(platform.getValue(), request, platform.getKey());
        }
        saveLog(request, targetTokensByPlatform);
    }

    public void send(EmojiMessageRequestDto request) {
        Map<Long, DeviceTokenResponseDto> deviceTokens = getDeviceTokens(request.target());
        Map<String, List<String>> targetTokensByPlatform = getTokensByPlatform(deviceTokens);
        for (Entry<String, List<String>> platform : targetTokensByPlatform.entrySet()) {
            sendMessage(platform.getValue(), request, platform.getKey());
        }
        saveLog(request, targetTokensByPlatform);
    }

    public void send(ChatMessageRequestDto request) {
        Map<Long, DeviceTokenResponseDto> deviceTokens = getDeviceTokens(request.target());
        Map<String, List<String>> targetTokensByPlatform = getTokensByPlatform(deviceTokens);
        for (Entry<String, List<String>> platform : targetTokensByPlatform.entrySet()) {
            sendMessage(platform.getValue(), request, platform.getKey());
        }
        saveLog(request, targetTokensByPlatform);
    }


    // 토큰 조회 시 캐시 먼저 조회하고 없는 정보만 DB에서 조회하기
    private Map<Long, DeviceTokenResponseDto> getDeviceTokens(String target) {
        Map<Long, DeviceTokenResponseDto> deviceTokens = new HashMap<>();

        List<Long> originIds = convertStringToList(target);
        List<Long> filterIds = new ArrayList<>();

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        originIds.forEach(id -> {
            String key = REDIS_DEVICE_KEY_PREFIX + id.toString();
            String value = valueOperations.get(key);
            if (Objects.isNull(value)) {
                filterIds.add(id);
            } else {
                try {
                    deviceTokens.put(id, objectMapper.readValue(value, DeviceTokenResponseDto.class));
                } catch (JsonProcessingException e) {
                    throw new CustomException(REDIS_DEVICE_TOKEN_PARSE_ERROR);
                }
            }
        });

        List<Device> devices = deviceRepository.findByMemberIdList(filterIds);
        devices.forEach(device -> {
            String setKey = REDIS_DEVICE_KEY_PREFIX + device.getMemberId().toString();
            DeviceTokenResponseDto response = DeviceTokenResponseDto.from(device);
            valueOperations.set(setKey, response.toString());
            deviceTokens.put(device.getId(), response);
        });
        return deviceTokens;

    }

    // 플랫폼 별로 디바이스 토큰 나누기
    private Map<String, List<String>> getTokensByPlatform(Map<Long, DeviceTokenResponseDto> deviceTokens) {
        Map<String, List<String>> targetTokensByPlatform = new HashMap<>();
        for (Entry<Long, DeviceTokenResponseDto> entry : deviceTokens.entrySet()) {
            List<String> tokens = targetTokensByPlatform.get(entry.getValue().platform());
            if (Objects.isNull(tokens))
                tokens = new ArrayList<>();
            tokens.add(entry.getValue().token());
            targetTokensByPlatform.put(entry.getValue().platform().toString(), tokens);
        }
        return targetTokensByPlatform;
    }

    // Stirng "[1, 2, 3]" -> List [1, 2, 3]
    private List<Long> convertStringToList(String target) {
        if (target.length() <= 2)
            return new ArrayList<Long>();
        else {
            target = target.substring(1, target.length() - 1);
            String[] split = target.split(", ");

            List<Long> ids = new ArrayList<>();
            for (String s : split) {
                ids.add(Long.parseLong(s));
            }
            return ids;
        }
    }

    private void sendMessage(List<String> targetTokens, DirectMessageRequestDto request, String platform) {
        try {
            MulticastMessage msg = fcm.makeMessage(
                    targetTokens,
                    fcm.makeTitle(request.nickname(), request.roomName()),
                    fcm.makeBody(request.type(), request.content()),
                    platform,
                    fcm.makeCustomData(null, request.roomId())
            );
            fcm.sendMessage(msg);
        } catch (FirebaseMessagingException e) {
            log.error("FIREBASE ERROR : {}", e);
        }
    }

    private void sendMessage(List<String> targetTokens, ChannelMessageRequestDto request, String platform) {
        try {
            MulticastMessage msg = fcm.makeMessage(
                    targetTokens,
                    fcm.makeTitle(request.nickname(), request.channelName()),
                    fcm.makeBody(request.type(), request.content()),
                    platform,
                    fcm.makeCustomData(request.communityId(), request.channelId())
            );
            fcm.sendMessage(msg);
        } catch (FirebaseMessagingException e) {
            log.error("FIREBASE ERROR : {}", e);
        }
    }

    private void sendMessage(List<String> targetTokens, EmojiMessageRequestDto request, String platform) {
        try {
            MulticastMessage msg = fcm.makeMessage(
                    targetTokens,
                    fcm.makeTitle(request.nickname(), null),
                    fcm.makeBody(request.type(), request.content()),
                    platform,
                    fcm.makeCustomData(null, null)
            );
            fcm.sendMessage(msg);
        } catch (FirebaseMessagingException e) {
            log.error("FIREBASE ERROR : {}", e);
        }
    }

    private void sendMessage(List<String> targetTokens, ChatMessageRequestDto request, String platform) {
        try {
            MulticastMessage msg = fcm.makeMessage(
                    targetTokens,
                    fcm.makeTitle(request.nickname(), request.channelName()),
                    fcm.makeBody(request.type(), request.content()),
                    platform,
                    fcm.makeCustomData(request.communityId(), request.channelId())
            );
            fcm.sendMessage(msg);
        } catch (FirebaseMessagingException e) {
            log.error("FIREBASE ERROR : {}", e);
        }
    }

    private void saveLog(DirectMessageRequestDto request, Map<String, List<String>> target) {
        Notification notification = new Notification(
                request.memberId().toString(),
                request.nickname(),
                request.type(),
                request.content(),
                request.roomId().toString(),
                request.roomName(),
                request.target()
        );
        mongoTemplate.insert(notification);
    }

    private void saveLog(ChannelMessageRequestDto request, Map<String, List<String>> target) {
        Notification notification = new Notification(
                request.memberId().toString(),
                request.nickname(),
                request.type(),
                request.content(),
                request.channelId().toString(),
                request.channelName(),
                request.target()
        );
        mongoTemplate.insert(notification);
    }

    private void saveLog(EmojiMessageRequestDto request, Map<String, List<String>> target) {

        Notification notification = new Notification(
                request.memberId().toString(),
                request.nickname(),
                request.type(),
                request.content(),
                request.reviewId().toString(),
                null,
                request.target()
        );
        mongoTemplate.insert(notification);

    }

    private void saveLog(ChatMessageRequestDto request, Map<String, List<String>> target) {
        Notification notification = new Notification(
                request.memberId().toString(),
                request.nickname(),
                request.type(),
                request.content(),
                request.channelId().toString(),
                request.channelName(),
                request.target()
        );
        mongoTemplate.insert(notification);
    }


}
