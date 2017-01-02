package com.infostructure.mybigbro.model.dto;

import com.google.gson.annotations.SerializedName;

public class CapturedImageDto implements JsonSerialisable {
	
	public CapturedImageDto() {}
		
	@SerializedName("DateTimeCaptured")
	public String dateTimeCaptured;
	@SerializedName("Url")
	public String url;
	
	@Override
	public String serialiseToJson() {
		return "{\"Url\":\"" + this.url + ",\"DateTimeCaptured\":\"" + this.dateTimeCaptured + "\"}";
	}
}
