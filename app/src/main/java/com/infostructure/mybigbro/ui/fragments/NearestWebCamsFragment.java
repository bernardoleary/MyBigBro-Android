package com.infostructure.mybigbro.ui.fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.model.dto.WebCamExtendedInfoDto;
import com.infostructure.mybigbro.services.DataAccessService;
import com.infostructure.mybigbro.services.GeoMarkerService;
import com.infostructure.mybigbro.ui.OnFragmentInteractionListener;
import com.infostructure.mybigbro.ui.fragments.dummy.DummyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link com.infostructure.mybigbro.ui.OnFragmentInteractionListener}
 * interface.
 */
public class NearestWebCamsFragment extends Fragment implements View.OnClickListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mSectionNumber;
    private OnFragmentInteractionListener mListener;

    // Services
    private DataAccessService dataAccessService;

    /* UI controls */
    private Button mButtonRefreshList;
    private EditText mEditTextGetTop;
    private ListView mListViewClosestCameras;
    private ArrayAdapter<WebCamExtendedInfoDto> mWebCamArrayAdapter;

    /* Instance variables */
    private List<WebCamExtendedInfoDto> mWebCamsList = new ArrayList<WebCamExtendedInfoDto>();

    public static NearestWebCamsFragment newInstance(int sectionNumber) {
        NearestWebCamsFragment fragment = new NearestWebCamsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NearestWebCamsFragment() {

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
        this.dataAccessService = DataAccessService.getInstance();
        this.dataAccessService.setApplicationContext(this.getActivity().getApplicationContext());

		/* Create the view */
        View rootView = inflater.inflate(R.layout.fragment_closest, container, false);
        this.mButtonRefreshList = (Button)rootView.findViewById(R.id.buttonRefreshList);
        this.mEditTextGetTop = (EditText)rootView.findViewById(R.id.editTextGetTop);
        this.mListViewClosestCameras = (ListView)rootView.findViewById(R.id.listViewClosestCameras);

        /* Save button event */
        this.mButtonRefreshList.setOnClickListener(this);

        /* Show the menu */
        setHasOptionsMenu(true);

        /* The data to show */
        initList();

        /* WebCam adapter */
        this.mWebCamArrayAdapter = new WebCamArrayAdapter(
                this.getActivity(),
                R.layout.fragment_item_closest,
                this.mWebCamsList);
        this.mListViewClosestCameras.setAdapter(this.mWebCamArrayAdapter);

        /* React to user clicks on item */
        this.mListViewClosestCameras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                // When clicked, transfer to the simple list items
                WebCamExtendedInfoDto webCamExtendedInfoDto = (WebCamExtendedInfoDto)parentAdapter.getItemAtPosition(position);
                Toast.makeText(getActivity().getApplicationContext(), "Distance [" + webCamExtendedInfoDto.Distance + "] - Camera [" + webCamExtendedInfoDto.Name + "]", Toast.LENGTH_SHORT).show();
            }
        });

		/* Return the view */
        return rootView;
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

    private void initList() {

        /* Get the list of cameras */
        int numWebCamDtos = 0;
        try {
            numWebCamDtos = Integer.valueOf(this.mEditTextGetTop.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("Error: ", e.toString());
            this.mEditTextGetTop.setText("0");
            return;
        }

        /* Query the service */
        WebCamExtendedInfoDto[] webCamExtendedInfoDtos;
        try {
            webCamExtendedInfoDtos = this.dataAccessService.getNearestManyWebCams(numWebCamDtos);
        } catch (Exception e) {
            Log.d("Error: ", e.toString());
            return;
        }

        /* We populate the cameras, if there are any */
        if (webCamExtendedInfoDtos != null) {
            this.mWebCamsList.clear();
            for (int i = 0; i < numWebCamDtos; i++)
                this.mWebCamsList.add(webCamExtendedInfoDtos[i]);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No cameras found nearby.\nAt least one geo-marker is required to determine what cameras are nearby.\nHave you switched on geo-marker collection yet?", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        initList();
        this.mWebCamArrayAdapter.notifyDataSetChanged();
    }

    /* For the ListView */
    private class WebCamArrayAdapter extends ArrayAdapter<WebCamExtendedInfoDto> {

        HashMap<WebCamExtendedInfoDto, Integer> mIdMap = new HashMap<WebCamExtendedInfoDto, Integer>();
        int textViewResourceId;

        public WebCamArrayAdapter(Context context, int textViewResourceId, List<WebCamExtendedInfoDto> objects) {
            super(context, textViewResourceId, objects);
            this.textViewResourceId = textViewResourceId;
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout webCamExtendedInfoDtoView;

            //Get the current alert object
            WebCamExtendedInfoDto webCamExtendedInfoDto = getItem(position);

            //Inflate the view
            if(convertView == null)
            {
                webCamExtendedInfoDtoView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi;
                vi = (LayoutInflater)getContext().getSystemService(inflater);
                vi.inflate(this.textViewResourceId, webCamExtendedInfoDtoView, true);
            }
            else
            {
                webCamExtendedInfoDtoView = (LinearLayout)convertView;
            }

            //Get the text boxes from the listitem.xml file
            TextView textViewName = (TextView)webCamExtendedInfoDtoView.findViewById(R.id.textViewName);
            TextView textViewDistance = (TextView)webCamExtendedInfoDtoView.findViewById(R.id.textViewDistance);

            //Assign the appropriate data from our alert object above
            textViewName.setText(webCamExtendedInfoDto.Name);
            textViewDistance.setText(Double.toString(webCamExtendedInfoDto.Distance));

            return webCamExtendedInfoDtoView;
        }

        @Override
        public long getItemId(int position) {
            WebCamExtendedInfoDto item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
