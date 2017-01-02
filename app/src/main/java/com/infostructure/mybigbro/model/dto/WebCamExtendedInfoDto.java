package com.infostructure.mybigbro.model.dto;

import com.google.gson.annotations.SerializedName;

public class WebCamExtendedInfoDto implements JsonSerialisable {
	
	public WebCamExtendedInfoDto() {}
	
	@SerializedName("Name")
	public String Name;
	@SerializedName("Url")
	public String Url;
	@SerializedName("Distance")
	public double Distance;
    @SerializedName("XCoord")
    public double XCoord;
    @SerializedName("YCoord")
    public double YCoord;
	
	@Override
	public String serialiseToJson() {
		return "{\"Name\":" + this.Name + ",\"Url\":" + this.Url + ",\"Distance\":" + this.Distance + ",\"XCoord\":" + this.XCoord + ",\"YCoord\":" + this.YCoord + "}";
	}
}