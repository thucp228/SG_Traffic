package com.qteam.saigonjams.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qteam.saigonjams.model.CarPost;
import com.qteam.saigonjams.R;
import com.qteam.saigonjams.adapter.CarRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarFragment extends Fragment implements View.OnClickListener {

    private static final String DATABASE_PATH = "Car_Posts";

    private FloatingActionButton fabAdd;
    private FirebaseDatabase fbDatabase;
    private DatabaseReference dbRef;
    private RecyclerView recyclerView;
    private List<CarPost> postList;

    public CarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container, false);
        fabAdd = view.findViewById(R.id.fab_add_2);
        fabAdd.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.rcvCarList);

        fbDatabase = FirebaseDatabase.getInstance();
        dbRef = fbDatabase.getReference(DATABASE_PATH);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CarPost carPost = postSnapshot.getValue(CarPost.class);
                    postList.add(carPost);
                }
                Collections.reverse(postList);
                CarRecyclerViewAdapter adapter = new CarRecyclerViewAdapter(postList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ReadData", "Failed to read data");
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        CarSubmitFragment carSubmitFragment = new CarSubmitFragment();
        setFragment(carSubmitFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
