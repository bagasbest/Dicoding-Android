package com.bagasbest.mydeepnavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "title";
    public static  final String EXTRA_MESSAGE = "msg";

    //init view
    TextView tvTitle, tvMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvMessage = findViewById(R.id.tvMessage);
        tvTitle = findViewById(R.id.tvTitle);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String message = getIntent().getStringExtra(EXTRA_MESSAGE);

        tvTitle.setText(title);
        tvMessage.setText(message);

    }
}
