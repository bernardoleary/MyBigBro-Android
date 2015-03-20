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

	private EditText editTextPollFrequency = null;
	private EditText editDeviceName = null;
	private Button buttonSave = null;
	private DataAccessService dataAccessService = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		// setup our data access manager
		dataAccessService = DataAccessService.getInstance();
        dataAccessService.setApplicationContext(getApplicationContext());
		
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
		buttonSave = (Button)findViewById(R.id.buttonSave);
		editTextPollFrequency = (EditText)findViewById(R.id.editTextPollFrequency);
		editDeviceName = (EditText)findViewById(R.id.editDeviceName);
		
		// save button event
		buttonSave.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				saveSettings();
			}
		});
		
		// setup the textbox contents
		long pollFrequency = dataAccessService.getPollFrequency();
		editTextPollFrequency.setText(Long.toString(pollFrequency));
		String deviceName = dataAccessService.getDeviceName();
		editDeviceName.setText(deviceName);
	}
	
	private void saveSettings() {
		long pollFrequency = Long.parseLong(editTextPollFrequency.getText().toString());
		String deviceName = editDeviceName.getText().toString();
		try {
			//dataAccess.setUserCredentials(userCrednetials);
			dataAccessService.setPollFrequency(pollFrequency);
			dataAccessService.setDeviceName(deviceName);
			
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
