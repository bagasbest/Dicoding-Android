package com.bagasbest.mygithub.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bagasbest.mygithub.R;
import com.bagasbest.mygithub.adapter.UserAdapter;
import com.bagasbest.mygithub.model.User;
import com.bagasbest.mygithub.viewmodel.MainViewModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    UserAdapter userAdapter;
    RecyclerView recyclerView;
    MainViewModel mainViewModel;

    private ImageView emptyIv;
    private TextView emptyTv;

    private static final String TAG = MainActivity.class.getSimpleName();


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

    //    private void setUserList(String username) {
//        final ArrayList <User> userArrayList = new ArrayList<>();
//
//        final String url = "https://api.github.com/search/users?q=" + username;
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.addHeader("Authorization", "token c9cab51a42a36bdde81b50ab26a7cf4bea1b1342");
//        client.addHeader("User-Agent", "request");
//        client.get(url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//                String result = new String(responseBody);
//                Log.d(TAG, result);
//
//                try {
//                    JSONObject responseObject = new JSONObject(result);
//                    JSONArray items = responseObject.getJSONArray("items");
//
//                    for (int i=0; i<items.length(); i++) {
//                        JSONObject item = items.getJSONObject(i);
//                        User user = new User();
//                        user.setName(item.getString("login"));
//                        user.setId(item.getInt("id"));
//                        user.setOrganization(item.getString("organizations_url"));
//                        user.setImage(item.getString("avatar_url"));
//
//                        userArrayList.add(user);
//                    }
//                    userAdapter.notifyDataSetChanged();
//                    //set data ke adapter
//                    userAdapter.setData(userArrayList);
//                    progressDialog.dismiss();
//
//                } catch (Exception e) {
//                    progressDialog.dismiss();
//                    Toast.makeText(MainActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.d("Exception: ", e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "onFailure: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("onFailure: ", error.getMessage());
//            }
//        });
//
//    }
}
