package com.bagasbest.myreadwritefile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fabNew;
    private FloatingActionButton fabOpen;
    private FloatingActionButton fabSave;
    private EditText etSubject;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabNew = findViewById(R.id.fabNew);
        fabOpen = findViewById(R.id.fabOpen);
        fabSave = findViewById(R.id.fabSave);

        etMessage = findViewById(R.id.etMessage);
        etSubject = findViewById(R.id.etSubject);

        fabSave.setOnClickListener(this);
        fabOpen.setOnClickListener(this);
        fabNew.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabNew :
                newFile();
                break;

            case  R.id.fabOpen :
                showList();
                break;

            case R.id.fabSave :
                saveFile();
                break;
        }
    }

    private void saveFile() {
        if(etSubject.getText().toString().isEmpty()) {
            Toast.makeText(this, "Title must be filled", Toast.LENGTH_SHORT).show();
        }else if(etMessage.getText().toString().isEmpty()) {
            Toast.makeText(this, "Message must be filled!", Toast.LENGTH_SHORT).show();
        } else {
            String title = etSubject.getText().toString();
            String text = etMessage.getText().toString();
            FileModel fileModel = new FileModel();
            fileModel.setFilename(title);
            fileModel.setData(text);
            FileHelper.writeToFile(fileModel, this);
            Toast.makeText(this, "Saving " + fileModel.getFilename() + " file", Toast.LENGTH_SHORT).show();

        }
    }

    private void showList() {
        ArrayList <String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, getFilesDir().list());
        final CharSequence[] items = arrayList.toArray(new CharSequence[arrayList.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadData(items[which].toString());
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadData(String title) {
        FileModel fileModel = FileHelper.reafFromFile(this, title);
        etSubject.setText(fileModel.getFilename());
        etMessage.setText(fileModel.getData());
        Toast.makeText(this, "Loading " + fileModel.getFilename() + " data", Toast.LENGTH_SHORT).show();
    }

    private void newFile() {
        etSubject.setText("");
        etMessage.setText("");
        Toast.makeText(this, "Clearing file", Toast.LENGTH_SHORT).show();
    }
}
