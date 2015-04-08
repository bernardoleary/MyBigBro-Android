package com.infostructure.mybigbro.ui.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.model.GeoMarker;
import com.infostructure.mybigbro.model.dto.WebCamExtendedInfoDto;
import com.infostructure.mybigbro.services.DataAccessService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestWebCamImageDialogFragment extends DialogFragment {

    private String mUrl;
    private String mName;
    private double mDistance;
    private double mXCoord;
    private double mYCoord;
    ImageView imageView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mUrl = getArguments().getString("url");
        this.mName = getArguments().getString("name");
        this.mDistance = getArguments().getDouble("distance");
        this.mXCoord = getArguments().getDouble("xcoord");
        this.mYCoord = getArguments().getDouble("ycoord");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        try {
            URL url = new URL(mUrl);
            URLConnection urlConnection = url.openConnection();
            imageView = new ImageView(getActivity());
            imageView.setImageBitmap(BitmapFactory.decodeStream(urlConnection.getInputStream()));
            imageView.setMinimumWidth(500);
            imageView.setMinimumHeight(500);
        } catch (MalformedURLException e) {
            Log.d("Error: ", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        InputStream is = getResources().openRawResource(R.drawable.camera_loading_128);
        Bitmap originalBitmap = BitmapFactory.decodeStream(is);
        imageView = new ImageView(getActivity());
        imageView.setImageBitmap(originalBitmap);
        imageView.setMinimumWidth(500);
        imageView.setMinimumHeight(500);
        */
        builder.setMessage(this.mName + " (" + (double)Math.round(this.mDistance * 100)/100 + "km away from you)")
            .setPositiveButton("Capture", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    /***
                     * Capture a geo-marker right on-top of the camera - assures we get a hit.
                     */
                    DataAccessService dataAccessService = DataAccessService.getInstance();
                    GeoMarker geoMarker = new GeoMarker();
                    geoMarker.setMarkerDateTime(Calendar.getInstance().getTime());
                    geoMarker.setXCoord(mXCoord);
                    geoMarker.setYCoord(mYCoord);
                    String deviceName = "(check your settings)";
                    try {
                        dataAccessService.createGeoMarker(geoMarker);
                        deviceName = "'" + dataAccessService.getDeviceName() + "'";
                    } catch (Exception e) {
                        Log.d("Error: ", e.toString());
                    }
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            "Go to www.mybigbro.tv and search for your device " + deviceName + " to see the image you've captured!",
                            Toast.LENGTH_LONG).show();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            })
            .setView(imageView);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /*** work in progress ***
    private class ImageDownloader extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog = new ProgressDialog(getActivity());
        private Exception ex = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(mUrl);
                URLConnection urlConnection = url.openConnection();
                imageView.setImageBitmap(BitmapFactory.decodeStream(urlConnection.getInputStream()));
                imageView.setMinimumWidth(500);
                imageView.setMinimumHeight(500);
            } catch (MalformedURLException e) {
                Log.d("Error: ", e.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            notifyAll();
        }
    }
    ***/
}
