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

    // Controls
    private Button buttonRefreshList;
    private EditText editTextGetTop;
    private ListView listViewClosestCameras;
    private SimpleAdapter simpleAdpter;

    // Instance variables
    private List<Map<String, String>> webCamsList = new ArrayList<Map<String,String>>();

    // TODO: Rename and change types of parameters
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

		/* Create and return the view */
        View rootView = inflater.inflate(R.layout.fragment_closest, container, false);
        this.buttonRefreshList = (Button)rootView.findViewById(R.id.buttonRefreshList);
        this.editTextGetTop = (EditText)rootView.findViewById(R.id.editTextGetTop);
        this.listViewClosestCameras = (ListView)rootView.findViewById(R.id.listViewClosestCameras);

        // save button event
        buttonRefreshList.setOnClickListener(this);

        // Show the menu
        setHasOptionsMenu(true);

        // The data to show
        initList();

        // We get the ListView component from the layout

        // This is a simple adapter that accepts as parameter
        // Context
        // Data list
        // The row layout that is used during the row creation
        // The keys used to retrieve the data
        // The View id used to show the data. The key number and the view id must match
        this.simpleAdpter = new SimpleAdapter(
                this.getActivity(),
                webCamsList,
                android.R.layout.simple_list_item_1, new String[] {"camera"},
                new int[] {android.R.id.text1});
        listViewClosestCameras.setAdapter(this.simpleAdpter);

        // React to user clicks on item
        listViewClosestCameras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                // We know the View is a TextView so we can cast it
                TextView clickedView = (TextView) view;
                Toast.makeText(getActivity().getApplicationContext(), "Item with id [" + id + "] - Position [" + position + "] - Planet [" + clickedView.getText() + "]", Toast.LENGTH_SHORT).show();
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

    private HashMap<String, String> createWebCamHashMap(WebCamExtendedInfoDto webCam) {
        HashMap<String, String> webCamDetails = new HashMap<String, String>();
        webCamDetails.put("camera", webCam.Name);
        webCamDetails.put("distance", Double.toString(webCam.Distance));
        return webCamDetails;
    }

    private void initList() {
        // Get the list of cameras
        int numWebCamDtos = 0;
        try {
            numWebCamDtos = Integer.valueOf(editTextGetTop.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("Error: ", e.toString());
            editTextGetTop.setText("0");
            return;
        }
        WebCamExtendedInfoDto[] webCamExtendedInfoDtos;
        try {
            webCamExtendedInfoDtos = this.dataAccessService.getNearestManyWebCams(numWebCamDtos);
        } catch (Exception e) {
            Log.d("Error: ", e.toString());
            return;
        }
        // We populate the cameras
        webCamsList.clear();
        for(int i=0; i<numWebCamDtos; i++)
            webCamsList.add(createWebCamHashMap(webCamExtendedInfoDtos[i]));
    }

    @Override
    public void onClick(View view) {
        initList();
        this.simpleAdpter.notifyDataSetChanged();
    }

    // For the ListView
    private class WebCamArrayAdapter extends ArrayAdapter<WebCamExtendedInfoDto> {

        HashMap<WebCamExtendedInfoDto, Integer> mIdMap = new HashMap<WebCamExtendedInfoDto, Integer>();

        public WebCamArrayAdapter(Context context, int textViewResourceId, List<WebCamExtendedInfoDto> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
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
