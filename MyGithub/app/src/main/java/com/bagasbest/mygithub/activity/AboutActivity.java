package com.bagasbest.mygithub.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bagasbest.mygithub.R;
import com.bumptech.glide.Glide;

public class AboutActivity extends AppCompatActivity {

    ImageView imageIv, ig, linkedin;
    TextView keteranganTv, quoteTv, myQuoteTv, contactTv, mediaTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        imageIv = findViewById(R.id.imageIv);
        ig = findViewById(R.id.ig);
        linkedin = findViewById(R.id.linkedIn);

        keteranganTv = findViewById(R.id.keteranganTv);
        quoteTv = findViewById(R.id.quote);
        myQuoteTv = findViewById(R.id.myQuote);
        contactTv = findViewById(R.id.contact);
        mediaTv = findViewById(R.id.media);

        setLocalization();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.about).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_change_settings){
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoInstagram(View view) {

        String link_ig = "https://www.instagram.com/bagas_best/?hl=id";
        String ig = "Instagram";

        alertDialogBuilder(link_ig, ig);


    }

    public void gotoLinkedIn(View view) {

        String link_linkedIn = "https://www.linkedin.com/in/bagas-pangestu/";
        String li = "LinkedIn";

        alertDialogBuilder(link_linkedIn, li);
    }

    private void alertDialogBuilder(final String key, String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menju "+ name +" Bagas Pangestu");
        builder.setMessage("Apakah anda ingin menuju "+ name +" Bagas Pangestu ? ");
        builder.setIcon(R.drawable.ic_public_black_24dp);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(key));
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });



        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void setLocalization() {
        String keterangan = getResources().getString(R.string.keterangan);
        keteranganTv.setText(keterangan);
        String quote = getResources().getString(R.string.myquote);
        quoteTv.setText(quote);
        String myQuote = getResources().getString(R.string.jangan_pernah_berhenti_untuk_belajar);
        myQuoteTv.setText(myQuote);
        String contect = getResources().getString(R.string.kontak);
        contactTv.setText(contect);
        String media = getResources().getString(R.string.media_sosial_saya);
        mediaTv.setText(media);
    }



}
