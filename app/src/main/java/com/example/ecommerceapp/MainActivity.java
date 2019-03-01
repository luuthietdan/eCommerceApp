package com.example.ecommerceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGetLogin, btnGetAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        btnGetLogin.setOnClickListener(this);
        btnGetAccount.setOnClickListener(this);
    }

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
