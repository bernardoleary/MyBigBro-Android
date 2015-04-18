package com.infostructure.mybigbro.utils;

import com.infostructure.mybigbro.model.dto.GeoMarkerDto;

/**
 * Created by Admin on 18/04/2015.
 */
public class Globals {

    private static GeoMarkerDto geoMarkerDto = null;

    public static GeoMarkerDto getGetCurrentLocation() {
        return Globals.geoMarkerDto;
    }

    public static void setGetCurrentLocation(GeoMarkerDto geoMarkerDto) {
        Globals.geoMarkerDto = geoMarkerDto;
    }
}
