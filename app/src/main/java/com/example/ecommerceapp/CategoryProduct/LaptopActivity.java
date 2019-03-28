package com.example.ecommerceapp.CategoryProduct;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.DetailLaptopActivity;
import com.example.ecommerceapp.DetailProductActivity;
import com.example.ecommerceapp.Model.Food;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.ViewHolder.ProductFoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LaptopActivity extends AppCompatActivity {
    private TextView txtPriceLaptop, txtProductNameLaptop;
    private ImageView imgProductLaptop;
    private RecyclerView rvListLaptop;
    private DatabaseReference mDBListLaptop;
    private FirebaseRecyclerAdapter adapterLaptop;
    private Toolbar toolbarLaptop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);

        Init();
        setSupportActionBar(toolbarLaptop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Laptop");
        mDBListLaptop = FirebaseDatabase.getInstance().getReference().child("Laptop");
        rvListLaptop.setHasFixedSize(true);
        rvListLaptop.setLayoutManager(new LinearLayoutManager(this));
        DisplayListFood();
    }


    private void Init() {
        txtPriceLaptop=findViewById(R.id.txtPriceFood);
        txtProductNameLaptop=findViewById(R.id.txtProductNameFood);
        imgProductLaptop=findViewById(R.id.imgProductFood);
        rvListLaptop=findViewById(R.id.rvListLaptop);
        toolbarLaptop=findViewById(R.id.toolbar_laptop);
    }

    private void DisplayListFood() {
        FirebaseRecyclerOptions<Food> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(mDBListLaptop,Food.class)
                .build();
        adapterLaptop=new FirebaseRecyclerAdapter<Food, ProductFoodViewHolder>(firebaseRecyclerOptions) {

            @NonNull
            @Override
            public ProductFoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.display_product, viewGroup, false);

                return new ProductFoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ProductFoodViewHolder holder, final int position, @NonNull final Food model) {
                final String cartId = getRef(holder.getAdapterPosition()).getKey();
                mDBListLaptop.child(cartId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final String laptopName = dataSnapshot.child("name").getValue().toString();
                            final String laptopImage = dataSnapshot.child("image").getValue().toString();
                            final String laptopPrice = dataSnapshot.child("price").getValue().toString();

                            holder.setFoodName(laptopName);
                            holder.setFoodImage(laptopImage);
                            holder.setFoodPrice(laptopPrice);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentDetail = new Intent(LaptopActivity.this, DetailLaptopActivity.class);
                        intentDetail.putExtra("id", model.getId());
                        startActivity(intentDetail);
                    }
                });
            }


        };

        rvListLaptop.setAdapter(adapterLaptop);

        rvListLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = rvListLaptop.indexOfChild(v);
                Toast.makeText(LaptopActivity.this, pos+"",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterLaptop.startListening();



    }

    @Override
    public void onStop() {
        super.onStop();
        adapterLaptop.stopListening();

    }
}
