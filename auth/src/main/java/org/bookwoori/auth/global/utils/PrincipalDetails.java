package org.bookwoori.auth.global.utils;

import org.bookwoori.auth.global.feignClient.dto.GetMemberResponseDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public record PrincipalDetails(
    Map<String, Object> attributes,
    String attributeKey,
    GetMemberResponseDto responseDto) implements OAuth2User, UserDetails {

    public GetMemberResponseDto getMemberResponseDto() {
        return responseDto;
    }

    @Override
    public String getName() {
        return attributes.get(attributeKey).toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(responseDto.memberId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

