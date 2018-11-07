package com.qteam.saigonjams.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qteam.saigonjams.R;
import com.qteam.saigonjams.activity.MainActivity;

public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (MainActivity.mainNav.getVisibility() != View.VISIBLE)
            MainActivity.mainNav.setVisibility(View.VISIBLE);

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
