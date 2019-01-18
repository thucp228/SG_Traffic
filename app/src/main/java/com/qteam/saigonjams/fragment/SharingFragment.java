package com.qteam.saigonjams.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

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
    public static final int PERMISSIONS_REQUEST = 1;
    private static final String DATABASE_PATH = "sharing";
    private static final String LOADING_MESSAGE = "Đang tải...";
    private static final String INFO = "Nhấn giữ để gọi!";

    private RecyclerView recyclerView;
    private SharingRecyclerViewAdapter recyclerViewAdapter;
    private List<Sharing> sharingList;
    private ProgressDialog progressDialog;

    public SharingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sharing, container, false);

        if (MainActivity.mainNav.getVisibility() != View.VISIBLE)
            MainActivity.mainNav.setVisibility(View.VISIBLE);

        checkPermissions();

        ImageButton btnAdd = view.findViewById(R.id.btn_add_sharing);
        btnAdd.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.rcv_sharing_list);
        recyclerView.setHasFixedSize(true);

        EditText searchBox = view.findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(LOADING_MESSAGE);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fbDatabase.getReference(DATABASE_PATH);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sharingList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Sharing sharing = postSnapshot.getValue(Sharing.class);
                    sharingList.add(sharing);
                }

                Collections.reverse(sharingList);

                recyclerViewAdapter = new SharingRecyclerViewAdapter(sharingList);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(recyclerViewAdapter);

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
        setFragment(addSharingFragment);
    }

    private boolean checkPermissions() {
        String[] permissions = {Manifest.permission.CALL_PHONE};
        if (ContextCompat.checkSelfPermission(getContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            requestPermissions(permissions, PERMISSIONS_REQUEST);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void filter(String searchText) {
        List<Sharing> filteredList = new ArrayList<>();

        for (Sharing item : sharingList) {
            if (item.getStartPosition().toLowerCase().contains(searchText.toLowerCase())
                    || item.getEndPosition().toLowerCase().contains(searchText.toLowerCase())
                    || item.getVehicleType().toLowerCase().contains(searchText.toLowerCase()))
                filteredList.add(item);
        }

        recyclerViewAdapter.filterList(filteredList);
    }

}
