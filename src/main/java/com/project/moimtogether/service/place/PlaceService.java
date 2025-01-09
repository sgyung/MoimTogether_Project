package com.project.moimtogether.service.place;

import com.project.moimtogether.config.error.customException.PlaceException;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.domain.place.PlaceErrorCode;
import com.project.moimtogether.dto.place.PlaceDTO;
import com.project.moimtogether.dto.place.SearchPlaceDTO;
import com.project.moimtogether.repository.PlaceRepository;
import com.project.moimtogether.repository.query.QueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final QueryRepository queryRepository;

    // 모든 장소 조회 서비스 메서드
    public List<PlaceDTO> findAllPlaces() {

        List<Place> places = placeRepository.findAll();

        if(places.isEmpty()){
            throw new PlaceException(PlaceErrorCode.PLACE_EMPTY_ERROR);
        }

        try {
            List<PlaceDTO> placeDTO = places.stream()
                    .map(place -> PlaceDTO.builder()
                            .placeName(place.getPlaceName())
                            .placeAddress(place.getPlaceAddress())
                            .placeLatitude(place.getPlaceLatitude())
                            .placeLongitude(place.getPlaceLongitude())
                            .build())
                    .toList();

            return placeDTO;
        } catch (Exception e) {
            throw new PlaceException(PlaceErrorCode.UNEXPECTED_ERROR);
        }
    }

    // 특정 장소 조회 서비스 메서드
    public SearchPlaceDTO findPlaceService(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceException(PlaceErrorCode.LIKE_EXISTS_ERROR));

        return new SearchPlaceDTO(place);
    }
}
