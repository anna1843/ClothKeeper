package org.project.dev.config.semiMember;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.project.dev.member.entity.SemiMemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class SemiMyUserDetails implements UserDetails {

    @Autowired
    private SemiMemberEntity semiMemberEntity;

    // 간편회원
    public SemiMyUserDetails(SemiMemberEntity semiMemberEntity) {
        this.semiMemberEntity = semiMemberEntity;
    }

    // 권한
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectRole = new ArrayList<>();
        collectRole.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_" + semiMemberEntity.getRole().toString(); // ROLE_
            }
        });
        return collectRole;
    }

    @Override
    public String getPassword() {
        return semiMemberEntity.getSemiMemberPhone();
    }

    @Override
    public String getUsername() {
        return semiMemberEntity.getSemiMemberEmail();
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

