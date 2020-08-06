package com.bagasbest.mygithub.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bagasbest.mygithub.R;
import com.bagasbest.mygithub.adapter.FollowerAdapter;
import com.bagasbest.mygithub.adapter.FollowingAdapter;
import com.bagasbest.mygithub.model.UserFollower;
import com.bagasbest.mygithub.model.UserFollowing;
import com.bagasbest.mygithub.viewmodel.FollowerViewModel;
import com.bagasbest.mygithub.viewmodel.FollowingViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowerFragment extends Fragment {

    ProgressDialog progressDialog;

    FollowerAdapter followerAdapter;
    RecyclerView recyclerView;
    FollowerViewModel followingViewModel;

    public FollowerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_follower, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        followingViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel.class);


        recyclerView = view.findViewById(R.id.rvFollower);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        followerAdapter = new FollowerAdapter();
        followerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(followerAdapter);

//        String username = getArguments().getString(ARG_NAME);

        progressDialog = new ProgressDialog(getActivity());

        progressDialog();
        followingViewModel.setFollower();

        followingViewModel.getUserFollowerList().observe(getActivity(), new Observer<ArrayList<UserFollower>>() {
            @Override
            public void onChanged(ArrayList<UserFollower> userFollowers) {
                if(userFollowers!= null) {
                    followerAdapter.setData(userFollowers);
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void progressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu, menu);

        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.about).setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
