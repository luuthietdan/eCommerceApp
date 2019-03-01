package com.example.ecommerceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button btnCreateAccount;
    private EditText edtName,edtPhoneNumber,edtPassword;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Init();
        progressDialog=new ProgressDialog(this);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        String name=edtName.getText().toString();
        String phone=edtPhoneNumber.getText().toString();
        String password=edtPassword.getText().toString();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name...", Toast.LENGTH_SHORT).show();
        }else  if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone...", Toast.LENGTH_SHORT).show();
        }else  if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        }else if (password.length()<6){
            Toast.makeText(this, "Password must higher 6 character...", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setTitle("Create Account");
            progressDialog.setMessage("Please wait while we are checking your information");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            ValidPhoneNumber(name,phone,password);
        }

    }

    private void ValidPhoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference DBRef;
        DBRef= FirebaseDatabase.getInstance().getReference();
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> userMap=new HashMap<>();
                    userMap.put("name",name);
                    userMap.put("phone",phone);
                    userMap.put("password",password);
                    DBRef.child("Users").child(phone).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Create account successfully.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intentLogin);
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Error: ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "This " + phone + "already exists", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again another phone.", Toast.LENGTH_SHORT).show();
                    Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Init() {
        btnCreateAccount=findViewById(R.id.btnCreateAccount);
        edtName=findViewById(R.id.edtName);
        edtPhoneNumber=findViewById(R.id.edtPhoneNumber);
        edtPassword=findViewById(R.id.edtPassword);
    }
}
