package com.project.moimtogether.service.member;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.dto.member.CustomUserDetailsDTO;
import com.project.moimtogether.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetailsDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = userRepository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetailsDTO(member);
    }
}
