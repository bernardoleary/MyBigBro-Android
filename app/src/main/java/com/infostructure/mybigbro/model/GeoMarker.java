package com.infostructure.mybigbro.model;

import java.util.Date;

public class GeoMarker {

	private Date mMarkerDateTime;
	private double mXCoord;
	private double mYCoord;
	
	public Date getMarkerDateTime()
	{
		return this.mMarkerDateTime;
	}
	
	public double getXCoord()
	{
		return this.mXCoord;
	}
	
	public double getYCoord()
	{
		return this.mYCoord;
	}
	
	public void setMarkerDateTime(Date markerDateTime)
	{
		this.mMarkerDateTime = markerDateTime;
	}
	
	public void setXCoord(double xCoord)
	{
		this.mXCoord = xCoord;
	}
	
	public void setYCoord(double yCoord)
	{
		this.mYCoord = yCoord;
	} 
}
