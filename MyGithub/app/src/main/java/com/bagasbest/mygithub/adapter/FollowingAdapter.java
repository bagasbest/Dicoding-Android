package com.bagasbest.mygithub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bagasbest.mygithub.R;
import com.bagasbest.mygithub.model.UserFollowing;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder> {

    private ArrayList<UserFollowing> userList = new ArrayList<>();

    public void setData(ArrayList<UserFollowing> users) {
        userList.clear();
        userList.addAll(users);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowingAdapter.FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_following, parent, false);
        return new FollowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.FollowingViewHolder holder, int position) {
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

    static class FollowingViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIv;
        TextView idTv, namaTv;

        FollowingViewHolder(@NonNull View itemView) {
            super(itemView);

            idTv = itemView.findViewById(R.id.idTv);
            namaTv = itemView.findViewById(R.id.nameTv);
            imageIv = itemView.findViewById(R.id.userIv);
        }

        void bind(UserFollowing userFollowing) {
            idTv.setText(String.valueOf(userFollowing.getId()));
            namaTv.setText(userFollowing.getNama());
        }
    }
}
