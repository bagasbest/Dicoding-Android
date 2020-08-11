package com.bagasbest.mysharedpreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName, tvAge, tvPhone, tvEmail, tvIsLoveMu;
    private Button btnSave;
    private UserPreference userPreference;

    private boolean isPreferenceEmpty = false;
    private UserModel userModel;

    private final int REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAge = findViewById(R.id.tvAge);
        tvIsLoveMu = findViewById(R.id.tvLoved);

        btnSave = findViewById(R.id.btnSave);

        if(getSupportActionBar()!= null) {
            getSupportActionBar().setTitle("My User Preference");
        }

        userPreference = new UserPreference(this);

        showExistingPreference();

        btnSave.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave) {
            Intent intent = new Intent(this, FormUserPreferenceActivity.class);
            if (isPreferenceEmpty) {
                intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_ADD);
                intent.putExtra("USER", userModel);
            } else {
                intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_EDIT);
                intent.putExtra("USER", userModel);
            }
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == FormUserPreferenceActivity.RESULT_CODE) {
                userModel = data.getParcelableExtra(FormUserPreferenceActivity.EXTRA_RESULT);
                populateView(userModel);
                checkForm(userModel);
            }
        }
    }

    private void showExistingPreference() {
        userModel = userPreference.getUser();
        populateView(userModel);
        checkForm(userModel);
    }

    private void checkForm(UserModel userModel) {
        if(!userModel.getName().isEmpty() || tvName.getText().toString().trim().equals("Tidak ada")) {
            btnSave.setText(getString(R.string.change));
            isPreferenceEmpty = false;
        }else {
            btnSave.setText(getString(R.string.save));
            isPreferenceEmpty = true;
        }
    }

    private void populateView(UserModel userModel) {
        tvName.setText(userModel.getName().isEmpty() ? "Tidak ada" : userModel.getName());
        tvAge.setText(String.valueOf(userModel.getAge()).isEmpty() ? "Tidak ada" : String.valueOf(userModel.getAge()));
        tvIsLoveMu.setText(userModel.isLove() ? "Ya" : "Tidak");
        tvEmail.setText(userModel.getEmail().isEmpty() ? "Tidak ada" : userModel.getEmail());
        tvPhone.setText(userModel.getPhoneNumber().isEmpty() ? "Tidak ada" : userModel.getPhoneNumber());
    }


}
