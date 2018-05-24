package com.qteam.saigonjams;

import android.os.Bundle;
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


public class CarSubmitFragment extends Fragment implements View.OnClickListener {

    private FirebaseDatabase fbDB;
    private DatabaseReference dbRef;
    private EditText name, phone, startPos, endPos;
    private Spinner vehicleType;
    private Button buttonOK;
    private CarPost carPost;

    public CarSubmitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_submit, container, false);

        name = view.findViewById(R.id.edtName);
        phone = view.findViewById(R.id.edtPhone);
        startPos = view.findViewById(R.id.edtStartPos);
        endPos = view.findViewById(R.id.edtEndPos);
        vehicleType = view.findViewById(R.id.spnType);
        buttonOK = view.findViewById(R.id.btnSubmit2);

        buttonOK.setOnClickListener(this);

        fbDB = FirebaseDatabase.getInstance();
        dbRef = fbDB.getReference("CarPost");

        String[] type = {"Xe máy", "Xe ô-tô"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        vehicleType.setAdapter(adapter);

        return view;
    }

    public void getValue() {
        carPost = new CarPost();
        carPost.setUserName(name.getText().toString());
        carPost.setPhoneNumber(phone.getText().toString());
        carPost.setStartPosition(startPos.getText().toString());
        carPost.setEndPosition(endPos.getText().toString());
        carPost.setVehicleType(vehicleType.getSelectedItem().toString());
    }

    @Override
    public void onClick(View view) {
        getValue();
        String postID = dbRef.push().getKey();
        dbRef.child(postID).setValue(carPost);
        Toast.makeText(getContext(), "Đã đăng thông báo!", Toast.LENGTH_SHORT).show();
        CarFragment carFragment = new CarFragment();
        setFragment(carFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

}
