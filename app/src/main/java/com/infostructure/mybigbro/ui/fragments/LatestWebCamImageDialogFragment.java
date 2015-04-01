package com.infostructure.mybigbro.ui.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infostructure.mybigbro.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestWebCamImageDialogFragment extends DialogFragment {

    private String mUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mUrl = getArguments().getString("url");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ImageView imageView = null;
        try {
            URL url = new URL(this.mUrl);
            imageView = new ImageView(getActivity());
            imageView.setImageBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
            imageView.setMinimumWidth(500);
            imageView.setMinimumHeight(500);
        } catch (MalformedURLException e) {
            Log.d("Error: ", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        builder.setMessage(this.mUrl)
            .setPositiveButton("fire", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // FIRE ZE MISSILES!
                }
            })
            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            })
            .setView(imageView);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
