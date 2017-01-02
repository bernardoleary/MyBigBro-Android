package com.infostructure.mybigbro.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.services.DataAccessService;
import com.infostructure.mybigbro.ui.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements View.OnClickListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mSectionNumber;
    private OnFragmentInteractionListener mListener;

    /* Services */
    private DataAccessService mDataAccessService;

    /* UI controls */
    private Button mButtonRefreshMap;
    private WebView mWebView;

    public static MapFragment newInstance(int sectionNumber) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MapFragment() {

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

		/* Create the services that we need */
        this.mDataAccessService = DataAccessService.getInstance();
        this.mDataAccessService.setApplicationContext(this.getActivity().getApplicationContext());

        /* Create the view */
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        this.mWebView = (WebView)rootView.findViewById(R.id.webView);
        this.mButtonRefreshMap = (Button)rootView.findViewById(R.id.buttonRefreshMap);

        /* Create the web view */
        this.mWebView.setInitialScale(1);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setLoadWithOverviewMode(true);
        this.mWebView.getSettings().setUseWideViewPort(true);
        this.mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        this.mWebView.setScrollbarFadingEnabled(true);

        /* Get the map URL */
        String mapUrl = "http://www.mybigbro.tv";
        try {
            mapUrl += "/?devicename=" + mDataAccessService.getDeviceName() + "&cameraCount=5&markerCount=5";
        } catch (Exception e) {
            Log.d("Error: ", e.toString());
        }

        /* Load the URL */
        this.mWebView.loadUrl(mapUrl);
        this.mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        /* Save button event */
        mButtonRefreshMap.setOnClickListener(this);

        /* Show the menu */
        setHasOptionsMenu(true);

		/* Return the view */
        return rootView;
    }

    @Override
    public void onClick(View view) {
        this.mWebView.reload();
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
