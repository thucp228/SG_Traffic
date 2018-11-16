package com.qteam.saigonjams.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qteam.saigonjams.R;
import com.qteam.saigonjams.activity.MainActivity;
import com.qteam.saigonjams.model.Notification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddNotificationsFragment extends Fragment {

    private static final String DATABASE_PATH = "notifications";
    private static final String STORAGE_PATH = "images/";
    private static final String STATUS_1 = "Thông thoáng";
    private static final String STATUS_2 = "Đông xe";
    private static final String STATUS_3 = "Ùn tắc";
    private static final String STATUS_4 = "Ngập nước";
    private static final String STATUS_5 = "Tai nạn";
    private static final String TIME_FORMAT = "dd-MM-YYYY h:mm a";
    private static final String LOADING_MESSAGE = "Đang tải ảnh lên...";
    private static final String FROM_GALLERY = "Chọn ảnh từ điện thoại";
    private static final String FROM_CAMERA = "Chụp ảnh từ camera";
    private static final String ADD_PHOTO_MESSAGE = "Thêm ảnh";
    private static final String ADD_PHOTO_SUCCESS_MESSAGE = "Đã chọn ảnh!";
    private static final String LOCATION_REQUEST_MESSAGE = "Vui lòng nhập vị trí!";
    private static final String POST_SUCCESS_MESSAGE = "Đã đăng thông báo!";
    private static final String MEDIA_TYPE = "image/*";
    private static final String ERROR_MESSAGE = "Lỗi: ";
    private static final String PHOTO_REQUEST_MESSAGE = "Vui lòng chọn ảnh!";

    private static final int PERMISSIONS_REQUEST = 3;
    private static final int GALLERY = 1, CAMERA = 2;

    private EditText position;
    private Spinner status;
    private ImageView imageView;
    private Uri fileURI;
    private ProgressDialog progressDialog;

    private DatabaseReference dbRef;
    private StorageReference storageRef;

    public AddNotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications_add, container, false);

        MainActivity.mainNav.setVisibility(View.GONE);

        position = view.findViewById(R.id.et_position);
        status = view.findViewById(R.id.spn_status);
        imageView = view.findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
        progressDialog = new ProgressDialog(getContext());

        Button buttonPost = view.findViewById(R.id.btn_post_notification);
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNotification();
            }
        });

        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        dbRef = fbDB.getReference(DATABASE_PATH);
        FirebaseStorage fbStorage = FirebaseStorage.getInstance();
        storageRef = fbStorage.getReference();

        String[] status = {STATUS_1, STATUS_2, STATUS_3, STATUS_4, STATUS_5};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, status);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        this.status.setAdapter(adapter);

        return view;
    }

    private void checkPermissions() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(getContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED)
            showAddImageDialog();
        else
            requestPermissions(permissions, PERMISSIONS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                showAddImageDialog();
        }
    }

    private void showAddImageDialog() {
        String[] options = {FROM_GALLERY, FROM_CAMERA};
        AlertDialog.Builder photoDialog = new AlertDialog.Builder(getActivity());
        photoDialog.setTitle(ADD_PHOTO_MESSAGE);
        photoDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        selectImageFromGallery();
                        break;
                    case 1:
                        takeImageFromCamera();
                        break;
                }
            }
        });
        photoDialog.show();
    }

    private void selectImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MEDIA_TYPE);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takeImageFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED)
            return;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    fileURI = data.getData();
                    imageView.setImageURI(fileURI);
                    Toast.makeText(getContext(), ADD_PHOTO_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap thumbnail = (Bitmap) extras.get("data");
                imageView.setImageBitmap(thumbnail);
                Toast.makeText(getContext(), ADD_PHOTO_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void postNotification() {
        if (fileURI != null) {
            if (position.getText().toString().equals("")) {
                Toast.makeText(getContext(), LOCATION_REQUEST_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            else {
                progressDialog.setMessage(LOADING_MESSAGE);
                progressDialog.show();

                final StorageReference storageRef2 = storageRef.child(STORAGE_PATH + System.currentTimeMillis() + "." + getFileExtension(fileURI));
                storageRef2.putFile(fileURI)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String pos = position.getText().toString().trim();
                                        String stt = status.getSelectedItem().toString();
                                        String date = getCurrentTime();

                                        Notification notification = new Notification(pos, stt, date, uri.toString());

                                        String postID = dbRef.push().getKey();
                                        dbRef.child(postID).setValue(notification);

                                        progressDialog.dismiss();

                                        Toast.makeText(getContext(), POST_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();

                                        NotificationsFragment notificationsFragment = new NotificationsFragment();
                                        setFragment(notificationsFragment);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), ERROR_MESSAGE + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.setMessage(LOADING_MESSAGE);
                            }
                        });
            }
        }
        else
            Toast.makeText(getContext(), PHOTO_REQUEST_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }
}
