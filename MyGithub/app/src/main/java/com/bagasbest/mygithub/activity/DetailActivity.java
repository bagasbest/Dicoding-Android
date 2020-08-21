package com.bagasbest.mygithub.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bagasbest.mygithub.R;
import com.bagasbest.mygithub.adapter.SelectionPagerAdapter;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    ImageView img;
    TextView tvNama;
    TextView usernameTv;
    TextView tvId;
    TextView tvOrganization;
    TextView tvRepo;
    TextView tvBio;

    public static final String EXTRA_NAME = "login";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_IMAGE = "avatar_url";


    String nama;
    String organization;
    String image;
    String username;
    String bio;
    int repo;
    int id;

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


        setUserDetail();


        SelectionPagerAdapter selectionPagerAdapter = new SelectionPagerAdapter(getSupportFragmentManager(), this, username);
        selectionPagerAdapter.name = username;
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(selectionPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        Objects.requireNonNull(getSupportActionBar()).setElevation(0);



    }

    @SuppressLint("SetTextI18n")
    private void setUserDetail() {

        username = getIntent().getStringExtra(EXTRA_NAME);
        id = getIntent().getIntExtra(EXTRA_ID, 0);
        image = getIntent().getStringExtra(EXTRA_IMAGE);

        usernameTv.setText(" aka " + username);
        tvId.setText(String.valueOf(id));


        Glide.with(this)
                .load(image)
                .error(R.drawable.ic_face_black_24dp)
                .placeholder(R.drawable.ic_face_black_24dp)
                .into(img);


        setValues();


    }

    private void setValues() {

        final String url = "https://api.github.com/users/" + username;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token 26a65ff9842fb2bda5a6473b1e52f873e9086339");
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


                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
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


}
