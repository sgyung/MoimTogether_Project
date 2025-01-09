package com.project.moimtogether.dto.place;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestClientResponse {
    private LocalData localData;

    @JsonAnySetter
    public void setDynamicField(String key, LocalData value) {
        this.localData = value; // LOCALDATA_030505, LOCALDATA_010101 등 어떤 키라도 매핑
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LocalData {

        @JsonProperty("row")
        private List<PlaceDTO> rows;

    }
}
