package com.infostructure.mybigbro.model.dto;

import com.google.gson.annotations.SerializedName;

public class GeoMarkerDto implements JsonSerialisable {

	public GeoMarkerDto() {}
		
	@SerializedName("DeviceName")
	public String deviceName;
	@SerializedName("MarkerDateTime")
	public String markerDateTime;
	@SerializedName("XCoord")
	public double xCoord;
	@SerializedName("YCoord")
	public double yCoord;
	
	@Override
	public String serialiseToJson() {
		return "{\"MarkerDateTime\":\"" + this.markerDateTime + "\",\"XCoord\":" + this.xCoord + ",\"YCoord\":" + this.yCoord + ",\"DeviceName\":\"" + this.deviceName + "\"}";
	}
}

