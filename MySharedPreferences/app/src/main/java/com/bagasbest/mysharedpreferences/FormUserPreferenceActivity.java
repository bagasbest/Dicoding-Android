package com.bagasbest.mysharedpreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FormUserPreferenceActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etEmail, etPhone, etAge;
    private RadioGroup rgLoveMu;
    private RadioButton rbYes, rbNo;
    private Button btnSave;

    public static final String EXTRA_TYPE_FORM = "extra_type_form";
    public final static String EXTRA_RESULT = "extra_result";
    public static final int RESULT_CODE = 101;

    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT= 2;


    public final String FIELD_REQUIRED = "Field tidak boleh kosong";
    public final String FIELD_DIGIT_ONLY = "Hanya boleh terisi angka";
    public final String FIELD_IS_NOT_VALID = "Email tidak valid";

    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_user_preference);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        rgLoveMu = findViewById(R.id.rg_loveMu);
        rbYes = findViewById(R.id.rb_yes);
        rbNo = findViewById(R.id.rb_no);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);

        userModel = getIntent().getParcelableExtra("USER");
        int formType = getIntent().getIntExtra(EXTRA_TYPE_FORM, 0);

        String actionBarTitle ="";
        String btnTitle ="";

        switch (formType) {
            case TYPE_ADD:
                actionBarTitle = "Tambah Baru";
                btnTitle = "Simpan";
                break;

            case TYPE_EDIT:
                actionBarTitle = "Ubah";
                btnTitle = "Update";
                showPreferenceInform();
        }

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSave.setText(btnTitle);
    }

    private void showPreferenceInform() {
        etName.setText(userModel.getName());
        etEmail.setText(userModel.getEmail());
        etAge.setText(String.valueOf(userModel.getAge()));
        etPhone.setText(userModel.getPhoneNumber());
        if(userModel.isLove()) {
            rbYes.setChecked(true);
        }else {
            rbNo.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave) {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String phoneNo = etPhone.getText().toString().trim();
            boolean isLoveMu = rgLoveMu.getCheckedRadioButtonId() == R.id.rb_yes;

            if(TextUtils.isEmpty(name)) {
                etName.setError(FIELD_REQUIRED);
                return;
            }

            if(TextUtils.isEmpty(email)) {
                etEmail.setError(FIELD_REQUIRED);
                return;
            }

            if(!isValidEmail(email)) {
                etEmail.setError(FIELD_IS_NOT_VALID);
                return;
            }

            if(TextUtils.isEmpty(age)) {
                etAge.setError(FIELD_REQUIRED);
                return;
            }

            if(TextUtils.isEmpty(phoneNo)) {
                etPhone.setError(FIELD_REQUIRED);
                return;
            }

            if(!TextUtils.isDigitsOnly(phoneNo)) {
                etAge.setError(FIELD_DIGIT_ONLY);
                return;
            }

            saveUser(name, email, age, phoneNo, isLoveMu);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_RESULT, userModel);
            setResult(RESULT_CODE, resultIntent);

            finish();
        }
    }

    private void saveUser(String name, String email, String age, String phoneNo, boolean isLoveMu) {
        UserPreference userPreference = new UserPreference(this);

        userModel.setName(name);
        userModel.setEmail(email);
        userModel.setAge(Integer.parseInt(age));
        userModel.setPhoneNumber(phoneNo);
        userModel.setLove(isLoveMu);

        userPreference.setUser(userModel);
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
