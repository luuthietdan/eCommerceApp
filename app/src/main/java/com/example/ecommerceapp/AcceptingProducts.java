package com.example.ecommerceapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.ecommerceapp.Adapter.PendingAdapter;
import com.example.ecommerceapp.Model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AcceptingProducts extends AppCompatActivity {

    ListView lsvAcceptingProducts;
    ArrayList<Cart> mArrayList;
    PendingAdapter pendingAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    String CurrentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepting_products);
        initComponents();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        CurrentUserID = mAuth.getCurrentUser().getUid();
        myRef = database.getReference().child("ProductsProcess").child(CurrentUserID);
        mArrayList = new ArrayList<>();
        pendingAdapter = new PendingAdapter(this, R.layout.display_cart,mArrayList);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pendingAdapter.clear();
                Iterable<DataSnapshot> dataChildren = dataSnapshot.getChildren();
                for(DataSnapshot data: dataChildren){
                    Cart s = data.getValue(Cart.class);
                    if(s.getStatus().equals("accepting"))
                         mArrayList.add(s);
                    //Log.d("List","List length: "+ studentList.size());

                    Log.d("Products: ",s.getStatus() );
                }
                lsvAcceptingProducts.setAdapter(pendingAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initComponents() {
        lsvAcceptingProducts = findViewById(R.id.lsvAcceptingProducts);
    }
}
