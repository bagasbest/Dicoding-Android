package com.bagasbest.mygithub.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

import com.bagasbest.mygithub.R;
import com.bagasbest.mygithub.adapter.UserAdapter;
import com.bagasbest.mygithub.model.User;
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

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Daftar pengguna GitHub");

        recyclerView = findViewById(R.id.rvListUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter();
        userAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(userAdapter);

        progressDialog = new ProgressDialog(this);




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
                    progressDialog();
                    setUserList(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void setUserList(String username) {
        final ArrayList <User> userArrayList = new ArrayList<>();

        final String url = "https://api.github.com/search/users?q=" + username;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token 344c2d7a43d608c0a6f8a7a889812e0c505ba80c");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String result = new String(responseBody);
                Log.d(TAG, result);

                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray items = responseObject.getJSONArray("items");

                    for (int i=0; i<items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        User user = new User();
                        user.setName(item.getString("login"));
                        user.setId(item.getInt("id"));
                        user.setOrganization(item.getString("organizations_url"));
                        user.setImage(item.getString("avatar_url"));

                        userArrayList.add(user);
                    }
                    //set data ke adapter
                    userAdapter.setData(userArrayList);
                    progressDialog.dismiss();

                } catch (Exception e) {
                    Log.d("Exception: ", e.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure: ", error.getMessage());
            }
        });

    }
}
