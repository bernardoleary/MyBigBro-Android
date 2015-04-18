package com.infostructure.mybigbro.ui.async;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.infostructure.mybigbro.model.dto.WebCamExtendedInfoDto;
import com.infostructure.mybigbro.services.DataAccessService;
import com.infostructure.mybigbro.ui.fragments.WebCamExtendedInfoFragment;
import com.infostructure.mybigbro.utils.Globals;

/**
 * Created by Admin on 18/04/2015.
 */
public class WebCamExtendedInfoDownloadAsyncTask extends AsyncTask<Void, Void, Void> {

    private ProgressDialog mDialog = null;
    private WebCamExtendedInfoDto[] mWebCamExtendedInfoDtos = null;
    private WebCamExtendedInfoFragment mWebCamExtendedInfoFragment = null;
    private DataAccessService mDataAccessService = null;
    private int mNumberOfWebCamsToDownload = 10;

    public WebCamExtendedInfoDownloadAsyncTask(WebCamExtendedInfoFragment webCamExtendedInfoFragment, int numberOfWebCamsToDownload) {
        this.mWebCamExtendedInfoFragment = webCamExtendedInfoFragment;
        this.mDialog = new ProgressDialog(webCamExtendedInfoFragment.getActivity());
        this.mNumberOfWebCamsToDownload = numberOfWebCamsToDownload;

        /* Create the services that we need */
        this.mDataAccessService = DataAccessService.getInstance();
        this.mDataAccessService.setApplicationContext(webCamExtendedInfoFragment.getActivity().getApplicationContext());
    }

    @Override
    protected void onPreExecute() {
        this.mDialog.setMessage("Getting cameras...");
        this.mDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        /* Query the service */
        try {
            this.mWebCamExtendedInfoDtos = this.mDataAccessService.getNearestManyWebCams(this.mNumberOfWebCamsToDownload);
            Globals.setGetCurrentLocation(this.mDataAccessService.getLastestGeoMarker());
        } catch (Exception e) {
            Log.d("Error: ", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        this.mWebCamExtendedInfoFragment.initUserInterface(this.mWebCamExtendedInfoDtos);
        this.mDialog.dismiss();
    }
}
