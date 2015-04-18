package com.infostructure.mybigbro.ui.fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.services.DataAccessService;
import com.infostructure.mybigbro.services.GeoMarkerService;
import com.infostructure.mybigbro.ui.OnFragmentInteractionListener;
import com.infostructure.mybigbro.ui.activities.SettingsActivity;

public class MainFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mSectionNumber;
    private OnFragmentInteractionListener mListener;
    private LocationManager mLocationManager;
    ToggleButton mToggleButton;
    TextView mTextViewMessageRunning1;
    TextView mTextViewMessageRunning2;
    TextView mTextViewMessagePaused1;
    TextView mTextViewMessagePaused2;
    TextView mTextViewMessageRunningDeviceName;
    TextView mTextViewMessagePausedDeviceName;

    // Services
    private DataAccessService mDataAccessService;

    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainFragment() {

    }

    private boolean isGeoMarkerServiceRunning() {
        ActivityManager manager = (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (GeoMarkerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        /* Create the services that we need */
        this.mDataAccessService = DataAccessService.getInstance();
        this.mDataAccessService.setApplicationContext(this.getActivity().getApplicationContext());

        /* GPS Enabled check */
        this.mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!this.mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showGPSDisabledAlertToUser();
                    // start time consuming background process here
                }
            }, 2000); // starting it in 2 seconds...
        }

        this.mTextViewMessageRunning1 = (TextView)rootView.findViewById(R.id.textViewMessageRunning1);
        this.mTextViewMessageRunning2 = (TextView)rootView.findViewById(R.id.textViewMessageRunning2);
        this.mTextViewMessagePaused1 = (TextView)rootView.findViewById(R.id.textViewMessagePaused1);
        this.mTextViewMessagePaused2 = (TextView)rootView.findViewById(R.id.textViewMessagePaused2);
        this.mTextViewMessageRunningDeviceName = (TextView)rootView.findViewById(R.id.textViewMessageRunningDeviceName);
        this.mTextViewMessagePausedDeviceName = (TextView)rootView.findViewById(R.id.textViewMessagePausedDeviceName);
        this.mToggleButton = (ToggleButton)rootView.findViewById(R.id.toggleButton);
        this.mToggleButton.setChecked(isGeoMarkerServiceRunning());
        //text = (TextView)rootView.findViewById(R.id.textView1);
        //text.setText("Status: " + isGeoMarkerServiceRunning());
        this.mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        getActivity().startService(new Intent(getActivity(), GeoMarkerService.class));
                    } else {
                        mToggleButton.setChecked(false);
                        showGPSDisabledAlertToUser();
                    }
                } else {
                    getActivity().stopService(new Intent(getActivity(), GeoMarkerService.class));
                }
                setTextViewRunningOrPaused();
            }
        });

        /* Set message visiblity */
        this.setTextViewRunningOrPaused();

        /* Return the view */
        return rootView;
    }

    private void setTextViewRunningOrPaused() {
        boolean isRunning = this.mToggleButton.isChecked();
        this.mTextViewMessageRunning1.setVisibility(isRunning ? View.VISIBLE : View.GONE);
        this.mTextViewMessageRunning2.setVisibility(isRunning ? View.VISIBLE : View.GONE);
        this.mTextViewMessageRunningDeviceName.setVisibility(isRunning ? View.VISIBLE : View.GONE);
        this.mTextViewMessagePaused1.setVisibility(isRunning ? View.GONE : View.VISIBLE);
        this.mTextViewMessagePaused2.setVisibility(isRunning ? View.GONE : View.VISIBLE);
        this.mTextViewMessagePausedDeviceName.setVisibility(isRunning ? View.GONE : View.VISIBLE);
        try {
            this.mTextViewMessagePausedDeviceName.setText("Your device name is: '" + this.mDataAccessService.getDeviceName() + "'\nClick here to change.");
            this.mTextViewMessagePausedDeviceName.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                }
            });
            this.mTextViewMessageRunningDeviceName.setText(Html.fromHtml("Browse to: <a href=\"http://mybigbro.tv/?devicename=" + this.mDataAccessService.getDeviceName() + "\">mybigbro.tv/?devicename=" + this.mDataAccessService.getDeviceName() + "</a>\nto see your captured images!"));
            this.mTextViewMessageRunningDeviceName.setLinksClickable(true);
            this.mTextViewMessageRunningDeviceName.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setMessage("GPS must be enabled to use My Big Bro. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Go to settings page to enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
