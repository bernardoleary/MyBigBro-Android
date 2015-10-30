package com.infostructure.mybigbro.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.infostructure.mybigbro.model.CapturedImage;
import com.infostructure.mybigbro.model.GeoMarker;
import com.infostructure.mybigbro.model.GeoMarkerDisplay;
import com.infostructure.mybigbro.model.dto.CapturedImageDto;
import com.infostructure.mybigbro.model.dto.GeoMarkerDto;

public class Mapper {

	public GeoMarkerDto GeoMarkerToGeoMarkerDto(GeoMarker geoMarker, String deviceName) {
		GeoMarkerDto geoMarkerDto = GeoMarkerToGeoMarkerDto(geoMarker);
		geoMarkerDto.deviceName = deviceName;
		return geoMarkerDto;
	}
	
	public GeoMarkerDto GeoMarkerToGeoMarkerDto(GeoMarker geoMarker) {
		GeoMarkerDto geoMarkerDto = new GeoMarkerDto();
		geoMarkerDto.markerDateTime = android.text.format.DateFormat.format("dd MMM yyyy kk:mm:ss", geoMarker.getMarkerDateTime()).toString();
        geoMarkerDto.xCoord = geoMarker.getXCoord();
        geoMarkerDto.yCoord = geoMarker.getYCoord();
        return geoMarkerDto;
    }

	public CapturedImageDto CapturedImageToCapturedImageDto(CapturedImage capturedImage) {
		CapturedImageDto capturedImageDto = new CapturedImageDto();
		capturedImageDto.dateTimeCaptured = capturedImage.getDateTimeCaptured().toGMTString();
		capturedImageDto.url = capturedImage.getUrl();
        return capturedImageDto;
    }
	
	public GeoMarkerDisplay GeoMarkerToGeoMarkerDisplay(GeoMarker geoMarker) {
		GeoMarkerDisplay geoMarkerDisplay = new GeoMarkerDisplay();
		geoMarkerDisplay.setMarkerDateTime(geoMarker.getMarkerDateTime());
		geoMarkerDisplay.setXCoord(geoMarker.getXCoord());
		geoMarkerDisplay.setYCoord(geoMarker.getYCoord());
        return geoMarkerDisplay;
    }

	@SuppressWarnings("deprecation")
	public CapturedImage CapturedImageDtoToCapturedImage(CapturedImageDto capturedImageDto) {
		CapturedImage capturedImage = new CapturedImage();
		capturedImage.setDateTimeCaptured(new Date(capturedImageDto.dateTimeCaptured));
		capturedImage.setUrl(capturedImageDto.url);
        return capturedImage;
    }
}
