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

import com.example.ecommerceapp.DetailProductActivity;
import com.example.ecommerceapp.DetailWineActivity;
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

public class WineActivity extends AppCompatActivity {
    private TextView txtPriceWine, txtProductNameWine;
    private ImageView imgProductWIne;
    private RecyclerView rvListWine;
    private DatabaseReference mDBListWine;
    private FirebaseRecyclerAdapter adapterWine;
    private Toolbar toolbarWine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine);

        Init();
        setSupportActionBar(toolbarWine);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Wine");
        mDBListWine = FirebaseDatabase.getInstance().getReference().child("Wine");
        rvListWine.setHasFixedSize(true);
        rvListWine.setLayoutManager(new LinearLayoutManager(this));
        DisplayListFood();
    }

    private void Init() {
        txtPriceWine=findViewById(R.id.txtPriceFood);
        txtProductNameWine=findViewById(R.id.txtProductNameFood);
        imgProductWIne=findViewById(R.id.imgProductFood);
        rvListWine=findViewById(R.id.rvListWine);
        toolbarWine=findViewById(R.id.toolbar_wine);
    }

    private void DisplayListFood() {
        FirebaseRecyclerOptions<Food> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(mDBListWine,Food.class)
                .build();
        adapterWine=new FirebaseRecyclerAdapter<Food, ProductFoodViewHolder>(firebaseRecyclerOptions) {

            @NonNull
            @Override
            public ProductFoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.display_product, viewGroup, false);

                return new ProductFoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ProductFoodViewHolder holder, int position, @NonNull final Food model) {
                final String cartId = getRef(holder.getAdapterPosition()).getKey();
                mDBListWine.child(cartId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final String foodName = dataSnapshot.child("name").getValue().toString();
                            final String foodImage = dataSnapshot.child("image").getValue().toString();
                            final String foodPrice = dataSnapshot.child("price").getValue().toString();

                            holder.setFoodName(foodName);
                            holder.setFoodImage(foodImage);
                            holder.setFoodPrice(foodPrice);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentDetail = new Intent(WineActivity.this, DetailWineActivity.class);
                        intentDetail.putExtra("id", model.getId());
                        startActivity(intentDetail);
                    }
                });
            }


        };

        rvListWine.setAdapter(adapterWine);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapterWine.startListening();



    }

    @Override
    public void onStop() {
        super.onStop();
        adapterWine.stopListening();

    }
}
