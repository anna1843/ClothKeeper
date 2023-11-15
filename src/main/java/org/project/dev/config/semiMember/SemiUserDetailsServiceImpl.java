package org.project.dev.config.semiMember;

import org.project.dev.member.entity.SemiMemberEntity;
import org.project.dev.member.repository.SemiMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SemiUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SemiMemberRepository semiMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String semiMemberEmail) throws UsernameNotFoundException {

        SemiMemberEntity semiMemberEntity=semiMemberRepository.findBySemiMemberEmail(semiMemberEmail).orElseThrow(() -> {
            throw new UsernameNotFoundException("이메일이 없습니다");
        });

        return new SemiMyUserDetails(semiMemberEntity);
    }
}
