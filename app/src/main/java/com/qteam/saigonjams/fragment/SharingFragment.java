package com.qteam.saigonjams.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.qteam.saigonjams.activity.MainActivity;
import com.qteam.saigonjams.model.Sharing;
import com.qteam.saigonjams.R;
import com.qteam.saigonjams.adapter.SharingRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharingFragment extends Fragment implements View.OnClickListener {

    private static final String DATABASE_PATH = "Car_Posts";

    private FloatingActionButton fabAdd;
    private FirebaseDatabase fbDatabase;
    private DatabaseReference dbRef;
    private RecyclerView recyclerView;
    private List<Sharing> postList;
    private ProgressDialog progressDialog;

    public SharingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sharing, container, false);

        if (MainActivity.mainNav.getVisibility() != View.VISIBLE)
            MainActivity.mainNav.setVisibility(View.VISIBLE);

        fabAdd = view.findViewById(R.id.fab_add_sharing);
        fabAdd.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.rcv_sharing_list);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        fbDatabase = FirebaseDatabase.getInstance();
        dbRef = fbDatabase.getReference(DATABASE_PATH);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Sharing sharing = postSnapshot.getValue(Sharing.class);
                    postList.add(sharing);
                }
                Collections.reverse(postList);
                SharingRecyclerViewAdapter adapter = new SharingRecyclerViewAdapter(postList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ReadData", "Failed to read data");
                progressDialog.dismiss();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        AddSharingFragment addSharingFragment = new AddSharingFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.main_container, addSharingFragment)
                .addToBackStack(null).commit();
    }

}
