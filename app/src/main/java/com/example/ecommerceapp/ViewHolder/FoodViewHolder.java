package com.example.ecommerceapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private ImageButton imgLikeSuggestion;
    private TextView txtTotalLike;
    private String currentUserId;
    private int countLikes;
    private DatabaseReference mDBLikeSuggestion;
    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;
        imgLikeSuggestion=mView.findViewById(R.id.imgLikeSuggestion);
        txtTotalLike=mView.findViewById(R.id.txtTotalLike);
        mDBLikeSuggestion= FirebaseDatabase.getInstance().getReference().child("Likes");
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

    }
    public void setFoodImage(String foodImage){
        ImageView imgFood=mView.findViewById(R.id.imgProductSuggestion);
        Picasso.get().load(foodImage).into(imgFood);
    }
    public void setFoodName(String foodName){
        TextView txtFoodName=mView.findViewById(R.id.txtProductName);
        txtFoodName.setText(foodName);
    }
    public void setFoodPrice(String foodPrice){
        TextView txtFoodPrice=mView.findViewById(R.id.txtProductPrice);
        txtFoodPrice.setText(foodPrice);
    }
    public void setLikeButtonStatus(final String postKey) {
        mDBLikeSuggestion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postKey).hasChild(currentUserId)){
                    countLikes=(int) dataSnapshot.child(postKey).getChildrenCount();
                    imgLikeSuggestion.setImageResource(R.drawable.dislike);
                    txtTotalLike.setText(Integer.toString(countLikes));


                }
                else
                {
                    countLikes=(int) dataSnapshot.child(postKey).getChildrenCount();
                    imgLikeSuggestion.setImageResource(R.drawable.like);
                    txtTotalLike.setText(Integer.toString(countLikes));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
