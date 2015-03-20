package com.infostructure.mybigbro.model;

import java.util.Date;

public class GeoMarker {

	private Date markerDateTime;
	private double xCoord;
	private double yCoord;
	
	public Date getMarkerDateTime()
	{
		return this.markerDateTime;
	}
	
	public double getXCoord()
	{
		return this.xCoord;
	}
	
	public double getYCoord()
	{
		return this.yCoord;
	}
	
	public void setMarkerDateTime(Date markerDateTime)
	{
		this.markerDateTime = markerDateTime;
	}
	
	public void setXCoord(double xCoord)
	{
		this.xCoord = xCoord;
	}
	
	public void setYCoord(double yCoord)
	{
		this.yCoord = yCoord;
	} 
}
