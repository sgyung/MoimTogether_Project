package com.project.moimtogether.util;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.dto.member.CustomUserDetailsDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Member getCurrentMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof CustomUserDetailsDTO){
            CustomUserDetailsDTO customUserDetailsDTO = (CustomUserDetailsDTO) authentication.getPrincipal();
            return customUserDetailsDTO.getMember();
        }

        return null;
    }
}
