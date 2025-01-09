package com.project.moimtogether.service.member;

import com.project.moimtogether.config.error.customException.ExceptionCode;
import com.project.moimtogether.config.error.customException.CustomLogicException;
import com.project.moimtogether.config.error.customException.MeetingException;
import com.project.moimtogether.config.error.customException.UserException;
import com.project.moimtogether.domain.alarm.Alarm;
import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.meeting.MeetingErrorCode;
import com.project.moimtogether.domain.meetingroom.MeetingRoom;
import com.project.moimtogether.domain.meetingroom.MeetingRoomRole;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.member.UserErrorCode;
import com.project.moimtogether.dto.alarm.AlarmDTO;
import com.project.moimtogether.dto.meeting.PostMeetingResDTO;
import com.project.moimtogether.dto.member.*;
import com.project.moimtogether.jwt.AuthToken;
import com.project.moimtogether.repository.AlarmRepository;
import com.project.moimtogether.repository.MeetingRepository;
import com.project.moimtogether.repository.MeetingRoomRepository;
import com.project.moimtogether.repository.UserRepository;
import com.project.moimtogether.repository.query.QueryRepository;
import com.project.moimtogether.util.FormatterUtils;
import com.project.moimtogether.util.SecurityUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ProfileImageService profileImageService;
    private final AuthService authService;
    private final MailService mailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final QueryRepository queryRepository;
    private final MeetingRepository meetingRepository;
    private final AlarmRepository alarmRepository;

    // 회원가입 서비스 메소드
    public PostMemberResDTO signUp(PostMemberReqDTO reqDTO) {
        if(userRepository.findByMemberId(reqDTO.getMemberId()).isPresent()) {
            throw new UserException(UserErrorCode.ID_ALREADY_EXISTS);
        }

        if(!reqDTO.getMemberPassword().equals(reqDTO.getMemberPasswordCheck())) {
            throw new UserException(UserErrorCode.PASSWORD_NOT_MATCH);
        }

        try {
            Member newUser = Member.signUpMember(reqDTO.getMemberId(),
                    reqDTO.getMemberPassword(),
                    reqDTO.getMemberName(),
                    reqDTO.getMemberBirth(),
                    reqDTO.getMemberEmail(),
                    reqDTO.getMemberGender(),
                    reqDTO.getMemberPhone()
            );

            // 유저 insert
            newUser = userRepository.save(newUser);

            return getPostUserResDTO(newUser);
        }catch (Exception e) {
            throw new UserException(UserErrorCode.DATABASE_ERROR);
        }

    }

    // 로그인 서비스 메소드
    public List<AuthToken> login(LoginDTO loginDTO){
        List<AuthToken> authTokenOList;

        Optional<Member> memberOptional = userRepository.findByMemberId(loginDTO.getMemberId());

        if(memberOptional.isEmpty()) {
            throw new UserException(UserErrorCode.ID_NOT_EXISTS);
        }

        Member member = memberOptional.get();

        if(!bCryptPasswordEncoder.matches(loginDTO.getMemberPassword(),member.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_NOT_MATCH);
        }

        authTokenOList = authService.login(member);

        return authTokenOList;
    }

    //회원 수정 메소드
    public PostMemberResDTO modifyService(ModifyMemberReqDTO modifyUserReqDTO, MultipartFile profileImage) throws UserException, IOException {
        Member currentMember = SecurityUtils.getCurrentMember();
        if (currentMember == null) {
            throw new CustomLogicException(ExceptionCode.USER_NONE, "사용자를 찾을 수 없습니다.");
        }

        Member member = userRepository.findById(currentMember.getId()).get();
        member.modifyMember(modifyUserReqDTO);

        if(profileImage != null && !profileImage.isEmpty()) {
            String filePath = profileImageService.uploadProfileImage(profileImage);
            member.setProfilePath(filePath);
        }

        userRepository.save(member);

        // 응답 객체에 유저 반환
        return getPostUserResDTO(member);
    }

    // 비밀번호 재설정 서비스 메서드
    public void regeneratePasswordService(PasswordVerificationReqDTO passwordVerificationReqDTO){
        Member currentMember = SecurityUtils.getCurrentMember();
        if (currentMember == null) {
            throw new CustomLogicException(ExceptionCode.USER_NONE, "사용자를 찾을 수 없습니다.");
        }

        try{
            Member member = userRepository.findById(currentMember.getId()).get();

            if(!bCryptPasswordEncoder.matches(passwordVerificationReqDTO.getTemporaryPassword(),member.getPassword())) {
                throw new UserException(UserErrorCode.PASSWORD_NOT_MATCH);
            }

            member.regenerativePassword(passwordVerificationReqDTO.getMemberPassword());

            userRepository.save(member);

        }catch (Exception e) {
            throw new UserException(UserErrorCode.DATABASE_ERROR);
        }
    }

    // 모든 회원 조회
    public List<BaseMemberDTO> searchMemberListService(){
        List<Member> memberList = userRepository.findAll();
        if(memberList.isEmpty()) {
            throw new CustomLogicException(ExceptionCode.USER_NONE);
        }

        try{
            List<BaseMemberDTO> resDTO = memberList.stream()
                    .map(m -> BaseMemberDTO.builder()
                            .memberId(m.getMemberId())
                            .memberName(m.getMemberName()).build()
                    ).collect(Collectors.toList());

            return resDTO;

        }catch (Exception e) {
            throw new UserException(UserErrorCode.DATABASE_ERROR);
        }
    }

    // 특정 회원 조회
    public MemberInfoDTO searchMemberService(Long id){
        Optional<Member> selectMember = userRepository.findById(id);

        if(selectMember.isEmpty()) {
            throw new CustomLogicException(ExceptionCode.USER_NONE);
        }

        try{
            MemberInfoDTO userInfoDTO = MemberInfoDTO.builder()
                    .memberId(selectMember.get().getMemberId())
                    .memberName(selectMember.get().getUsername())
                    .memberEmail(selectMember.get().getMemberEmail())
                    .memberBirth(selectMember.get().getMemberPhone())
                    .memberPhone(selectMember.get().getMemberPhone())
                    .profile(selectMember.get().getProfilePath())
                    .build();

            return userInfoDTO;

        }catch (Exception e) {
            throw new UserException(UserErrorCode.DATABASE_ERROR);
        }
    }

    // 마이페이지 서비스 메서드
    public PostMemberResDTO myProfile() throws UserException{
        Member currentMember = SecurityUtils.getCurrentMember();
        if (currentMember == null) {
            throw new CustomLogicException(ExceptionCode.USER_NONE);
        }

        try{
            currentMember = userRepository.findById(currentMember.getId()).get();
            return getPostUserResDTO(currentMember);
        }catch (Exception e) {
            throw new UserException(UserErrorCode.DATABASE_ERROR);
        }
    }

    // 아이디 찾기 서비스 메서드
    public String findLoginIdService(FindLoginIdReqDTO findLoginIdReqDTO) throws UserException{
        Optional<Member> member = userRepository.findByMemberNameAndMemberEmailAndMemberPhone(
                findLoginIdReqDTO.getMemberName(),
                findLoginIdReqDTO.getMemberEmail(),
                findLoginIdReqDTO.getMemberPhone());

        if(member.isEmpty()){
            throw new CustomLogicException(ExceptionCode.USER_NONE);
        }

        try{
            String findId = member.get().getMemberId();

            return findId;
        }catch (Exception e) {
            throw new UserException(UserErrorCode.DATABASE_ERROR);
        }
    }

    // 비밀번호 찾기 임시비밀번호 요청 서비스 메서드
    public void sendCodeToEmail(FindPasswordReqDTO findPasswordReqDTO) {
        String temporaryPassword = mailService.createTemporaryPassword();

        Optional<Member> findMember = userRepository.findByMemberId(findPasswordReqDTO.getMemberId());
        if(findMember.isEmpty()){
            throw new CustomLogicException(ExceptionCode.USER_NONE);
        }
        if(!findMember.get().getMemberName().equals(findPasswordReqDTO.getMemberName())){
            throw new CustomLogicException(ExceptionCode.VALUE_NOT_MATCH,"이름이 일치하지 않습니다.");
        }
        if(!findMember.get().getMemberEmail().equals(findPasswordReqDTO.getMemberEmail())){
            throw new CustomLogicException(ExceptionCode.VALUE_NOT_MATCH,"이메일이 일치하지 않습니다.");
        }

        String title = "MoimTogether 임시 비밀번호";

        String content = "<html>"
                + "<body>"
                + "<h1>MoimTogether 임시 비밀번호: " + temporaryPassword + "</h1>"
                + "</body>"
                + "</html>";

        try{
            mailService.sendMail(findPasswordReqDTO.getMemberEmail(), title, content);
            findMember.get().regenerativePassword(temporaryPassword);

            userRepository.save(findMember.get());
        }catch (MailException | MessagingException e) {
            throw new RuntimeException("이메일 전송에 문제가 발생하였습니다.",e);
        }catch (Exception e) {
            throw new UserException(UserErrorCode.DATABASE_ERROR);
        }
    }

    // 회원이 속한 모임 리스트 조회
    public List<PostMeetingResDTO> findMeetingList(){
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new CustomLogicException(ExceptionCode.USER_NONE);
        }

        List<Meeting> meetingList = queryRepository.findMeetings(currentMember.getId());

        if(meetingList == null || meetingList.isEmpty()){
            throw new UserException(UserErrorCode.MEETING_ROOM_NOT_EXISTS_ERROR);
        }

        return meetingList.stream()
                .map(meeting -> PostMeetingResDTO.builder()
                        .meetingId(meeting.getMeetingId())
                        .meetingTitle(meeting.getMeetingTitle())
                        .meetingDescription(meeting.getMeetingDescription())
                        .meetingDate(String.valueOf(meeting.getMeetingDate()))
                        .meetingTime(String.valueOf(meeting.getMeetingTime()))
                        .meetingLeader(meeting.getMeetingLeader())
                        .maximumPerson(meeting.getMaximumPerson())
                        .meetingStatus(String.valueOf(meeting.getMeetingStatus()))
                        .meetingCreateTime(FormatterUtils.convertTimestampToString(meeting.getMeetingCreateTime()))
                        .build())
                .collect(Collectors.toList());

    }

    // 회원 모임 탈퇴 서비스
    public void withdrawMeeting(Long meetingId) throws UserException{
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new CustomLogicException(ExceptionCode.USER_NONE);
        }

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()-> new MeetingException(MeetingErrorCode.MEETING_NOT_FOUND_ERROR));

        MeetingRoomRole myRole = queryRepository.findMeetingRoomRole(meetingId, currentMember.getId());

        if(myRole == MeetingRoomRole.OWNER){
            throw new MeetingException(MeetingErrorCode.MEETING_NOT_WITHDRAW_ERROR);
        }

        MeetingRoom meetingRoom = queryRepository.findMeetingRoom(meeting.getMeetingId(), currentMember.getId());
        if (meetingRoom == null) {
            throw new MeetingException(MeetingErrorCode.MEETING_NOT_FOUND_ERROR);
        }

        currentMember = userRepository.findByMemberId(currentMember.getMemberId()).get();

        currentMember.removeMeetingRoom(meetingRoom);

        meeting.updateStatus();
        Member ownerMember = userRepository.findByMemberId(meeting.getMeetingLeader())
                .orElseThrow(()-> new UserException(UserErrorCode.ID_NOT_EXISTS));

        Alarm newAlarm = Alarm.createAlarm(new AlarmDTO(meeting.getMeetingTitle(),currentMember.getMemberId() + "회원이 모임을 탈퇴하였습니다.", ownerMember));
        alarmRepository.save(newAlarm);
    }


    private PostMemberResDTO getPostUserResDTO(Member currentMember) {
        PostMemberResDTO resDTO = PostMemberResDTO.builder()
                .memberId(currentMember.getMemberId())
                .memberName(currentMember.getMemberName())
                .memberBirth(FormatterUtils.convertBirth(currentMember.getMemberBirth()))
                .memberEmail(currentMember.getMemberEmail())
                .memberGender(String.valueOf(currentMember.getMemberGender()))
                .memberPhone(FormatterUtils.convertPhone(currentMember.getMemberPhone()))
                .profilePath(currentMember.getProfilePath())
                .joinDate(FormatterUtils.convertTimestampToString(currentMember.getJoinDate()))
                .build();

        return resDTO;
    }
}

