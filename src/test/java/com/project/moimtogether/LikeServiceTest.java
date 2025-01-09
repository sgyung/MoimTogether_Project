package com.project.moimtogether;

import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.repository.LikeRepository;
import com.project.moimtogether.repository.MeetingRepository;
import com.project.moimtogether.repository.PlaceRepository;
import com.project.moimtogether.repository.UserRepository;
import com.project.moimtogether.service.like.LikeService;
import com.project.moimtogether.service.meeting.MeetingService;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.exception.LockAcquisitionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingRepository meetingRepository;

    @Test
    public void testOptimisticLockExceptionHandling() {
        Place place = placeRepository.findById(10L).get();
        Member member1 = userRepository.findById(1L).get();
        Member member2 = userRepository.findById(2L).get();

        // 두 스레드를 생성하여 동시성 테스트
        Thread thread1 = new Thread(() -> {
            try {
                likeService.registerLike(20L, "test1");
            } catch (OptimisticLockException e) {
                System.out.println("스레드 1: OptimisticLockException 발생");
                likeService.likePlaceWithRetry(place, member1);
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("스레드 1: 기타 예외 발생 - " + e.getMessage());
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                // 첫 번째 스레드가 대기 중인 동안 두 번째 좋아요 등록 시도
                Thread.sleep(2000);
                likeService.registerLike(20L, "test2");
            } catch (OptimisticLockException e) {
                System.out.println("스레드 2: OptimisticLockException 발생");
                likeService.likePlaceWithRetry(place, member2);
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("스레드 2: 기타 예외 발생 - " + e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // 스레드 실행
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            fail("테스트 실패: 스레드 실행 중 문제가 발생했습니다.");
        }
    }

//    @Test
//    public void testPessimisticLockInJoinMeetingService() {
//        // 테스트를 위한 초기 데이터 조회
//        Meeting meeting = meetingRepository.findById(1L)
//                .orElseThrow(() -> new RuntimeException("Meeting not found"));
//        Member member2 = userRepository.findById(2L)
//                .orElseThrow(() -> new RuntimeException("Member2 not found"));
//        Member member3 = userRepository.findById(3L)
//                .orElseThrow(() -> new RuntimeException("Member2 not found"));
//
//        // 첫 번째 스레드
//        Thread thread1 = new Thread(() -> {
//            try {
//                meetingService.joinMeetingServiceTest(meeting.getMeetingId(), member2);
//                System.out.println("Thread 1: 모임 가입 성공");
//            } catch (Exception e) {
//                System.out.println("인원 초과");
//                System.out.println("Thread 1: 예외 발생 - " + e.getMessage());
//            }
//        });
//
//        // 두 번째 스레드
//        Thread thread2 = new Thread(() -> {
//            try {
//                meetingService.joinMeetingServiceTest(meeting.getMeetingId(), member3);
//                System.out.println("Thread 2: 모임 가입 성공");
//            } catch (Exception e) {
//                System.out.println("인원 초과");
//                System.out.println("Thread 2: 예외 발생 - " + e.getMessage());
//            }
//        });
//
//        // 스레드 실행
//        thread1.start();
//        thread2.start();
//
//        try {
//            thread1.join();
//            thread2.join();
//        } catch (InterruptedException e) {
//            fail("테스트 실패: 스레드 실행 중 문제가 발생했습니다.");
//        }
//    }
}
