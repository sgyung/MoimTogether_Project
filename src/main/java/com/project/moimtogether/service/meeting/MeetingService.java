package com.project.moimtogether.service.meeting;

import com.project.moimtogether.config.error.customException.ExceptionCode;
import com.project.moimtogether.config.error.customException.CustomLogicException;
import com.project.moimtogether.config.error.customException.MeetingException;
import com.project.moimtogether.config.error.customException.PlaceException;
import com.project.moimtogether.domain.alarm.Alarm;
import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.meeting.MeetingErrorCode;
import com.project.moimtogether.domain.meeting.MeetingStatus;
import com.project.moimtogether.domain.meetingroom.MeetingRoom;
import com.project.moimtogether.domain.meetingroom.MeetingRoomRole;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.domain.place.PlaceErrorCode;
import com.project.moimtogether.dto.alarm.AlarmDTO;
import com.project.moimtogether.dto.meeting.*;
import com.project.moimtogether.dto.place.PlaceDTO;
import com.project.moimtogether.repository.*;
import com.project.moimtogether.repository.query.QueryRepository;
import com.project.moimtogether.util.FormatterUtils;
import com.project.moimtogether.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final QueryRepository queryRepository;
    private final AlarmRepository alarmRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final NotifyRepository notifyRepository;
    private final PlaceRepository placeRepository;

    // 모임 방 생성 메서드
    public PostMeetingResDTO createMeetingService(PostMeetingReqDTO postMeetingReqDTO) throws MeetingException {
        Member currentMember = validateCurrentMember();
        Member persistedMember = findPersistedMember(currentMember);

        Place place = findPlace(postMeetingReqDTO.getPlaceId());
        
        try {
            MeetingDTO meetingDTO = buildMeetingDTO(postMeetingReqDTO, persistedMember, place);
            Meeting newMeeting = saveNewMeeting(meetingDTO);
            createAndLinkMeetingRoom(newMeeting, persistedMember);

            return buildPostMeetingResDTO(newMeeting);
        } catch (Exception e) {
            throw new MeetingException(MeetingErrorCode.UNEXPECTED_ERROR);
        }
    }

    // 모임 방 전체 조회
    public List<PostMeetingResDTO> findAllMeetingList() {
        List<Meeting> meetings = meetingRepository.findAll();
        if (meetings.isEmpty()) {
            throw new MeetingException(MeetingErrorCode.MEETING_NOT_EXISTS);
        }

        return meetings.stream()
                .map(this::buildPostMeetingResDTO)
                .collect(Collectors.toList());
    }

    // 특정 모임 조회 서비스
    public MeetingRoomResDTO findMeetingRoom(Long meetingId) throws MeetingException {
        Meeting currentMeeting = findMeeting(meetingId);

        List<MeetingRoomMemberResDTO> meetingRoomMembers = queryRepository.findMeetingRoomAllMembers(meetingId);
        List<MeetingRoomNotifyDTO> meetingRoomNotifies = queryRepository.findMeetingRoomNotifies(meetingId);

        PlaceDTO placeDTO = buildPlaceDTO(currentMeeting.getPlace());

        return buildMeetingRoomResDTO(currentMeeting, meetingRoomMembers, meetingRoomNotifies, placeDTO);
    }

    // 모임 수정 서비스
    public PostMeetingResDTO modifyMeetingService(ModifyMeetingReqDTO modifyMeetingReqDTO) throws MeetingException {
        Member currentMember = validateCurrentMember();
        Member persistedMember = findPersistedMember(currentMember);
        Meeting currentMeeting = findMeeting(modifyMeetingReqDTO.getMeetingId());

        checkPermissions(persistedMember, currentMeeting);

        currentMeeting.modifyMeeting(modifyMeetingReqDTO);
        checkMaxPersonLimit(modifyMeetingReqDTO, currentMeeting);

        currentMeeting = meetingRepository.save(currentMeeting);
        currentMeeting.updateStatus();

        sendModificationNotifications(currentMeeting);

        return buildPostMeetingResDTO(currentMeeting);
    }

    // 모임 가입 서비스
    public void joinMeetingService(Long meetingId) throws MeetingException {
        Member currentMember = validateCurrentMember();
        Member member = findPersistedMember(currentMember);
        Meeting currentMeeting = findMeetingWithLock(meetingId);

        validateMeetingStatus(currentMeeting);

        MeetingRoom meetingRoom = createMeetingRoom(member, currentMeeting);

        saveMeetingRoom(meetingRoom, member, currentMeeting);
        sendJoinNotification(currentMeeting, member);

        currentMeeting.updateStatus();
    }

    // 모임 삭제 서비스
    public void deleteMeetingService(Long meetingId) throws MeetingException {
        Member currentMember = validateCurrentMember();
        Member persistedMember = findPersistedMember(currentMember);
        Meeting currentMeeting = findMeeting(meetingId);

        checkPermissions(persistedMember, currentMeeting);

        validateMeetingMembers(currentMeeting);

        notifyRepository.deleteByMeeting(currentMeeting);
        meetingRepository.delete(currentMeeting);
    }

    private Member validateCurrentMember() {
        Member currentMember = SecurityUtils.getCurrentMember();
        if (currentMember == null) {
            throw new CustomLogicException(ExceptionCode.USER_NONE, "사용자를 찾을 수 없습니다.");
        }
        return currentMember;
    }

    private Member findPersistedMember(Member currentMember) {
        return userRepository.findById(currentMember.getId())
                .orElseThrow(() -> new CustomLogicException(ExceptionCode.USER_NONE, "회원 정보를 찾을 수 없습니다."));
    }

    private Place findPlace(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceException(PlaceErrorCode.PLACE_NOT_EXISTS_ERROR));
    }

    private MeetingDTO buildMeetingDTO(PostMeetingReqDTO postMeetingReqDTO, Member persistedMember, Place place) {
        return MeetingDTO.builder()
                .meetingTitle(postMeetingReqDTO.getMeetingTitle())
                .maximumPerson(postMeetingReqDTO.getMaximumPerson())
                .meetingDescription(postMeetingReqDTO.getMeetingDescription())
                .meetingDate(postMeetingReqDTO.getMeetingDate())
                .meetingTime(postMeetingReqDTO.getMeetingTime())
                .meetingStatus(MeetingStatus.Y)
                .meetingLeader(persistedMember.getMemberId())
                .meetingPlace(place)
                .build();
    }

    private Meeting saveNewMeeting(MeetingDTO meetingDTO) {
        Meeting newMeeting = Meeting.createMeeting(meetingDTO);
        return meetingRepository.save(newMeeting);
    }

    private void createAndLinkMeetingRoom(Meeting meeting, Member member) {
        MeetingRoom meetingRoom = MeetingRoom.joinMeetingRoom(new MeetingRoomDTO(member, meeting, MeetingRoomRole.OWNER, LocalDateTime.now()));
        meeting.addMeetingRoom(meetingRoom);
        member.addMeetingRoom(meetingRoom);
    }

    private PostMeetingResDTO buildPostMeetingResDTO(Meeting meeting) {
        return PostMeetingResDTO.builder()
                .meetingId(meeting.getMeetingId())
                .meetingTitle(meeting.getMeetingTitle())
                .meetingDescription(meeting.getMeetingDescription())
                .meetingDate(String.valueOf(meeting.getMeetingDate()))
                .meetingTime(String.valueOf(meeting.getMeetingTime()))
                .meetingLeader(meeting.getMeetingLeader())
                .maximumPerson(meeting.getMaximumPerson())
                .meetingStatus(String.valueOf(meeting.getMeetingStatus()))
                .meetingCreateTime(FormatterUtils.convertTimestampToString(meeting.getMeetingCreateTime()))
                .build();
    }

    private Meeting findMeeting(Long meetingId) throws MeetingException {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.MEETING_NOT_EXISTS));
    }

    private Meeting findMeetingWithLock(Long meetingId) throws MeetingException {
        return meetingRepository.findByIdWithLock(meetingId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.MEETING_NOT_EXISTS));
    }

    private void checkPermissions(Member persistedMember, Meeting currentMeeting) throws MeetingException {
        if (!persistedMember.getMemberId().equals(currentMeeting.getMeetingLeader())) {
            throw new MeetingException(MeetingErrorCode.PERMISSION_DENIED);
        }
    }

    private void checkMaxPersonLimit(ModifyMeetingReqDTO modifyMeetingReqDTO, Meeting currentMeeting) throws MeetingException {
        List<Member> meetingMember = queryRepository.findMeetingGuestMembers(currentMeeting.getMeetingId());
        if (meetingMember.size() > Integer.parseInt(modifyMeetingReqDTO.getMaximumPerson())) {
            throw new MeetingException(MeetingErrorCode.MEETING_MODIFY_MAXIMUM_PERSON_ERROR);
        }
    }

    private void sendModificationNotifications(Meeting currentMeeting) {
        List<Member> meetingMember = queryRepository.findMeetingGuestMembers(currentMeeting.getMeetingId());
        for (Member member : meetingMember) {
            Alarm newAlarm = Alarm.createAlarm(new AlarmDTO(currentMeeting.getMeetingTitle(), "모임 정보가 수정되었습니다.", member));
            alarmRepository.save(newAlarm);
        }
    }

    private void validateMeetingStatus(Meeting currentMeeting) throws MeetingException {
        if (currentMeeting.getMeetingStatus() == MeetingStatus.N) {
            throw new MeetingException(MeetingErrorCode.MEETING_JOIN_NOT_ALLOWED_ERROR);
        }
    }

    private MeetingRoom createMeetingRoom(Member member, Meeting currentMeeting) {
        return MeetingRoom.joinMeetingRoom(new MeetingRoomDTO(member, currentMeeting, MeetingRoomRole.GUEST, LocalDateTime.now()));
    }

    private void saveMeetingRoom(MeetingRoom meetingRoom, Member member, Meeting currentMeeting) {
        currentMeeting.addMeetingRoom(meetingRoom);
        member.addMeetingRoom(meetingRoom);
        meetingRoomRepository.save(meetingRoom);
    }

    private void sendJoinNotification(Meeting currentMeeting, Member member) {
        Member ownerMember = userRepository.findByMemberId(currentMeeting.getMeetingLeader()).orElseThrow();
        Alarm newAlarm = Alarm.createAlarm(new AlarmDTO(currentMeeting.getMeetingTitle(), member.getMemberId() + "회원이 모임에 참여하였습니다.", ownerMember));
        alarmRepository.save(newAlarm);
    }

    private void validateMeetingMembers(Meeting currentMeeting) throws MeetingException {
        List<Member> meetingMember = queryRepository.findMeetingGuestMembers(currentMeeting.getMeetingId());
        if (!meetingMember.isEmpty()) {
            throw new MeetingException(MeetingErrorCode.MEETING_REMAIN_PERSON_ERROR);
        }
    }

    private PlaceDTO buildPlaceDTO(Place place) {
        return new PlaceDTO(
                place.getPlaceName(),
                place.getPlaceAddress(),
                place.getPlaceLongitude(),
                place.getPlaceLatitude()
        );
    }

    private MeetingRoomResDTO buildMeetingRoomResDTO(Meeting currentMeeting, List<MeetingRoomMemberResDTO> meetingRoomMembers, List<MeetingRoomNotifyDTO> meetingRoomNotifies, PlaceDTO placeDTO) {
        return MeetingRoomResDTO.builder()
                .meetingRoomId(currentMeeting.getMeetingId())
                .meetingTitle(currentMeeting.getMeetingTitle())
                .meetingDescription(currentMeeting.getMeetingDescription())
                .meetingStatus(currentMeeting.getMeetingStatus())
                .place(placeDTO)
                .meetingRoomMembers(meetingRoomMembers)
                .meetingRoomNotifies(meetingRoomNotifies)
                .build();
    }
}