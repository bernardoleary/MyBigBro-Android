package com.infostructure.mybigbro.model.dto;

import com.google.gson.annotations.SerializedName;

public class WebCamDto implements JsonSerialisable {
	
	public WebCamDto() {}
	
	@SerializedName("Name")
	public String Name;
	@SerializedName("Url")
	public String Url;
	
	@Override
	public String serialiseToJson() {
		return "{\"Name\":" + this.Name + ",\"Url\":" + this.Url + "}";
	}
}
