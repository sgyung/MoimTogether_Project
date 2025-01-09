package com.project.moimtogether.config.error.customException;

import com.project.moimtogether.domain.member.UserErrorCode;
import com.project.moimtogether.domain.place.PlaceErrorCode;
import lombok.Getter;

@Getter
public class PlaceException extends RuntimeException {

    private final PlaceErrorCode placeErrorCode;

    public PlaceException(PlaceErrorCode placeErrorCode) {
        super(placeErrorCode.getPlaceErrorMsg());
        this.placeErrorCode = placeErrorCode;
    }

    public PlaceException(String message, PlaceErrorCode placeErrorCode) {
        super(message);
        this.placeErrorCode = placeErrorCode;
    }
}
