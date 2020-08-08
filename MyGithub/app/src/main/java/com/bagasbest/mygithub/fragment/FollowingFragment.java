package com.bagasbest.mygithub.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bagasbest.mygithub.R;
import com.bagasbest.mygithub.adapter.FollowingAdapter;
import com.bagasbest.mygithub.model.UserFollowing;
import com.bagasbest.mygithub.viewmodel.FollowingViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    private ProgressDialog progressDialog;

    private FollowingAdapter followingAdapter;
    private RecyclerView recyclerView;
    private FollowingViewModel followingViewModel;

    private static final String ARG_NAME = "name";

    public FollowingFragment() {
        // Required empty public constructor
    }

    public static FollowingFragment newInstance (String name) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_NAME, name);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_following, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        followingViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);


        recyclerView = view.findViewById(R.id.rvFollowing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        followingAdapter = new FollowingAdapter();
        followingAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(followingAdapter);

       String username = getArguments().getString(ARG_NAME);

        progressDialog = new ProgressDialog(getActivity());

        progressDialog();
        followingViewModel.setFollowing(getActivity(), username);

        followingViewModel.getUserFollowingList().observe(getActivity(), new Observer<ArrayList<UserFollowing>>() {
            @Override
            public void onChanged(ArrayList<UserFollowing> userFollowings) {
                if(userFollowings != null) {
                    followingAdapter.setData(userFollowings);
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
