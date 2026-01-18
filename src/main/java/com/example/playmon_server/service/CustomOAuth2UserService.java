package com.example.playmon_server.service;

import com.example.playmon_server.domain.Role;
import com.example.playmon_server.domain.User;
import com.example.playmon_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 서비스 구분 (google, naver, kakao 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 유저 정보 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuth2UserInfo userInfo = extractUserInfo(registrationId, attributes);

        if (userInfo == null) {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        // db에 유저 저장 or 업데이트
        User user = saveOrUpdate(userInfo.email, userInfo.name, registrationId, userInfo.providerId);

        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("role", user.getRole().name());
        customAttributes.put("userId", user.getId());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                customAttributes,
                userNameAttributeName
        );
    }

    private record OAuth2UserInfo(String email, String name, String providerId) {}

    private OAuth2UserInfo extractUserInfo(String registrationId, Map<String, Object> attributes) {
        if("google".equals(registrationId)) {
            return new OAuth2UserInfo(
                    (String) attributes.get("email"),
                    (String) attributes.get("name"),
                    (String) attributes.get("sub")
            );
        }
        return null;
    }

    private User saveOrUpdate(String email, String name, String provider, String providerId) {
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .name(name)
                        .provider(provider)
                        .providerId(providerId)
                        .role(Role.USER)
                        .build());
        return userRepository.save(user);
    }
}