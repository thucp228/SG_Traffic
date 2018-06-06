package com.qteam.saigonjams.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.qteam.saigonjams.model.AlertPost;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlertSubmitFragment extends Fragment {

    private static final String IMAGE_DIRECTORY = "/TTKXHCM";
    private static final String DATABASE_PATH = "Alert_Posts";
    private static final String STORAGE_PATH = "All_Images/";
    private static final int PERMISSIONS_REQUEST = 3;

    private static final int GALLERY = 1, CAMERA = 2;
    private EditText position;
    private Spinner status;
    private Button buttonAddImg, buttonPost;
    private ImageView imageView;
    private Uri fileURI;
    private AlertPost alertPost;
    private ProgressDialog progressDialog;

    private FirebaseDatabase fbDB;
    private FirebaseStorage fbStorage;
    private DatabaseReference dbRef;
    private StorageReference storageRef;

    public AlertSubmitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert_submit, container, false);

        position = view.findViewById(R.id.edtPosition);
        status = view.findViewById(R.id.spnStatus);
        imageView = view.findViewById(R.id.imgView);
        progressDialog = new ProgressDialog(getContext());

        buttonAddImg = view.findViewById(R.id.btnAddImg);
        buttonAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        buttonPost = view.findViewById(R.id.btnSubmit1);
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postAlert();
            }
        });

        fbDB = FirebaseDatabase.getInstance();
        dbRef = fbDB.getReference(DATABASE_PATH);
        fbStorage = FirebaseStorage.getInstance();
        storageRef = fbStorage.getReference();

        String[] status = {"Thông thoáng", "Đông xe", "Ùn tắc", "Ngập nước", "Tai nạn"};
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
            else
                return;
        }
    }

    private void showAddImageDialog() {
        String[] options = {"Chọn ảnh từ điện thoại", "Chụp ảnh từ camera"};
        AlertDialog.Builder photoDialog = new AlertDialog.Builder(getActivity());
        photoDialog.setTitle("Thêm ảnh");
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
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takeImageFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA);
    }

    private String saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bAOS);
        File photoDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        if (!photoDirectory.exists())
            photoDirectory.mkdirs();
        try {
            File file = new File(photoDirectory, timeStamp + ".jpg");
            file.createNewFile();
            FileOutputStream fOS = new FileOutputStream(file);
            fOS.write(bAOS.toByteArray());
            MediaScannerConnection.scanFile(getActivity(), new String[] {file.getPath()}, new String[] {"image/jpeg"}, null);
            fOS.close();

            return file.getAbsolutePath();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_CANCELED)
            return;
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    fileURI = data.getData();
                    imageView.setImageURI(fileURI);
                    Toast.makeText(getContext(), "Đã chọn ảnh!", Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap thumbnail = (Bitmap) extras.get("data");
                imageView.setImageBitmap(thumbnail);
                Toast.makeText(getContext(), "Đã chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getValue() {
        alertPost = new AlertPost();
        alertPost.setPosition(position.getText().toString().trim());
        alertPost.setStatus(status.getSelectedItem().toString());
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void postAlert() {
        getValue();
        if (fileURI != null) {
            if (alertPost.getPosition().equals("")) {
                Toast.makeText(getContext(), "Vui lòng nhập vị trí!", Toast.LENGTH_SHORT).show();
            }
            else {
                progressDialog.setTitle("Đang tải ảnh lên...");
                progressDialog.show();

                StorageReference storageRef2nd = storageRef.child(STORAGE_PATH + System.currentTimeMillis() + "." + getFileExtension(fileURI));
                storageRef2nd.putFile(fileURI)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();

                                String postID = dbRef.push().getKey();
                                dbRef.child(postID).setValue(alertPost);

                                Toast.makeText(getContext(), "Đã đăng cảnh báo!", Toast.LENGTH_SHORT).show();

                                AlertFragment alertFragment = new AlertFragment();
                                setFragment(alertFragment);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.setTitle("Đang tải ảnh lên...");
                            }
                        });
            }
        }
        else
            Toast.makeText(getContext(), "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

}
