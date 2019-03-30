package com.example.ecommerceapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ecommerceapp.R;

public  class CommentViewHolder extends RecyclerView.ViewHolder {
    public View mView;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mView = itemView;
    }

    public void setUsername(String username) {
        TextView txtCommentUsername = mView.findViewById(R.id.txtCommentUsername);
        txtCommentUsername.setText("@" + username + " ");
    }

    public void setDate(String date) {
        TextView txtDate = mView.findViewById(R.id.txtCommentDate);
        txtDate.setText("Date: " + date);
    }

    public void setTime(String time) {
        TextView txtTime = mView.findViewById(R.id.txtCommentTime);
        txtTime.setText("Time: " + time);
    }

    public void setComment(String comments) {
        TextView txtComment = mView.findViewById(R.id.txtCommentTextHere);
        txtComment.setText(comments);
    }
}