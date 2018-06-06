package com.qteam.saigonjams.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qteam.saigonjams.R;

public class AlertFragment extends Fragment implements View.OnClickListener {

    private static final String DATABASE_PATH = "Alert_Posts";

    private FloatingActionButton fabAdd;

    public AlertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        fabAdd = view.findViewById(R.id.fab_add_1);
        fabAdd.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        AlertSubmitFragment alertSubmitFragment = new AlertSubmitFragment();
        setFragment(alertSubmitFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
