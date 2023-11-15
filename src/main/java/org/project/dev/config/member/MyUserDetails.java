package org.project.dev.config.member;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.project.dev.member.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
@NoArgsConstructor
public class MyUserDetails implements UserDetails, OAuth2User {

    @Autowired
    private MemberEntity memberEntity;

    private Map<String, Object> attributes;

    // 일반
    public MyUserDetails(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }

    // OAuth2
    public MyUserDetails(MemberEntity memberEntity, Map<String, Object> attributes) {
        this.memberEntity = memberEntity;
        this.attributes = attributes;
    }

    // 권한(사용자의 권한을 접두사 "ROLE_"를 붙여서 반환)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectRole = new ArrayList<>();
        collectRole.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_" + memberEntity.getRole().toString(); // ROLE_
            }
        });
        return collectRole;
    }

    // OAuth2 사용 시 추가 사용자 속성을 반환
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return memberEntity.getMemberEmail();
    }

    @Override
    public String getPassword() {
        return memberEntity.getMemberPassword();
    }

    @Override
    public String getUsername() {
        return memberEntity.getMemberEmail();
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사용자 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }

}
