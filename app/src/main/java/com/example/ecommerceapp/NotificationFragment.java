package com.example.ecommerceapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;


public class NotificationFragment extends Fragment {

    private Button btnLogout;
    private CircleImageView imgUser;
    private TextView txtNameUser;
    private DatabaseReference mDBUser;
    private ProgressDialog progressDialog;

    final static int Gallery_Pick=1;
    private StorageReference UserProfileImage;
    private FirebaseAuth mAuth;
    private String CurrentUserId = "";
    private TextView txtAddress;
    private Location location;

    private ImageView imgAccepting;
    private ImageView imgGetting;
    private ImageView imgShipping;
    private ImageView imgRate;


    //TODO Lấy vị trí GPS người dùng
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        mAuth = FirebaseAuth.getInstance();
        btnLogout = view.findViewById(R.id.btnLogout);
        imgUser = view.findViewById(R.id.imgUser);

        txtNameUser = view.findViewById(R.id.txtNameUser);
        txtAddress = view.findViewById(R.id.txtAddress);

        imgAccepting = view.findViewById(R.id.imgWaitAccept);
        imgGetting = view.findViewById(R.id.imgWaitGetting);
        imgShipping = view.findViewById(R.id.imgShipping);
        imgRate = view.findViewById(R.id.imgRate);

      //  getAddress( getContext(), new GeocoderHandler());
        mDBUser = FirebaseDatabase.getInstance().getReference().child("Users");
        if (mAuth.getCurrentUser() == null) {
            CurrentUserId = "";
        } else {
            CurrentUserId = mAuth.getCurrentUser().getUid();
        }

        mDBUser= FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId);
        UserProfileImage= FirebaseStorage.getInstance().getReference().child("Profile Images");
        progressDialog=new ProgressDialog(getContext());
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Log out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                SendUserToLoginActivity();
            }
        });
        mDBUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("fullname")) {
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        txtNameUser.setText(fullname);
                    }
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();

                        Picasso.get().load(image).into(imgUser);
                    } else {
                        Toast.makeText(getContext(), "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imgAccepting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AcceptingProducts.class);
                startActivity(intent);
            }
        });

        imgGetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),GettingProducts.class);
                startActivity(intent);
            }
        });

        imgShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ShippingProducts.class);
                startActivity(intent);
            }
        });
        imgRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getActivity().getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageInProfile();

            }
        });
        return view;
    }
    private void getImageInProfile() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_Pick);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getActivity());
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                progressDialog.setTitle("Getting Image");
                progressDialog.setMessage("Please wait, while we are getting image in profile...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImage.child(CurrentUserId + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getContext(), "Update Image Successfully.", Toast.LENGTH_SHORT).show();

                            Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();

                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();

                                    mDBUser.child("profileimage").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Intent selfIntent = new Intent(getContext(), NotificationFragment.class);
                                                        startActivity(selfIntent);
                                                        Toast.makeText(getContext(), "Update Image Successfully.", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    } else {
                                                        String message = task.getException().getMessage();
                                                        Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Error:", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }
//    private void getAddress( final Context context, final Handler handler) {
//            Thread thread = new Thread() {
//                @Override public void run() {
//                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                    String result = null;
//                    try {
//                        List<Address> list = geocoder.getFromLocation(
//                                location.getLatitude(), location.getLongitude(), 1);
//                        if (list != null && list.size() > 0) {
//                            Address address = list.get(0);
//                            // sending back first address line and locality
//                            result = address.getAddressLine(0) + ", " + address.getLocality();
//                        }
//                    } catch (IOException e) {
//                        Log.e(TAG, "Impossible to connect to Geocoder", e);
//                    } finally {
//                        Message msg = Message.obtain();
//                        msg.setTarget(handler);
//                        if (result != null) {
//                            msg.what = 1;
//                            Bundle bundle = new Bundle();
//                            bundle.putString("address", result);
//                            msg.setData(bundle);
//                        } else
//                            msg.what = 0;
//                        msg.sendToTarget();
//                    }
//                }
//            };
//            thread.start();
//    }

    private void SendUserToLoginActivity() {
        Intent intentLogin=new Intent(getActivity(),LoginActivity.class);
        startActivity(intentLogin);
        getActivity().finish();
    }
//    private class GeocoderHandler extends Handler {
//        @Override
//        public void handleMessage(Message message) {
//            String result;
//            switch (message.what) {
//                case 1:
//                    Bundle bundle = message.getData();
//                    result = bundle.getString("address");
//                    break;
//                default:
//                    result = null;
//            }
//            // replace by what you need to do
//            txtAddress.setText(result);
//        }
//    }

}
