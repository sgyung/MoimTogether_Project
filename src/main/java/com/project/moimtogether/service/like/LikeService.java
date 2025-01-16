package com.project.moimtogether.service.like;

import com.project.moimtogether.config.error.customException.PlaceException;
import com.project.moimtogether.config.error.customException.UserException;
import com.project.moimtogether.domain.like.Likes;
import com.project.moimtogether.domain.like.LikeStatus;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.member.UserErrorCode;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.domain.place.PlaceErrorCode;
import com.project.moimtogether.dto.like.CancelLikeReqDTO;
import com.project.moimtogether.dto.like.LikeDTO;
import com.project.moimtogether.dto.like.PostLikeResDTO;
import com.project.moimtogether.repository.LikeRepository;
import com.project.moimtogether.repository.PlaceRepository;
import com.project.moimtogether.repository.UserRepository;
import com.project.moimtogether.repository.query.QueryRepository;
import com.project.moimtogether.util.SecurityUtils;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final QueryRepository queryRepository;

    // 장소 좋아요 서비스
    public PostLikeResDTO registerLike(Long placeId) {
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }

        Place place = findPlaceWithSharedLock(placeId);
        Member member = findMemberById(currentMember.getMemberId());

        Likes existingLike = getExistingLike(member, place);

        try {
            Likes likeToReturn;

            if (existingLike != null) {
                // 기존 좋아요 상태 업데이트
                handleLikeUpdate(existingLike, place);
                likeToReturn = existingLike; // 기존 좋아요를 반환
            } else {
                // 새로운 좋아요 등록 및 좋아요 수 업데이트
                likeToReturn = Likes.createLike(new LikeDTO(member, place, LikeStatus.LIKE));
                likeToReturn = likeRepository.save(likeToReturn);
                updateIncreasePlaceLikeCount(place); // 좋아요 수 업데이트
            }

            // DTO 반환
            return buildPostLikeResDTO(likeToReturn, place, member);
        } catch (OptimisticLockException | CannotAcquireLockException | LockAcquisitionException e) {
            // 재시도 로직: 최대 재시도 횟수 초과 방지
            likePlaceWithRetry(place, member);
        } catch (Exception e) {
            throw new PlaceException(PlaceErrorCode.UNEXPECTED_ERROR);
        }
        return null;
    }

    // 장소 좋아요 서비스
    public void registerLike(Long placeId, String memberId) {
        Place place = findPlaceWithSharedLock(placeId);
        Member member = findMemberById(memberId);

        Likes existingLike = getExistingLike(member, place);

        try {
            Likes likeToReturn;

            if (existingLike != null) {
                // 기존 좋아요 상태 업데이트
                handleLikeUpdate(existingLike, place);
                likeToReturn = existingLike; // 기존 좋아요를 반환
            } else {
                // 새로운 좋아요 등록 및 좋아요 수 업데이트
                likeToReturn = Likes.createLike(new LikeDTO(member, place, LikeStatus.LIKE));
                likeToReturn = likeRepository.save(likeToReturn);
                updateIncreasePlaceLikeCount(place); // 좋아요 수 업데이트
            }

            // DTO 반환
            PostLikeResDTO resDTO = buildPostLikeResDTO(likeToReturn, place, member);
        } catch (OptimisticLockException | CannotAcquireLockException | LockAcquisitionException e) {
            // 재시도 로직: 최대 재시도 횟수 초과 방지
            likePlaceWithRetry(place, member);
        } catch (Exception e) {
            throw new PlaceException(PlaceErrorCode.UNEXPECTED_ERROR);
        }
    }

    // 재시도 로직
    public void likePlaceWithRetry(Place place, Member member) {
        int retryCount = 10; // 최대 재시도 횟수
        while (retryCount > 0) {
            try {
                // 좋아요 상태를 처리하는 메서드만 호출하여 다시 시도
                registerNewLike(member, place);
                return; // 성공하면 종료
            } catch (OptimisticLockException e) {
                System.out.println("OptimisticLockException 발생");
                retryCount--;
                if (retryCount == 0) {
                    throw new RuntimeException("최대 재시도 횟수 초과: OptimisticLockException 발생", e);
                }
            } catch (LockAcquisitionException | CannotAcquireLockException e) {
                System.out.println("Deadlock 발생");
                retryCount--;
                if (retryCount == 0) {
                    throw new RuntimeException("최대 재시도 횟수 초과: Deadlock 발생", e);
                }
            }
        }
    }


    // 장소 좋아요 취소 서비스
    public void cancelLike(CancelLikeReqDTO reqDTO) {
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }

        Likes like = queryRepository.findLike(reqDTO.getPlaceId(),currentMember.getId(), reqDTO.getLikeId());

        if(like == null) {
            throw new PlaceException(PlaceErrorCode.LIKE_ID_ERROR);
        }
        try{
            like.updateLikeStatus();

            likeRepository.save(like);

        }catch (Exception e){
            throw new PlaceException(PlaceErrorCode.UNEXPECTED_ERROR);
        }
    }

    // 장소 좋아요 취소 서비스
    public void cancelLike(CancelLikeReqDTO reqDTO, Long memberId) {

        Likes like = queryRepository.findLike(reqDTO.getPlaceId(),memberId, reqDTO.getLikeId());

        if(like == null) {
            throw new PlaceException(PlaceErrorCode.LIKE_ID_ERROR);
        }
        try{
            like.updateLikeStatus();

            likeRepository.save(like);

            updateDecreasePlaceLikeCount(like.getPlace());

        }catch (Exception e){
            throw new PlaceException(PlaceErrorCode.UNEXPECTED_ERROR);
        }
    }

    // 멤버 조회 메서드
    private Member findMemberById(String memberId) {
        return userRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UserException(UserErrorCode.ID_NOT_EXISTS));
    }

    @Transactional(readOnly = true) // 공유 락을 사용
    public Place findPlaceWithSharedLock(Long placeId) {
        return placeRepository.findForLockById(placeId)
                .orElseThrow(() -> new PlaceException(PlaceErrorCode.PLACE_NOT_EXISTS_ERROR));
    }

    @Transactional
    public void updateIncreasePlaceLikeCount(Place place) {
        place.updateLikeCount();
        placeRepository.save(place); // 배타 락이 적용된 상태로 저장
    }

    @Transactional
    public void updateDecreasePlaceLikeCount(Place place) {
        place.decreaseLikeCount();
        placeRepository.save(place); // 배타 락이 적용된 상태로 저장
    }

    @Transactional(readOnly = true)
    public Likes getExistingLike(Member member, Place place) {
        return queryRepository.findLikeByMemberAndPlace(member.getId(), place.getId());
    }

    @Transactional
    public void registerNewLike(Member member, Place place) {
        Likes like = Likes.createLike(new LikeDTO(member, place, LikeStatus.LIKE));
        likeRepository.save(like);
        updateIncreasePlaceLikeCount(place); // 좋아요 수 업데이트
    }

    public void handleLikeUpdate(Likes existingLike, Place place) {
        existingLike.updateLikeStatus();
        likeRepository.save(existingLike);
        updateIncreasePlaceLikeCount(place); // 좋아요 수 업데이트
    }


    public PostLikeResDTO buildPostLikeResDTO(Likes like, Place place, Member member) {
        return PostLikeResDTO.builder()
                .LikeId(like.getId())
                .placeId(place.getId())
                .placeName(place.getPlaceName())
                .memberId(member.getMemberId())
                .build();
    }
}
