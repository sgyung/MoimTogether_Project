package com.project.moimtogether;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.dto.like.CancelLikeReqDTO;
import com.project.moimtogether.repository.PlaceRepository;
import com.project.moimtogether.repository.UserRepository;
import com.project.moimtogether.service.like.LikeService;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

    @Test
    public void testOptimisticLockExceptionHandling() {
        Place place = placeRepository.findById(27L).get();
        Member member1 = userRepository.findById(1L).get();
        Member member2 = userRepository.findById(2L).get();


        // 두 스레드를 생성하여 동시성 테스트
        Thread thread1 = new Thread(() -> {
            try {
                likeService.registerLike(27L, "test1");
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
                likeService.cancelLike(new CancelLikeReqDTO(27L,51L),1L);
            } catch (OptimisticLockException e) {
                System.out.println("스레드 2: OptimisticLockException 발생");
                likeService.likePlaceWithRetry(place, member1);
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("스레드 2: 기타 예외 발생 - " + e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread3 = new Thread(() -> {
            try {
                // 첫 번째 스레드가 대기 중인 동안 두 번째 좋아요 등록 시도
                Thread.sleep(2000);
                likeService.registerLike(27L, "test2");
            } catch (OptimisticLockException e) {
                System.out.println("스레드 3: OptimisticLockException 발생");
                likeService.likePlaceWithRetry(place, member2);
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("스레드 3: 기타 예외 발생 - " + e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // 스레드 실행
        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            fail("테스트 실패: 스레드 실행 중 문제가 발생했습니다.");
        }
    }

}
