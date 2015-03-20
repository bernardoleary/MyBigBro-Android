package com.infostructure.mybigbro.services;

import java.util.Calendar;

import com.infostructure.mybigbro.model.GeoMarker;
import com.infostructure.mybigbro.model.GeoMarkerDisplay;
import com.infostructure.mybigbro.utils.Mapper;

import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.util.Log;

public class LocationService {
	
	private DataAccessService dataAccessService;

	public LocationService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

	public GeoMarkerDisplay locationChanged(Location loc) {		
	    loc.getLatitude();
	    loc.getLongitude();                  	        
	    GeoMarker geoMarker = new GeoMarker();	    
	    geoMarker.setMarkerDateTime(Calendar.getInstance().getTime());
	    geoMarker.setXCoord(loc.getLongitude());
	    geoMarker.setYCoord(loc.getLatitude());	     
	    Mapper mapper = new Mapper();
	    GeoMarkerDisplay geoMarkerDisplay = mapper.GeoMarkerToGeoMarkerDisplay(geoMarker);	    
	    try {			
			// Call the web method, capture any exception information and print
	    	geoMarkerDisplay.setServerReponse(dataAccessService.createGeoMarker(geoMarker));
		} catch (Exception e) {
			geoMarkerDisplay.setError(e.getMessage());				
		}		    	    
		return geoMarkerDisplay;
	}
}
