package com.bagasbest.mygithub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bagasbest.mygithub.R;
import com.bagasbest.mygithub.model.UserFollower;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder> {

    private ArrayList<UserFollower> userList = new ArrayList<>();

    public void setData(ArrayList<UserFollower> users) {
        userList.clear();
        userList.addAll(users);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowerAdapter.FollowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_follower, parent, false);
        return new FollowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerAdapter.FollowerViewHolder holder, int position) {
        holder.bind(userList.get(position));

        String image = userList.get(position).getImage();

        try {
            Glide.with(holder.itemView.getContext()).load(image)
                    .placeholder(R.drawable.ic_face_black_24dp).into(holder.imageIv);
        } catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

     static class FollowerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIv;
        TextView idTv, nameTv;

         FollowerViewHolder(@NonNull View itemView) {
            super(itemView);

            idTv = itemView.findViewById(R.id.idTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            imageIv = itemView.findViewById(R.id.userIv);

        }

         void bind(UserFollower userFollower) {
            idTv.setText(String.valueOf(userFollower.getId()));
            nameTv.setText(userFollower.getNama());
        }
    }
}
