package com.infostructure.mybigbro.ui.activities;

import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.model.UserCredentials;
import com.infostructure.mybigbro.services.DataAccessService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends ActionBarActivity {

	private EditText mEditTextPollFrequency = null;
    private Spinner mSpinnerPollFrequency = null;
	private EditText mEditDeviceName = null;
	private Button mButtonSave = null;
	private DataAccessService mDataAccessService = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

	public void initControls() throws Exception {
		
		// setup the controls
		this.mButtonSave = (Button)findViewById(R.id.buttonSave);
        this.mEditDeviceName = (EditText)findViewById(R.id.editDeviceName);
        this.mSpinnerPollFrequency = (Spinner)findViewById(R.id.spinnerPollFrequency);
		
		// save button event
        this.mButtonSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveSettings();
			}
		});
		
		// setup the textbox contents
		long pollFrequency = mDataAccessService.getPollFrequency();
		mSpinnerPollFrequency.setSelection(setSpinnerPollFrequency(pollFrequency));
		String deviceName = mDataAccessService.getDeviceName();
		mEditDeviceName.setText(deviceName);
	}
	
	private void saveSettings() {
		long pollFrequency = getSpinnerPollFrequency();
		String deviceName = mEditDeviceName.getText().toString();
		try {
			mDataAccessService.setPollFrequency(getSpinnerPollFrequency());
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

    private long getSpinnerPollFrequency() {
        if (mSpinnerPollFrequency.getSelectedItemPosition() == 0) {
            return 5000;
        } else if (mSpinnerPollFrequency.getSelectedItemPosition() == 1) {
            return 10000;
        } else if (mSpinnerPollFrequency.getSelectedItemPosition() == 2) {
            return 30000;
        } else if (mSpinnerPollFrequency.getSelectedItemPosition() == 3) {
            return 60000;
        } else {
            return 10000; // Set 10 seconds by default
        }
    }

    private int setSpinnerPollFrequency(long pollFrequency) {
        if (pollFrequency <= 5000) {
            return 0;
        } else if (pollFrequency <= 10000) {
            return 1;
        } else if (pollFrequency <= 30000) {
            return 2;
        } else if (pollFrequency > 30000) {
            return 3;
        } else {
            return 1; // Set 10 seconds by default
        }
    }
}
