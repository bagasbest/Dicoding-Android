package com.bagasbest.mygithub.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bagasbest.mygithub.R;
import com.bumptech.glide.Glide;

public class AboutActivity extends AppCompatActivity {

    ImageView imageIv, ig, linkedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        imageIv = findViewById(R.id.imageIv);
        ig = findViewById(R.id.ig);
        linkedin = findViewById(R.id.linkedIn);

        setImageView();

    }

    private void setImageView() {
        String profil = "https://avatars0.githubusercontent.com/u/45277197?v=4";
        String instagram = "https://cdn.icon-icons.com/icons2/1584/PNG/512/3721672-instagram_108066.png";
        String linkedIn = "https://image.flaticon.com/icons/png/512/174/174857.png";

        try {
            Glide.with(this).load(profil).
                    placeholder(R.drawable.ic_face_black_24dp).into(imageIv);
        } catch (Exception e){
            imageIv.setImageResource(R.drawable.ic_face_black_24dp);
        }



        try {
            Glide.with(this).load(instagram)
                    .placeholder(R.drawable.ic_face_black_24dp).into(ig);
        }catch (Exception e){
            ig.setImageResource(R.drawable.ic_face_black_24dp);
        }



        try {
            Glide.with(this).load(linkedIn)
                    .placeholder(R.drawable.ic_face_black_24dp).into(linkedin);
        }catch (Exception e){
            linkedin.setImageResource(R.drawable.ic_face_black_24dp);
        }
    }
}
