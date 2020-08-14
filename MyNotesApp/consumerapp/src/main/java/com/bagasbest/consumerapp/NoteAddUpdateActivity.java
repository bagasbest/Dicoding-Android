package com.bagasbest.consumerapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bagasbest.consumerapp.db.DatabaseContract;
import com.bagasbest.consumerapp.entity.Note;
import com.bagasbest.consumerapp.helper.MappingHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.bagasbest.consumerapp.db.DatabaseContract.NoteColumns.CONTENT_URI;

public class NoteAddUpdateActivity extends AppCompatActivity
                implements View.OnClickListener{

    private EditText etTitle, etDescription;
    private Button btnSubmit;

    private boolean isEdit = false;
    private Note note;
    private int position;
    private Uri uriWithId;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add_update);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDesc);
        btnSubmit = findViewById(R.id.btnSubmit);


        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if(note != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            note = new Note();
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit) {
            //Uri yang di dapatkan disini akan digunakan untuk ambil data dari provider
            //content://com.bagasbest.mynotesapp/note/id
            uriWithId = Uri.parse(CONTENT_URI + "/" + note.getId());
            if(uriWithId != null) {
                Cursor cursor = getContentResolver().query(
                        uriWithId,
                        null,
                        null,
                        null,
                        null
                );

                if(cursor != null) {
                    note = MappingHelper.mapCursorToObject(cursor);
                    cursor.close();
                }
            }

            actionBarTitle = "Ubah";
            btnTitle = "Perbarui";

            if(note != null) {
                etTitle.setText(note.getTitle());
                etDescription.setText(note.getDescription());
            }

        } else {
            actionBarTitle = "Tambah";
            btnTitle  = "Simpan";
        }

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSubmit.setText(btnTitle);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSubmit) {
            String title = etTitle.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();

            if(TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)) {
                Toast.makeText(this, "Field cannot be blank", Toast.LENGTH_SHORT).show();
                return;
            }

            note.setTitle(title);
            note.setDescription(desc);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_NOTE, note);
            intent.putExtra(EXTRA_POSITION, position);

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.NoteColumns.TITLE, title);
            values.put(DatabaseContract.NoteColumns.DESCRIPTION, desc);

            if (isEdit) {
                //Gunakan uriWithId untuk update
                //content://com.bagasbest.mynotesapp/note/id
                getContentResolver().update(uriWithId, values, null, null);

                Toast.makeText(NoteAddUpdateActivity.this, "Satu item berhasil di perbarui...", Toast.LENGTH_SHORT).show();
                finish();

//                long result = noteHelper.update(String.valueOf(note.getId()), values);
//                if (result > 0) {
//                    setResult(RESULT_UPDATE, intent);
//                    finish();
//                } else {
//                    Toast.makeText(NoteAddUpdateActivity.this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
//                }
            } else {
                note.setDate(getCurrentDate());
                values.put(DatabaseContract.NoteColumns.DATE, getCurrentDate());

                //Gunakan content uri untuk insert
                //content://com.bagasbest.mynotesapp/note/
                getContentResolver().insert(CONTENT_URI, values);
                Toast.makeText(NoteAddUpdateActivity.this, "Satu item berhasil ditambahkan...", Toast.LENGTH_SHORT).show();
                finish();

//                long result = noteHelper.insert(values);
//
//                if(result > 0) {
//                    note.setId((int) result);
//                    setResult(RESULT_ADD, intent);
//                    finish();
//                } else {
//                    Toast.makeText(NoteAddUpdateActivity.this, "Gagal menambah data", Toast.LENGTH_SHORT).show();
//
//                }
            }
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete :
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;

            case android.R.id.home :
                showAlertDialog(ALERT_DIALOG_CLOSE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form ?";
        } else {
            dialogTitle = "Hapus Note";
            dialogMessage = "Apakah anda yakin ingin menghapus item ini";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(dialogTitle);
        builder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isDialogClose) {
                            finish();
                        } else {
                            //Gunakan uriWithId untuk delete
                            //content://com.bagasbest.mynotesapp/note/id
                            getContentResolver().delete(
                                    uriWithId,
                                    null,
                                    null
                            );

                            Toast.makeText(NoteAddUpdateActivity.this, "Satu item berhasil dihapus...", Toast.LENGTH_SHORT).show();
                            finish();

//                            long  result = noteHelper.deleteById(String.valueOf(note.getId()));
//                            if(result > 0) {
//                                Intent intent = new Intent();
//                                intent.putExtra(EXTRA_POSITION, position);
//                                setResult(RESULT_DELETE, intent);
//                                finish();
//                            } else {
//                                Toast.makeText(NoteAddUpdateActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }
                })

                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
