package com.infostructure.mybigbro.services;

import com.infostructure.mybigbro.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.lang.String;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
//import com.infostructure.simplelist.model.SimpleList;
//import com.infostructure.simplelist.model.SimpleListItem;
//import com.infostructure.simplelist.model.UserCredentials;
//import com.infostructure.simplelist.model.dto.SimpleListDto;
//import com.infostructure.simplelist.model.dto.SimpleListItemDto;
import com.infostructure.mybigbro.model.GeoMarker;
import com.infostructure.mybigbro.model.UserCredentials;
import com.infostructure.mybigbro.model.dto.CapturedImageDto;
import com.infostructure.mybigbro.model.dto.CapturedImageGeoMarkerDto;
import com.infostructure.mybigbro.model.dto.GeoMarkerDto;
import com.infostructure.mybigbro.model.dto.WebCamDto;
import com.infostructure.mybigbro.model.dto.WebCamExtendedInfoDto;
import com.infostructure.mybigbro.utils.Mapper;

public class DataAccessService {

	// http://developer.android.com/guide/appendix/faq/commontasks.html#localhostalias
	// use the alias "10.0.2.2" instead of "localhost" or "127.0.0.1"
	// the port in the TEST URL needs to be changed sometimes, based on which port IIS express is using
	
	/* == */
	/* TEST */
    // Default Android emulator
	//private final String URL = "http://10.0.2.2:58000/api/";
    // Genymotion Android emulator: http://blog.zeezonline.com/2013/11/access-localhost-from-genymotion/
    //private final String URL = "http://10.0.3.2:58000/api/";
	/* == */
	/* PROD */
	/* == */
	private final String URL = "http://www.mybigbro.tv/api/";
	/* == */
	
	private final String PREFS_FILE_NAME = "prefs";
	private final String FORWARD_SLASH = "/";
	private final String QUESTION_MARK = "?";
	private final String AMPERSAND = "&";
	private final String EQUALS = "=";
	private final String GEOMARKERS = "geomarkers";
	private final String WEBCAM = "webcams";
	private final String CAPTURED_IMAGES = "capturedimages";
	private final String NEAREST = "nearestmany";
    private final String LATEST = "latest";
	private final String SETTINGS_FILE_NAME = "settings";
	private final String TOP = "top";
    private final String COUNT = "count";
	private final String DEVICE_NAME = "deviceName";
    private final String POLL_FREQUENCY = "pollFrequency";
	private final String USER_NAME = "userName";
	private final String PASSWORD = "password";
    private final String RANDOM_WORD_URL = "http://randomword.setgetgo.com/get.php";
	private Context mApplicationContext = null;

    // Set up singleton
    private static DataAccessService instance = null;
	
	protected DataAccessService() {}

    public static DataAccessService getInstance() {
        if (instance == null) {
            instance = new DataAccessService();
        }
        instance.setStrictModeThreadPolicyOff();
        return instance;
    }

    public void setStrictModeThreadPolicyOff() {
        /*
        Turn "StrictMode" off:
        http://blog.vogella.com/2012/02/22/android-strictmode-networkonmainthreadexception/
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        /***/
    }

    public void setApplicationContext(Context applicationContext) {
        this.mApplicationContext = applicationContext;
    }

	public String createGeoMarker(GeoMarker geoMarker) throws Exception {
		String geoMarkersUrl = URL + GEOMARKERS;
		WebService webService = new WebService(geoMarkersUrl);
		// Pass data if needed.
		Mapper mapper = new Mapper();
		GeoMarkerDto geoMarkerDto = mapper.GeoMarkerToGeoMarkerDto(geoMarker);
		// Get the name of this device
		geoMarkerDto.deviceName = getDeviceName();
		//Get JSON response from server the "" are where the method name would normally go if needed example
		return webService.webInvoke("", new HashMap<String, String>(), geoMarkerDto);
	}
	
	public WebCamExtendedInfoDto[] getNearestManyWebCams(int top) throws Exception {
		String webCamsUrl = URL + WEBCAM + FORWARD_SLASH + NEAREST + QUESTION_MARK + TOP + EQUALS + top + AMPERSAND + DEVICE_NAME + EQUALS + getDeviceName();
		WebService webService = new WebService(webCamsUrl);
		// Query the service
		String result = webService.webGet("", new HashMap<String, String>());
		// Get image URL
		Gson gson = new GsonBuilder().create();
		WebCamExtendedInfoDto[] webCamDtos = gson.fromJson(result, WebCamExtendedInfoDto[].class);
		return webCamDtos;
	}
	
	public String getLastestImageUrl() throws Exception {
		String geoMarkersCapturedImagesUrl = URL + GEOMARKERS + FORWARD_SLASH + CAPTURED_IMAGES + QUESTION_MARK + TOP + EQUALS + "1" + AMPERSAND + DEVICE_NAME + EQUALS + getDeviceName();
		WebService webService = new WebService(geoMarkersCapturedImagesUrl);
		// Query the service
		String result = webService.webGet("", new HashMap<String, String>());
		// Get image URL
		Gson gson = new GsonBuilder().create();
		CapturedImageGeoMarkerDto[] capturedImageGeoMarkerDtos = gson.fromJson(result, CapturedImageGeoMarkerDto[].class);
		return capturedImageGeoMarkerDtos[0].capturedImage.url; //.getCapturedImage().getUrl();
	}

