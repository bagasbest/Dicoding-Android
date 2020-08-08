package com.bagasbest.mygithub.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bagasbest.mygithub.R;
import com.bagasbest.mygithub.adapter.UserAdapter;
import com.bagasbest.mygithub.model.User;
import com.bagasbest.mygithub.viewmodel.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    UserAdapter userAdapter;
    RecyclerView recyclerView;
    MainViewModel mainViewModel;

    private ImageView emptyIv;
    private TextView emptyTv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Daftar pengguna GitHub");

        emptyIv = findViewById(R.id.emptyIv);
        emptyTv = findViewById(R.id.emptyTv);

        emptyIv.setVisibility(View.VISIBLE);
        emptyTv.setVisibility(View.VISIBLE);

        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);

        recyclerView = findViewById(R.id.rvListUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter();
        userAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(userAdapter);

        progressDialog = new ProgressDialog(this);

        mainViewModel.getUserList().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {


                if(users != null) {
                    emptyIv.setVisibility(View.GONE);
                    emptyTv.setVisibility(View.GONE);
                    userAdapter.setData(users);
                    progressDialog.dismiss();
                }

                else {
                    //tidak muncul image dan teks, padahal sudah visible
                    emptyIv.setVisibility(View.VISIBLE);
                    emptyTv.setVisibility(View.VISIBLE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if(searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.searc_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    if(!TextUtils.isEmpty(query.trim())) {
                        //searchView tidak kosong
                        progressDialog();
                        mainViewModel.setUserList(query,MainActivity.this);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {

//                    if (!TextUtils.isEmpty(query.trim())) {
//                        mainViewModel.setUserList(query);
//                    }
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.about) {
            //menuju about activity
            startActivity(new Intent(this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


}
