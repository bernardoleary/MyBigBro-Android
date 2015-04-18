package com.infostructure.mybigbro.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.infostructure.mybigbro.model.dto.WebCamExtendedInfoDto;

/**
 * Created by Admin on 18/04/2015.
 */
public interface WebCamExtendedInfoFragment {
    public void initUserInterface(WebCamExtendedInfoDto[] webCamExtendedInfoDtos);
    public FragmentActivity getActivity();
}
