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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.services.DataAccessService;
import com.infostructure.mybigbro.services.GeoMarkerService;
import com.infostructure.mybigbro.ui.OnFragmentInteractionListener;

public class MainFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mSectionNumber;
    private OnFragmentInteractionListener mListener;
    private Switch mSwitchGeoMarkerServiceIsRunning;

    // TODO: Rename and change types of parameters
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
        this.mSwitchGeoMarkerServiceIsRunning = (Switch)rootView.findViewById(R.id.switchGeoMarkerServiceIsRunning);
        this.mSwitchGeoMarkerServiceIsRunning.setChecked(isGeoMarkerServiceRunning());
        this.mSwitchGeoMarkerServiceIsRunning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getActivity().startService(new Intent(getActivity(), GeoMarkerService.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), GeoMarkerService.class));
                }
            }
        });
        /* GPS Enabled check */
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            this.mSwitchGeoMarkerServiceIsRunning.setEnabled(true);
            //Toast.makeText(getActivity().getApplicationContext(), "GPS is Enabled on your device", Toast.LENGTH_LONG).show();
        } else{
            this.mSwitchGeoMarkerServiceIsRunning.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showGPSDisabledAlertToUser();
                    // start time consuming background process here
                }
            }, 2000); // starting it in 2 seconds...
        }
        return rootView;
    }

    private void showGPSDisabledAlertToUser(){
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
