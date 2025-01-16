package com.project.moimtogether.service.place;

import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.dto.place.PlaceDTO;
import com.project.moimtogether.dto.place.RestClientResponse;
import com.project.moimtogether.repository.PlaceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RestClientService {

    private final RestClient restClient;
    private final PlaceRepository placeRepository;

    @PersistenceContext
    private EntityManager entityManager;  // EntityManager 주입

    private static final String API_KEY = "7841686f66746a733131366d45427846";
    private static final String API_TYPE = "json";
    private static final String API_SERVICE_RESTAURANT = "LOCALDATA_072405";
    private static final String API_SERVICE_PARK = "SearchParkInfoService";

    // 서비스에서 사용할 상수들
    private static final List<String> API_SERVICES = List.of(API_SERVICE_RESTAURANT, API_SERVICE_PARK);

    // 배치 사이즈 상수
    private static final int BATCH_SIZE_RESTAURANT = 1000;

    // 서울 데이터를 가져오고 저장
    public void getSeoulData(Integer startIndex, Integer endIndex) {
        // 음식점 데이터를 배치로 처리
        if (API_SERVICES.contains(API_SERVICE_RESTAURANT)) {
            processRestaurantData(startIndex, endIndex);
        }

        // 공원 데이터는 한 번에 처리
        if (API_SERVICES.contains(API_SERVICE_PARK)) {
            List<PlaceDTO> parkData = fetchDataFromApi(API_SERVICE_PARK, 1, 100);
            saveAllPlaces(parkData); // 배치 저장 없이 한 번에 저장
        }
    }

    // 스케줄러 추가: 매주 월요일 새벽 6시에 동작
    @Scheduled(cron = "0 0 6 ? * MON")
    public void scheduleSeoulDataUpdate() {
        log.info("스케줄러 실행: 서울 데이터 업데이트 시작");
        try {
            getSeoulData(1, 5000); // API 호출 범위 지정
        } catch (Exception e) {
            log.error("스케줄러 실행 중 오류 발생: ", e);
        }
        log.info("스케줄러 완료: 서울 데이터 업데이트 종료");
    }

    // 개별 API 호출 메서드
    private List<PlaceDTO> fetchDataFromApi(String apiService, Integer startIndex, Integer endIndex) {
        String url = "/" + API_KEY + "/" + API_TYPE + "/" + apiService + "/" + startIndex + "/" + endIndex;
        RestClientResponse restClientResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder.path(url).build())
                .retrieve()
                .body(RestClientResponse.class);

        if (restClientResponse.getLocalData() != null) {
            return restClientResponse.getLocalData().getRows().stream()
                    .map(row -> new PlaceDTO(row.getPlaceName(),
                            row.getPlaceAddress(),
                            row.getPlaceLatitude(),
                            row.getPlaceLongitude()))
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    // 음식점 데이터를 배치 단위로 처리하는 메서드
    public void processRestaurantData(Integer startIndex, Integer endIndex) {
        for(int i = startIndex; i <= endIndex; i+=BATCH_SIZE_RESTAURANT) {
            int batchStart = i;
            int batchEnd = Math.min(i + BATCH_SIZE_RESTAURANT - 1, endIndex);

            List<PlaceDTO> restaurantData = fetchDataFromApi(API_SERVICE_RESTAURANT, batchStart, batchEnd);

            saveInBatches(restaurantData);
        }
    }

    // 데이터를 배치 단위로 저장하는 메서드
    public void saveInBatches(List<PlaceDTO> placeDTOs) {
        for (PlaceDTO placeDTO : placeDTOs) {
            saveOrUpdatePlace(placeDTO);
        }

        placeRepository.flush();
        entityManager.clear();
    }

    // 데이터를 한 번에 저장하는 메서드 (공원과 같은 소규모 데이터에 사용)
    private void saveAllPlaces(List<PlaceDTO> placeDTOs) {
        for (PlaceDTO placeDTO : placeDTOs) {
            saveOrUpdatePlace(placeDTO); // 기존 데이터가 있으면 업데이트, 없으면 새로 저장
        }
    }

    // PlaceDTO를 Place 엔티티로 변환하고 저장하는 메서드
    private void saveOrUpdatePlace(PlaceDTO placeDTO) {
        Place existingPlace = placeRepository.findByPlaceAddress(placeDTO.getPlaceAddress())
                .orElse(null);

        if (existingPlace == null) {
            placeRepository.save(Place.createPlace(placeDTO));
        } else {
            existingPlace.updatePlaceData(placeDTO);
            placeRepository.save(existingPlace);
        }
    }

}
