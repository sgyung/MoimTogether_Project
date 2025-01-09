package com.project.moimtogether.repository.query;

import com.project.moimtogether.domain.alarm.AlarmReadStatus;
import com.project.moimtogether.domain.like.Likes;
import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.meetingroom.MeetingRoom;
import com.project.moimtogether.domain.meetingroom.MeetingRoomRole;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.notify.Notify;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.dto.alarm.AlarmResDTO;
import com.project.moimtogether.dto.meeting.MeetingRoomMemberDTO;
import com.project.moimtogether.dto.meeting.MeetingRoomMemberResDTO;
import com.project.moimtogether.dto.meeting.MeetingRoomNotifyDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryRepository {

    private final EntityManager em;

    // 특정 모임 방에 GUEST인 회원만 조회하는 쿼리
    public List<Member> findMeetingGuestMembers(Long meetingId) {
        return em.createQuery("select distinct m from Member m" +
                " join fetch m.meetingRooms mr" +
                " join fetch mr.meeting mt" +
                " where mt.meetingId = :meetingId" +
                " and mr.meetingRoomRole = :role", Member.class)
                .setParameter("meetingId", meetingId)
                .setParameter("role", MeetingRoomRole.GUEST)
                .getResultList();
    }

    // 특정 모임 방에 있는 모든 멤버 조회 쿼리
    public List<MeetingRoomMemberResDTO> findMeetingRoomAllMembers(Long meetingId) {
        return em.createQuery("select new com.project.moimtogether.dto.meeting.MeetingRoomMemberResDTO(" +
                "m.memberId," +
                "mr.meetingRoomRole," +
                "mr.meetingRoomJoinDate)" +
                " from MeetingRoom mr" +
                " join mr.member m" +
                " where mr.meeting.meetingId = :meetingId",MeetingRoomMemberResDTO.class)
                .setParameter("meetingId",meetingId)
                .getResultList();
    }

    // 특정 모임 방에 있는 특정 멤버 조회 쿼리
    public MeetingRoomMemberDTO findMeetingRoomMember(Long meetingId, String memberId) {
        try {
            return em.createQuery("select new com.project.moimtogether.dto.meeting.MeetingRoomMemberDTO(" +
                            "m," +
                            "mr.meetingRoomRole," +
                            "mr.meetingRoomJoinDate)" +
                            " from MeetingRoom mr" +
                            " join mr.member m" +
                            " where mr.meeting.meetingId = :meetingId" +
                            " and m.memberId = :memberId", MeetingRoomMemberDTO.class)
                    .setParameter("meetingId", meetingId)
                    .setParameter("memberId", memberId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // 데이터가 없는 경우 null 반환
        }
    }

    // 특정 모임 방에 있는 모든 공지사항 조회 쿼리
    public List<MeetingRoomNotifyDTO> findMeetingRoomNotifies(Long meetingId) {
        return em.createQuery("select new com.project.moimtogether.dto.meeting.MeetingRoomNotifyDTO("+
                        " n.id, n.notifyTitle)" +
                        " from Notify n" +
                        " where n.meeting.meetingId = :meetingId",MeetingRoomNotifyDTO.class)
                .setParameter("meetingId",meetingId)
                .getResultList();
    }

    // 회원이 속한 모든 모임 조회
    public List<Meeting> findMeetings(Long memberId) {
        try {
            return em.createQuery("select distinct mr.meeting from MeetingRoom mr" +
                            " join mr.meeting mt" +
                            " where mr.member.id = :memberId", Meeting.class)
                    .setParameter("memberId", memberId)
                    .getResultList();
        }catch (NoResultException e) {
            // 모임 없는 경우 null을 반환하거나 예외를 처리
            return null;
        }
    }

    // 특정 모임에서 본인의 역할 조회
    public MeetingRoomRole findMeetingRoomRole(Long meetingId, Long memberId) {
        return em.createQuery("select mr.meetingRoomRole from MeetingRoom mr" +
                " where mr.meeting.meetingId = :meetingId" +
                " and mr.member.id = :memberId",MeetingRoomRole.class)
                .setParameter("meetingId", meetingId)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }

    // 특정 모임 조회
    public MeetingRoom findMeetingRoom(Long meetingId, Long memberId) {
        return em.createQuery("select mr from MeetingRoom mr" +
                        " where mr.meeting.meetingId = :meetingId" +
                        " and mr.member.id = :memberId",MeetingRoom.class)
                .setParameter("meetingId", meetingId)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }

    // 회원별 읽지 않은 알림 조회
    public List<AlarmResDTO> findNotReadAlarm(Long memberId) {
        return em.createQuery("select new com.project.moimtogether.dto.alarm.AlarmResDTO(" +
                " a.id, a.title, a.content, a.alarmDate)" +
                " from Alarm a" +
                " where a.member.id= :memberId" +
                " and a.alarmReadStatus= :alarmStatus",AlarmResDTO.class)
                .setParameter("memberId", memberId)
                .setParameter("alarmStatus", AlarmReadStatus.N)
                .getResultList();
    }

    // 특정 공지사항 조회
    public Notify findNotifyWithAll(Long notifyId) {
        return em.createQuery("select distinct n from Notify n" +
                " join fetch n.meeting mt" +
                " join fetch n.replies r" +
                " join fetch r.member m" +
                " where n.id = :notifyId", Notify.class)
                .setParameter("notifyId", notifyId)
                .getSingleResult();
    }

    // 특정 공지사항 조회 - 모임만 조인
    public Notify findNotifyWithMeeting(Long notifyId) {
        try {
            return em.createQuery("select distinct n from Notify n" +
                            " join fetch n.meeting mt" +
                            " where n.id = :notifyId", Notify.class)
                    .setParameter("notifyId", notifyId)
                    .getSingleResult();
        }catch (NoResultException e) {
            return null;
        }
    }

    // 특정 장소 좋아요 조회
    public Likes findLike(Long placeId, Long memberId, Long likeId){
        try {
            return em.createQuery("select distinct l from Likes l " +
                            " join fetch l.place p" +
                            " join fetch l.member m" +
                            " where p.id = :placeId" +
                            " and m.id = :memberId" +
                            " and l.id = :likeId", Likes.class)
                    .setParameter("placeId", placeId)
                    .setParameter("memberId", memberId)
                    .setParameter("likeId", likeId)
                    .getSingleResult();
        } catch (NoResultException e) {
            // 좋아요가 없는 경우 null을 반환하거나 예외를 처리
            return null;
        }
    }

    // 특정 장소 멤버와 장소를 이용한 좋아요 조회
    public Likes findLikeByMemberAndPlace(Long memberId, Long placeId){
        try {
            return em.createQuery("select distinct l from Likes l " +
                            " where l.place.id = :placeId" +
                            " and l.member.id = :memberId", Likes.class)
                    .setParameter("placeId", placeId)
                    .setParameter("memberId", memberId)
                    .getSingleResult();
        } catch (NoResultException e) {
            // 좋아요가 없는 경우 null을 반환하거나 예외를 처리
            return null;
        }
    }

    // 특정 장소 조회 쿼리
    public Place findPlace(Long placeId) {
        try {
            return em.createQuery("select distinct p from Place p" +
                            " join fetch p.reviews r" +
                            " join fetch r.member m" +
                            " where p.id = :placeId", Place.class)
                    .setParameter("placeId", placeId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
