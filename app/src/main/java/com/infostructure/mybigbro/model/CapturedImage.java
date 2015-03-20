package com.infostructure.mybigbro.model;

import java.util.Date;

public class CapturedImage {

	private String url;
	private Date dateTimeCaptured;
	
	public String getUrl()
	{
		return this.url;
	}
	
	public Date getDateTimeCaptured()
	{
		return this.dateTimeCaptured;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public void setDateTimeCaptured(Date dateTimeCaptured)
	{
		this.dateTimeCaptured = dateTimeCaptured;
	}
}
