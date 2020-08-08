package com.bagasbest.mygithub.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bagasbest.mygithub.R;
import com.bagasbest.mygithub.activity.DetailActivity;
import com.bagasbest.mygithub.model.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

//    Context context;
    private ArrayList<User> userList = new ArrayList<>();





    public void setData(ArrayList<User> users) {
        userList.clear();
        userList.addAll(users);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_github, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.UserViewHolder holder, final int position) {
        holder.bind(userList.get(position));

        String userIv = userList.get(position).getImage();

        try {
            Glide.with(holder.itemView.getContext()).load(userIv)
                    .placeholder(R.drawable.ic_face_black_24dp).into(holder.imgProfil);
        }catch (Exception e) {
            e.getMessage();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passingValueToDetailActivity(holder, position);
            }
        });

    }


    private void passingValueToDetailActivity(UserAdapter.UserViewHolder holder, int position) {
        Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_NAME, userList.get(position).getName());
        intent.putExtra(DetailActivity.EXTRA_ID, userList.get(position).getId());
        intent.putExtra(DetailActivity.EXTRA_IMAGE, userList.get(position).getImage());

        holder.itemView.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvId, tvOrganization;
        ImageView imgProfil;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.nameTv);
            tvId = itemView.findViewById(R.id.idTv);
            tvOrganization = itemView.findViewById(R.id.organizationTv);
            imgProfil = itemView.findViewById(R.id.userIv);

        }

        void bind(User user) {
            tvNama.setText(user.getName());
            tvId.setText(String.valueOf(user.getId()));
            tvOrganization.setText(user.getOrganization());
        }
    }
}
