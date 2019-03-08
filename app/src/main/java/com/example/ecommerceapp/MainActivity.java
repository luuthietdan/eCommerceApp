package com.example.ecommerceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerceapp.Admin.AddProductActivity;
import com.example.ecommerceapp.Model.Users;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGetLogin, btnGetAccount;
    private ProgressDialog progressDialog;
    private String parentData="Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        btnGetLogin.setOnClickListener(this);
        btnGetAccount.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
//        Paper.init(this);
//        AllowUserLogin();
    }

//    private void AllowUserLogin() {
//        String userPhoneKey=Paper.book().read(Prevalent.UserPhone);
//        String userPassword=Paper.book().read(Prevalent.UserPassword);
//        if (userPhoneKey!="" && userPassword!=""){
//            if (!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPassword)){
//
//                progressDialog.setTitle("Login");
//                progressDialog.setMessage("Please wait while we are checking your information");
//                progressDialog.setCanceledOnTouchOutside(false);
//                progressDialog.show();
//                AllowAccess(userPhoneKey,userPassword);
//            }
//        }
//    }
//
//    private void AllowAccess(final String userPhoneKey, final String userPassword) {
//        final DatabaseReference DBRefLogin;
//        DBRefLogin= FirebaseDatabase.getInstance().getReference();
//        DBRefLogin.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(parentData).child(userPhoneKey).exists()){
//                    Users usersData=dataSnapshot.child(parentData).child(userPhoneKey).getValue(Users.class);
//                    if (usersData.getPhone().equals(userPhoneKey)){
//                        if (usersData.getPassword().equals(userPassword)) {
//                            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                            Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
//                            startActivity(intentHome);
//                            finish();
////                            if (parentData.equals("Admin")) {
////
////                                Toast.makeText(MainActivity.this, "Login with admin successfully", Toast.LENGTH_SHORT).show();
////                                Intent intentHome = new Intent(MainActivity.this, AddProductActivity.class);
////                                startActivity(intentHome);
////                                finish();
////                            }
////                            else if (parentData.equals("Users")){
////                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
////                                Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
////                                startActivity(intentHome);
////                                finish();
////                            }
//                        }else {
//                            Toast.makeText(MainActivity.this, "Please check your password", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else {
//                        Toast.makeText(MainActivity.this, "Please check your phone number", Toast.LENGTH_SHORT).show();
//                    }
//
//                }else {
//                    Toast.makeText(MainActivity.this, "Account with this " +userPhoneKey+ "do not exists", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void Init() {
        btnGetAccount = findViewById(R.id.btnGetSignUp);
        btnGetLogin = findViewById(R.id.btnGetLogin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGetLogin:
                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.btnGetSignUp:
                Intent intentSignUp = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intentSignUp);
                break;
            default:
                break;
        }
    }
}
