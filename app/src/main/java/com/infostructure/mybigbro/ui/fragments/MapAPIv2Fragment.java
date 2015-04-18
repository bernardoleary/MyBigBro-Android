package com.infostructure.mybigbro.ui.fragments;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CameraPositionCreator;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.model.dto.GeoMarkerDto;
import com.infostructure.mybigbro.model.dto.WebCamExtendedInfoDto;
import com.infostructure.mybigbro.services.DataAccessService;
import com.infostructure.mybigbro.ui.OnFragmentInteractionListener;
import com.infostructure.mybigbro.ui.async.WebCamExtendedInfoDownloadAsyncTask;
import com.infostructure.mybigbro.utils.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapAPIv2Fragment extends SupportMapFragment implements SeekBar.OnSeekBarChangeListener, WebCamExtendedInfoFragment, GoogleMap.OnMarkerClickListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mSectionNumber;
    private OnFragmentInteractionListener mListener;

    /* UI controls */
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private ListView mListViewClosestCameras;
    private SeekBar mSeekBarClosestCameras;
    private int mSeekBarProgress = 10; // Set SeekBar progress as 10 by default, same as UI

    // Services
    private DataAccessService mDataAccessService;
    private WebCamExtendedInfoDto[] mWebCamExtendedInfoDtos;

    public static MapAPIv2Fragment newInstance(int sectionNumber) {
        MapAPIv2Fragment fragment = new MapAPIv2Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MapAPIv2Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /* For SupportMapFragment it's necessary to call the super class */
        super.onCreateView(inflater, container, savedInstanceState);

		/* Create the services that we need */
        this.mDataAccessService = DataAccessService.getInstance();
        this.mDataAccessService.setApplicationContext(this.getActivity().getApplicationContext());

        // inflate and return the layout
        View rootView = inflater.inflate(R.layout.fragment_map_apiv2, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        /* Show the menu */
        this.setHasOptionsMenu(true);

        /* The data to show and the seekbar event */
        this.mSeekBarClosestCameras = (SeekBar)rootView.findViewById(R.id.seekBarClosestCameras);
        this.mSeekBarClosestCameras.setOnSeekBarChangeListener(this);

        /* Initialise the map */
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
            this.mGoogleMap = mMapView.getMap();
            this.mGoogleMap.setOnMarkerClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Populate the list */
        new WebCamExtendedInfoDownloadAsyncTask(this, this.mSeekBarProgress).execute();

        /* Return the view */
        return rootView;
    }

    public void initUserInterface(WebCamExtendedInfoDto[] webCamExtendedInfoDtos) {

        /* We populate the cameras, if there are any */
        if (webCamExtendedInfoDtos != null) {

            this.mWebCamExtendedInfoDtos = webCamExtendedInfoDtos;
            this.mGoogleMap.clear();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            /* Add markers for cameras */
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
            for (int i = 0; i < this.mSeekBarClosestCameras.getProgress(); i++) {
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(webCamExtendedInfoDtos[i].YCoord, webCamExtendedInfoDtos[i].XCoord))
                        .title(webCamExtendedInfoDtos[i].Name)
                        .icon(icon);
                builder.include(marker.getPosition());
                this.mGoogleMap.addMarker(marker);
            }

            /* Add marker for current location */
            GeoMarkerDto currentLocation = Globals.getGetCurrentLocation();
            LatLng currentLocationLatLng = new LatLng(currentLocation.yCoord, currentLocation.xCoord);
            MarkerOptions marker = new MarkerOptions()
                    .position(currentLocationLatLng)
                    .title("My Location");
            builder.include(marker.getPosition());
            this.mGoogleMap.addMarker(marker);

            /* Build the marker map and set position */
            LatLngBounds bounds = builder.build();
            int padding = 50; // offset from edges of the map in pixels
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            this.mGoogleMap.animateCamera(cameraUpdate);

            /* Camera position with tilt (unable to set bounds so not using currently)
            CameraPosition cameraPosition =
                    new CameraPosition.Builder().target(currentLocationLatLng)
                            .zoom(17)
                            .bearing(320)
                            .tilt(30)
                            .build();
            this.mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            */

            /* Announce the result */
            Toast.makeText(getActivity().getApplicationContext(), "Showing closest " + mSeekBarProgress + " cameras.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No cameras found nearby.\nAt least one geo-marker is required to determine what cameras are nearby.\nHave you switched on geo-marker collection yet?", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.mSeekBarProgress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        new WebCamExtendedInfoDownloadAsyncTask(this, this.mSeekBarProgress).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        /* Match the webcam info to the marker */
        WebCamExtendedInfoDto webCamExtendedInfoDto = null;
        for (WebCamExtendedInfoDto webCam : this.mWebCamExtendedInfoDtos) {
            if (webCam.Name.equals(marker.getTitle())) {
                webCamExtendedInfoDto = webCam;
                break;
            }
        }
        /* Make sure that we have a match and continue */
        if (webCamExtendedInfoDto != null) {
            LatestWebCamImageDialogFragment newFragment = new LatestWebCamImageDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", webCamExtendedInfoDto.Url);
            bundle.putString("name", webCamExtendedInfoDto.Name);
            bundle.putDouble("distance", webCamExtendedInfoDto.Distance);
            bundle.putDouble("xcoord", webCamExtendedInfoDto.XCoord);
            bundle.putDouble("ycoord", webCamExtendedInfoDto.YCoord);
            newFragment.setArguments(bundle);
            newFragment.show(getActivity().getSupportFragmentManager(), webCamExtendedInfoDto.Url);
            return true;
        }
        return false;
    }
}
