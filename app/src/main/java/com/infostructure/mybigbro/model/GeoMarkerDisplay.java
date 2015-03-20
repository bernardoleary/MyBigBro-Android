package com.infostructure.mybigbro.model;

public class GeoMarkerDisplay extends GeoMarker {

	private String serverResponse;
	private String error;
	
	public String getServerResponse() {
		return this.serverResponse;
	}
	
	public void setServerReponse(String serverResponse) {
		this.serverResponse = serverResponse;
	}
	
	public String getError() {
		return this.error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
}
