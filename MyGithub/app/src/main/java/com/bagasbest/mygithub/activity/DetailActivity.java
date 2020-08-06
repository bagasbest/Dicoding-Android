package com.bagasbest.mygithub.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bagasbest.mygithub.R;
import com.bagasbest.mygithub.adapter.SelectionPagerAdapter;
import com.bagasbest.mygithub.model.User;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    ImageView img;
    TextView tvNama, usernameTv, tvId, tvOrganization, tvRepo, tvBio;

    public static final String EXTRA_NAME = "login";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_IMAGE = "avatar_url";

    private ProgressDialog progressDialog;

    String nama,organization, image, username, bio;
    int repo, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        img = findViewById(R.id.imageIv);
        tvNama = findViewById(R.id.namaTv);
        tvId = findViewById(R.id.idTv);
        tvOrganization = findViewById(R.id.organizationTv);
        usernameTv = findViewById(R.id.usernameTv);
        tvRepo = findViewById(R.id.publicRepo);
        tvBio = findViewById(R.id.bio);

        progressDialog = new ProgressDialog(this);

        setUserDetail();


        SelectionPagerAdapter selectionPagerAdapter = new SelectionPagerAdapter(getSupportFragmentManager(), this);
       // selectionPagerAdapter.name = username;
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(selectionPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setElevation(0);



    }

    @SuppressLint("SetTextI18n")
    private void setUserDetail() {
        progressDialog();

        username = getIntent().getStringExtra(EXTRA_NAME);
        id = getIntent().getIntExtra(EXTRA_ID, 0);
        image = getIntent().getStringExtra(EXTRA_IMAGE);

        usernameTv.setText(" aka " + username);
        tvId.setText(String.valueOf(id));
        try {
            Glide.with(this).load(image)
                    .placeholder(R.drawable.ic_face_black_24dp).into(img);
        }catch (Exception e) {
            e.getMessage();
        }

        setValues();


    }

    private void setValues() {

        final String url = "https://api.github.com/users/" + username;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token c9cab51a42a36bdde81b50ab26a7cf4bea1b1342");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String result = new String(responseBody);
                Log.d(TAG, result);

                try {
                    JSONObject responseObject = new JSONObject(result);

                    nama = responseObject.getString("name");
                    organization = responseObject.getString("company");
                    repo = responseObject.getInt("public_repos");
                    bio = responseObject.getString("bio");

                    setValueAttribute();

                progressDialog.dismiss();

                }catch (Exception e){
                    progressDialog.dismiss();
                    Toast.makeText(DetailActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Exception: ", e.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(DetailActivity.this, "Exception: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", error.getMessage());
            }
        });
    }

    private void setValueAttribute() {

        if(!nama.equals("null")){
            tvNama.setText(nama);
        }else {
            tvNama.setText(username);
        }

        if(!organization.equals("null")) {
            tvOrganization.setText(organization);
        } else {
            tvOrganization.setText(R.string.belum_mengisi);
        }

        if (repo != 0){
            tvRepo.setText(String.valueOf(repo));
        }else {
            tvRepo.setText(R.string.belum_memiliki);
        }

        if(!bio.equals("null")){
            tvBio.setText(bio);
        }else {
            tvBio.setText(R.string.belum_mengisi);
        }
    }

    private void progressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
