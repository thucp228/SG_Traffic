package com.qteam.saigonjams.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qteam.saigonjams.activity.MainActivity;
import com.qteam.saigonjams.model.Sharing;
import com.qteam.saigonjams.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddSharingFragment extends Fragment {

    private static final String DATABASE_PATH = "sharing";
    private static final String TIME_FORMAT = "dd-MM-YYYY h:mm a";
    private static final String TRANSPORT_1 = "Xe máy";
    private static final String TRANSPORT_2 = "Xe ô-tô";
    private static final String SUCCESS_MESSAGE = "Đã chia sẻ!";
    private static final String INFO_REQUEST_MESSAGE = "Vui lòng nhập đầy đủ thông tin!";

    private EditText name, phone, startPos, endPos;
    private Spinner vehicleType;

    private DatabaseReference dbRef;

    public AddSharingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sharing_add, container, false);

        MainActivity.mainNav.setVisibility(View.GONE);

        name = view.findViewById(R.id.et_name);
        phone = view.findViewById(R.id.et_phone_number);
        startPos = view.findViewById(R.id.et_start_position);
        endPos = view.findViewById(R.id.et_end_position);
        vehicleType = view.findViewById(R.id.spn_transport);

        Button buttonPost = view.findViewById(R.id.btn_post_sharing);
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSharing();
            }
        });

        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        dbRef = fbDB.getReference(DATABASE_PATH);

        String[] type = {TRANSPORT_1, TRANSPORT_2};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        vehicleType.setAdapter(adapter);

        return view;
    }

    private void postSharing() {
        if ((name.getText().toString().trim().equals(""))
                || (phone.getText().toString().trim().equals(""))
                || (startPos.getText().toString().trim().equals(""))
                || (endPos.getText().toString().equals("")))
            Toast.makeText(getContext(), INFO_REQUEST_MESSAGE, Toast.LENGTH_SHORT).show();
        else {
            String userName = name.getText().toString().trim();
            String phoneNumber = phone.getText().toString().trim();
            String startPosition = startPos.getText().toString().trim();
            String endPosition = endPos.getText().toString().trim();
            String vehicle = vehicleType.getSelectedItem().toString();
            String date = getCurrentTime();

            Sharing sharing = new Sharing(userName, phoneNumber, startPosition, endPosition, vehicle, date);

            String postID = dbRef.push().getKey();
            dbRef.child(postID).setValue(sharing);

            Toast.makeText(getContext(), SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();

            SharingFragment sharingFragment = new SharingFragment();
            setFragment(sharingFragment);
        }
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

}
