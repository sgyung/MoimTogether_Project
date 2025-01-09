package com.project.moimtogether.domain.place;

import com.project.moimtogether.domain.like.LikeStatus;
import com.project.moimtogether.domain.like.Likes;
import com.project.moimtogether.domain.review.Review;
import com.project.moimtogether.dto.place.PlaceDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "place")
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "place_address")
    private String placeAddress;

    @Column(name = "place_latitude")
    private Double placeLatitude;

    @Column(name = "place_longitude")
    private Double placeLongitude;

    @Column(nullable = false)
    private int likeCount;  // 좋아요 수 필드

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    // 버전 관리 필드 추가
    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(getId(), place.getId()) &&
                Objects.equals(getPlaceName(), place.getPlaceName()) &&
                Objects.equals(getPlaceAddress(), place.getPlaceAddress()) &&
                Objects.equals(getPlaceLatitude(), place.getPlaceLatitude()) &&
                Objects.equals(getPlaceLongitude(), place.getPlaceLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPlaceName(), getPlaceAddress(), getPlaceLatitude(), getPlaceLongitude());
    }

    // 생성 메서드
    public static Place createPlace(PlaceDTO placeDTO){
        Place place = new Place();
        place.setPlaceName(placeDTO.getPlaceName());
        place.setPlaceAddress(placeDTO.getPlaceAddress());
        place.setPlaceLatitude(placeDTO.getPlaceLatitude());
        place.setPlaceLongitude(placeDTO.getPlaceLongitude());

        return place;
    }

    // 수정 메서드
    public void updatePlaceData(PlaceDTO newPlaceDTO) {
        if (newPlaceDTO.getPlaceName() != null && !Objects.equals(this.placeName, newPlaceDTO.getPlaceName())) {
            this.placeName = newPlaceDTO.getPlaceName();
        }
        if (newPlaceDTO.getPlaceAddress() != null && !Objects.equals(this.placeAddress, newPlaceDTO.getPlaceAddress())) {
            this.placeAddress = newPlaceDTO.getPlaceAddress();
        }
        if (newPlaceDTO.getPlaceLatitude() != null && !Objects.equals(this.placeLatitude, newPlaceDTO.getPlaceLatitude())) {
            this.placeLatitude = newPlaceDTO.getPlaceLatitude();
        }
        if (newPlaceDTO.getPlaceLongitude() != null && !Objects.equals(this.placeLongitude, newPlaceDTO.getPlaceLongitude())) {
            this.placeLongitude = newPlaceDTO.getPlaceLongitude();
        }
    }

    // 연관관계 편의 메소드
    public void addReview(Review review){
        reviews.add(review);
        review.setPlace(this);
    }

    public void addLike(Likes like){
        likes.add(like);
        like.setPlace(this);
    }

    // 관계 삭제 메서드
    public void removeLike(Likes like) {
        likes.remove(like);
    }

//    // 좋아요 개수 계산
//    public long getLikeCount() {
//        return likes.stream()
//                .filter(like -> like.getLikeStatus() == LikeStatus.LIKE)
//                .count();
//    }

    // 좋아요 수 증가 메서드
    public void updateLikeCount() {
        this.likeCount++;
    }

    // 좋아요 수 감소 메서드 (예: 좋아요 취소 시)
    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

}
