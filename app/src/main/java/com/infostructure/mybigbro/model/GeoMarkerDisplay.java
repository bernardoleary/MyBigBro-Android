package com.infostructure.mybigbro.model;

public class GeoMarkerDisplay extends GeoMarker {

	private String mServerResponse;
	private String mError;
	
	public String getServerResponse() {
		return this.mServerResponse;
	}
	
	public void setServerReponse(String serverResponse) {
		this.mServerResponse = serverResponse;
	}
	
	public String getError() {
		return this.mError;
	}
	
	public void setError(String error) {
		this.mError = error;
	}
}
