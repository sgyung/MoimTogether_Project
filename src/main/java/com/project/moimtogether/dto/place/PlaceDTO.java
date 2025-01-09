package com.project.moimtogether.dto.place;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "장소 정보 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceDTO {

    @JsonAlias({"P_PARK", "BPLCNM"}) // P_PARK 또는 BPLCNM을 placeName에 매핑
    private String placeName;

    @JsonAlias({"P_ADDR", "RDNWHLADDR"}) // P_ADDR 또는 RDNWHLADDR을 placeAddress에 매핑
    private String placeAddress;

    @JsonAlias({"LONGITUDE", "X"}) // LONGITUDE 또는 X를 placeLongitude에 매핑
    private Double placeLongitude;

    @JsonAlias({"LATITUDE", "Y"}) // LATITUDE 또는 Y를 placeLatitude에 매핑
    private Double placeLatitude;
}
