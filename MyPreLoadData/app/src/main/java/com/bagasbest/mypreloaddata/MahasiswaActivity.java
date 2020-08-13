package com.bagasbest.mypreloaddata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bagasbest.mypreloaddata.adapter.MahasiswaAdapter;
import com.bagasbest.mypreloaddata.database.MahasiswaHelper;
import com.bagasbest.mypreloaddata.model.MahasiswaModel;

import java.util.ArrayList;

public class MahasiswaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MahasiswaAdapter mahasiswaAdapter = new MahasiswaAdapter();
        recyclerView.setAdapter(mahasiswaAdapter);

        MahasiswaHelper mahasiswaHelper = new MahasiswaHelper(this);
        mahasiswaHelper.open();

        //ambil semua data mahasiswa di database
        ArrayList<MahasiswaModel> listMahasiswa = mahasiswaHelper.getAllData();
        mahasiswaHelper.close();

        mahasiswaAdapter.setData(listMahasiswa);

    }
}
