package com.infostructure.mybigbro.ui.activities;

import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.abstractions.UserInterface;
import com.infostructure.mybigbro.model.UserCredentials;
import com.infostructure.mybigbro.services.DataAccessService;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity implements UserInterface {	

	private EditText mEditTextPollFrequency = null;
	private EditText mEditDeviceName = null;
	private Button mButtonSave = null;
	private DataAccessService mDataAccessService = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		// setup our data access manager
        mDataAccessService = DataAccessService.getInstance();
        mDataAccessService.setApplicationContext(getApplicationContext());
		
		// initialise UI
		try {
			initControls();
		} catch (Exception e) {
			Log.d("Error: ", e.toString());
			return;
		}
	}
	
	@Override
	public void initControls() throws Exception {
		
		// setup the controls
		mButtonSave = (Button)findViewById(R.id.buttonSave);
		mEditTextPollFrequency = (EditText)findViewById(R.id.editTextPollFrequency);
		mEditDeviceName = (EditText)findViewById(R.id.editDeviceName);
		
		// save button event
		mButtonSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveSettings();
			}
		});
		
		// setup the textbox contents
		long pollFrequency = mDataAccessService.getPollFrequency();
		mEditTextPollFrequency.setText(Long.toString(pollFrequency));
		String deviceName = mDataAccessService.getDeviceName();
		mEditDeviceName.setText(deviceName);
	}
	
	private void saveSettings() {
		long pollFrequency = Long.parseLong(mEditTextPollFrequency.getText().toString());
		String deviceName = mEditDeviceName.getText().toString();
		try {
			//dataAccess.setUserCredentials(userCrednetials);
			mDataAccessService.setPollFrequency(pollFrequency);
			mDataAccessService.setDeviceName(deviceName);
			
			Context context = getApplicationContext();
			CharSequence text = "Settings Saved...";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			
		} catch (Exception e) {
			Log.d("Error: ", e.toString());
			return;
		}
	}
}
