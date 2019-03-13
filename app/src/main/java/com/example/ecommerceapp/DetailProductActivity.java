package com.example.ecommerceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerceapp.Model.Food;
import com.example.ecommerceapp.Model.Users;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailProductActivity extends AppCompatActivity {
    private ImageView imgProductDetail;
    private TextView txtNameDetail,txtDescription,txtPriceDetail;
    private String productId="";
    private Button btnBuy;
    private ElegantNumberButton btnAmount;
    private FirebaseAuth mAuth;
    private String currentId="";
    private Toolbar toolbarDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        Init();
        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Food");
        productId=getIntent().getStringExtra("id");
        getProductDetail(productId);
        mAuth=FirebaseAuth.getInstance();
        currentId=mAuth.getCurrentUser().getUid();
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartList();
            }
        });
    }

    private void addToCartList() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());
        DatabaseReference mDBList=FirebaseDatabase.getInstance().getReference().child("Cart").child(currentId);

        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("id",productId);
        cartMap.put("name",txtNameDetail.getText().toString());
        cartMap.put("price",txtPriceDetail.getText().toString());
        cartMap.put("description",txtDescription.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",btnAmount.getNumber());
        cartMap.put("discount","");
        //Quan edited
        cartMap.put("status","shipping");
        cartMap.put("image","none");


        mDBList.child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(DetailProductActivity.this, "Add Product to cart successfully", Toast.LENGTH_SHORT).show();
                        Intent intentCart=new Intent(DetailProductActivity.this,CartFragment.class);
                        startActivity(intentCart);

                    }
            }
        });
    }

    private void getProductDetail(String productId) {
        DatabaseReference mDBDetail= FirebaseDatabase.getInstance().getReference().child("Food");
        mDBDetail.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Food food=dataSnapshot.getValue(Food.class);
                    txtNameDetail.setText(food.getName());
                    txtDescription.setText(food.getDescription());
                    txtPriceDetail.setText(food.getPrice());
                    Picasso.get().load(food.getImage()).into(imgProductDetail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Init() {
        imgProductDetail=findViewById(R.id.imgProductDetail);
        txtNameDetail=findViewById(R.id.txtNameDetail);
        txtDescription=findViewById(R.id.txtDescriptionDetail);
        txtPriceDetail=findViewById(R.id.txtPriceDetail);
        btnAmount=findViewById(R.id.btnAmount);
        btnBuy=findViewById(R.id.btnBuy);
        toolbarDetail=findViewById(R.id.toolbar_detail);

    }
}
