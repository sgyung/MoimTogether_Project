package com.project.moimtogether.dto.place;

import com.project.moimtogether.domain.place.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "특정 장소 조회 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPlaceDTO {

    private Long placeId;
    private String placeName;
    private String placeAddress;
    private Double placeLatitude;
    private Double placeLongitude;
    private Long likeCount;
    private List<PlaceReviewDTO> reviews;

    public SearchPlaceDTO(Place place) {
        this.placeId = place.getId();
        this.placeName = place.getPlaceName();
        this.placeAddress = place.getPlaceAddress();
        this.placeLatitude = place.getPlaceLatitude();
        this.placeLongitude = place.getPlaceLongitude();
//        this.likeCount = place.getLikeCount();
        this.reviews = place.getReviews().stream()
                .map(review -> new PlaceReviewDTO(review))
                .collect(Collectors.toList());
    }

}
