package com.project.moimtogether.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.moimtogether.domain.meetingroom.MeetingRoom;
import com.project.moimtogether.dto.member.ModifyMemberReqDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "member")
public class Member implements UserDetails {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //,generator = "member_seq")
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_Login_id", unique = true, nullable = false)
    private String memberId;

    @Column(name = "member_password", nullable = false)
    private String memberPassword;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_birth", nullable = false)
    private String memberBirth;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_gender", nullable = false)
    private UserGender memberGender;

    @Column(name = "member_phone", nullable = false)
    private String memberPhone;

    @Column(name = "profile_path")
    private String profilePath;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingRoom> meetingRooms = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return memberPassword;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return memberId;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    //생성 메서드
    public static Member signUpMember(String userId, String password, String name, String birth, String email, UserGender gender, String phone) {
        Member member = new Member();
        member.setMemberId(userId);
        member.setMemberPassword(ENCODER.encode(password));
        member.setMemberName(name);
        member.setMemberBirth(birth);
        member.setMemberEmail(email);
        member.setMemberGender(gender);
        member.setMemberPhone(phone);
        member.setJoinDate(LocalDateTime.now());

        return member;
    }

    //연관관계 편의 메소드
    public void addMeetingRoom(MeetingRoom meetingRoom) {
        if (!this.meetingRooms.contains(meetingRoom)) { // 중복 방지
            this.meetingRooms.add(meetingRoom);
            meetingRoom.setMember(this);
        }
    }

    // 수정 메서드
    public void modifyMember(ModifyMemberReqDTO modifyUserReqDTO) {
        if(modifyUserReqDTO.getMemberName() != null){
            this.memberName = modifyUserReqDTO.getMemberName();
        }
        if(modifyUserReqDTO.getMemberEmail() != null){
            this.memberEmail = modifyUserReqDTO.getMemberEmail();
        }
        if(modifyUserReqDTO.getMemberPhone() != null){
            this.memberPhone = modifyUserReqDTO.getMemberPhone();
        }
        if (modifyUserReqDTO.getProfilePath() != null) {
            this.profilePath = modifyUserReqDTO.getProfilePath();
        }
    }

    // 비밀번호 재생성 메서드
    public void regenerativePassword(String password) {
        this.memberPassword = ENCODER.encode(password);
    }

    // 모임 탈퇴 메서드
    public void removeMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRooms.remove(meetingRoom);
    }
}