    public CapturedImageDto getLastestImage() throws Exception {
        String geoMarkersCapturedImagesUrl = URL + GEOMARKERS + FORWARD_SLASH + CAPTURED_IMAGES + QUESTION_MARK + TOP + EQUALS + "1" + AMPERSAND + DEVICE_NAME + EQUALS + getDeviceName();
        WebService webService = new WebService(geoMarkersCapturedImagesUrl);
        // Query the service
        String result = webService.webGet("", new HashMap<String, String>());
        // Get image URL
        Gson gson = new GsonBuilder().create();
        CapturedImageGeoMarkerDto[] capturedImageGeoMarkerDtos = gson.fromJson(result, CapturedImageGeoMarkerDto[].class);
        return capturedImageGeoMarkerDtos[0].capturedImage; //.getCapturedImage().getUrl();
    }

    public int getGetCountOfMarkersWithImage() throws Exception {
        String geoMarkersCapturedImagesUrl = URL + GEOMARKERS + FORWARD_SLASH + CAPTURED_IMAGES + FORWARD_SLASH + COUNT + QUESTION_MARK + DEVICE_NAME + EQUALS + getDeviceName();
        WebService webService = new WebService(geoMarkersCapturedImagesUrl);
        // Query the service
        String result = webService.webGet("", new HashMap<String, String>());
        // Return count
        return Integer.valueOf(result);
    }

    public GeoMarkerDto getLastestGeoMarker() throws Exception {
        String geoMarkersCapturedImagesUrl = URL + GEOMARKERS + FORWARD_SLASH + LATEST + QUESTION_MARK + DEVICE_NAME + EQUALS + getDeviceName();
        WebService webService = new WebService(geoMarkersCapturedImagesUrl);
        // Query the service
        String result = webService.webGet("", new HashMap<String, String>());
        // Get image URL
        Gson gson = new GsonBuilder().create();
        GeoMarkerDto geoMarkerDto = gson.fromJson(result, GeoMarkerDto.class);
        return geoMarkerDto; //.getCapturedImage().getUrl();
    }
	
	public UserCredentials getUserCredentials() throws Exception {
		// set user credentials and return
		UserCredentials userCredentials = new UserCredentials();
		SharedPreferences settings = getSharedPreferences();
		userCredentials.setUserName(settings.getString(USER_NAME, ""));
		userCredentials.setPassword(settings.getString(PASSWORD, ""));
		return userCredentials;
	}
	
	public void setUserCredentials(UserCredentials userCredentials) throws Exception {
		// We need an Editor object to make preference changes.
		SharedPreferences settings = getSharedPreferences();
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(USER_NAME, userCredentials.getUserName());
		editor.putString(PASSWORD, userCredentials.getPassword());
		// Commit the edits!
		editor.commit();
	}

	public long getPollFrequency() throws Exception {
		// set user credentials and return		
		SharedPreferences settings = getSharedPreferences();
		long pollFrequency = settings.getLong(POLL_FREQUENCY, 10000);
		// Divide by 1000 for display
		return pollFrequency;
	}
	
	public void setPollFrequency(long pollFrequency) throws Exception {
		// We need an Editor object to make preference changes.
		SharedPreferences settings = getSharedPreferences();
		SharedPreferences.Editor editor = settings.edit();
		// Multiply by 1000 for storage
		editor.putLong(POLL_FREQUENCY, pollFrequency);
		// Commit the edits!
		editor.commit();
	}

	public String getDeviceName() throws Exception {		
		// Try to find device name in shared preferences...
		SharedPreferences settings = getSharedPreferences();
        String defaultDeviceName = "default";
        String deviceName = settings.getString(DEVICE_NAME, defaultDeviceName);
        // If it's not in shared preferences try to get a random word...
        if (defaultDeviceName == deviceName) {
            WebService webService = new WebService(RANDOM_WORD_URL);
            String randomWord = webService.webGet("", new HashMap<String, String>());
            if (randomWord != "") {
                deviceName = randomWord.trim();
                this.setDeviceName(deviceName);
            }
        }
        // If it's not in shared preferences and we weren't able to get a random word set to a UUID...
        if (defaultDeviceName == deviceName) {
            String randomString = UUID.randomUUID().toString();
            deviceName = randomString;
            this.setDeviceName(deviceName);
        }
        // Return the device name...
		return deviceName;		
	}
		
	public void setDeviceName(String deviceName) throws Exception {		
		// We need an Editor object to make preference changes.
		SharedPreferences settings = getSharedPreferences();
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DEVICE_NAME, deviceName);
		// Commit the edits!
		editor.commit();		
	}
	
	private Map<String, String> getUserCredentialsHashMap() throws Exception {
		// collect user credentials from external storage
		UserCredentials credentials = getUserCredentials();
		String userName = credentials.getUserName();
		String password = credentials.getPassword();
		// Pass the parameters if needed , if not then pass dummy one as follows
		Map<String, String> params = new HashMap<String, String>();
		params.put(USER_NAME, userName);
		params.put(PASSWORD, password);
		return params;
	}

    private SharedPreferences getSharedPreferences() {
        SharedPreferences settings = this.mApplicationContext.getSharedPreferences(PREFS_FILE_NAME, 0);
        return settings;
    }
}
