package com.example.mydiary.service.impl;

import com.example.mydiary.entity.Member;
import com.example.mydiary.repository.MemberMapper;
import com.example.mydiary.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserDetailsServiceImpl implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String uId) throws UsernameNotFoundException {
        Member member = memberMapper.findByuId(uId); // uId 기준

        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + uId);
        }

        return new MyUserDetails(member);
    }
}