package com.qteam.saigonjams.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.qteam.saigonjams.R;
import com.qteam.saigonjams.activity.MainActivity;
import com.qteam.saigonjams.adapter.NotificationRecyclerViewAdapter;
import com.qteam.saigonjams.model.Notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsFragment extends Fragment implements View.OnClickListener {

    private static final String DATABASE_PATH = "notifications";
    private static final String LOADING_MESSAGE = "Đang tải...";

    private RecyclerView recyclerView;
    private NotificationRecyclerViewAdapter recyclerViewAdapter;
    private List<Notification> notificationList;
    private ProgressDialog progressDialog;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        if (MainActivity.mainNav.getVisibility() != View.VISIBLE)
            MainActivity.mainNav.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.rcv_notifications_list);
        recyclerView.setHasFixedSize(true);

        ImageButton btnAdd = view.findViewById(R.id.btn_add_notification);
        btnAdd.setOnClickListener(this);

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
        progressDialog.show();

        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fbDatabase.getReference(DATABASE_PATH);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Notification notification = postSnapshot.getValue(Notification.class);
                    notificationList.add(notification);
                }

                Collections.reverse(notificationList);

                recyclerViewAdapter = new NotificationRecyclerViewAdapter(notificationList);

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
        AddNotificationsFragment addNotificationsFragment = new AddNotificationsFragment();
        setFragment(addNotificationsFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void filter(String searchText) {
        List<Notification> filteredList = new ArrayList<>();

        for (Notification item : notificationList) {
            if (item.getPosition().toLowerCase().contains(searchText.toLowerCase())
                    || item.getStatus().toLowerCase().contains(searchText.toLowerCase()))
                filteredList.add(item);
        }

        recyclerViewAdapter.filterList(filteredList);
    }

}
