package com.infostructure.mybigbro.model.dto;

import com.google.gson.annotations.SerializedName;

public class CapturedImageGeoMarkerDto implements JsonSerialisable {
	
	public CapturedImageGeoMarkerDto() {}
	
	@SerializedName("CapturedImage")
	public CapturedImageDto capturedImage;
	@SerializedName("GeoMarker")
	public GeoMarkerDto geoMarker;
	
	@Override
	public String serialiseToJson() {
		return "{\"GeoMarker\":" + this.capturedImage.serialiseToJson() + ",\"CapturedImage\":" + this.geoMarker.serialiseToJson() + "}";
	}
}
