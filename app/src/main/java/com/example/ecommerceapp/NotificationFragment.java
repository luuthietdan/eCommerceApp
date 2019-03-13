package com.example.ecommerceapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationFragment extends Fragment {

    private Button btnLogout;
    private CircleImageView imgUser;
    private TextView txtNameUser;
    private DatabaseReference mDBUser;
    private FirebaseAuth mAuth;
    private String CurrentUserId="";

    private ImageView imgAccepting;
    private ImageView imgGetting;
    private ImageView imgShipping;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_notification, container, false);
        mAuth=FirebaseAuth.getInstance();
        btnLogout=view.findViewById(R.id.btnLogout);
        imgUser=view.findViewById(R.id.imgUser);

        txtNameUser=view.findViewById(R.id.txtNameUser);

        imgAccepting = view.findViewById(R.id.imgWaitAccept);
        imgGetting = view.findViewById(R.id.imgWaitGetting);
        imgShipping = view.findViewById(R.id.imgShipping);


        mDBUser= FirebaseDatabase.getInstance().getReference().child("Users");
        if (mAuth.getCurrentUser()==null){
            CurrentUserId="";
        }
        else {
            CurrentUserId=mAuth.getCurrentUser().getUid();
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Log out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                SendUserToLoginActivity();
            }
        });
        mDBUser.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if (dataSnapshot.hasChild("fullname"))
                    {
                        String fullname=dataSnapshot.child("fullname").getValue().toString();
                        txtNameUser.setText(fullname);
                    }
                    if(dataSnapshot.hasChild("profileimage"))
                    {
                        String image=dataSnapshot.child("profileimage").getValue().toString();

                        Picasso.get().load(image).into(imgUser);
                    }

                    else{
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

        return view;
    }

    private void SendUserToLoginActivity() {
        Intent intentLogin=new Intent(getActivity(),LoginActivity.class);
        startActivity(intentLogin);
        getActivity().finish();
    }

}
