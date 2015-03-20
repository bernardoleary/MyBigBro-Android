package com.infostructure.mybigbro.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageService {
	
	private DataAccessService dataAccessService;

	public ImageService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}
	
	public Bitmap getLatestBitmapImage() { 
		String url = null;
		try {
			url = dataAccessService.getLastestImageUrl();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return bitmap;
	}
}
