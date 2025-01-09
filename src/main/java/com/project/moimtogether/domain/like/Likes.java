package com.project.moimtogether.domain.like;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.dto.like.LikeDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "likes")
public class Likes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_status", nullable = false)
    private LikeStatus likeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    // 생성 메서드
    public static Likes createLike(LikeDTO likeDTO){
        Likes like = new Likes();
        like.setLikeStatus(likeDTO.getLikeStatus());
        like.setMember(likeDTO.getMember());
        like.setPlace(likeDTO.getPlace());
        like.getPlace().addLike(like);
        return like;
    }

    // 상태 업데이트 메서드
    public void updateLikeStatus(){
        if(likeStatus == LikeStatus.LIKE){
            likeStatus = LikeStatus.CANCEL;
            place.removeLike(this);
        }else{
            likeStatus = LikeStatus.LIKE;
            place.addLike(this);
        }
    }
}
