package com.bagasbest.myworkmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bagasbest.myworkmanager.service.MyWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //init view
    Button btnOneTimeTsk,btnPeriodicTask, btnCancel;
    EditText etCity;
    TextView tvStatus;
    private PeriodicWorkRequest periodicWorkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOneTimeTsk = findViewById(R.id.btnOneTimeTask);
        etCity = findViewById(R.id.etCity);
        tvStatus = findViewById(R.id.textStatus);
        btnPeriodicTask = findViewById(R.id.btnPeriodicTask);
        btnCancel = findViewById(R.id.cancel);
        btnOneTimeTsk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnPeriodicTask.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOneTimeTask :
                startOneTimeTsk();
                break;

            case R.id.btnPeriodicTask :
                startPeriodicTask();
                break;

            case R.id.cancel :
                cancelPeriodicTask();
                break;
        }
    }

    private void cancelPeriodicTask() {
//        WorkManager.getInstance().cancelWorkById(periodicWorkRequest.getId());
        WorkManager.getInstance().cancelAllWorkByTag("my_tag");
    }

    private void startPeriodicTask() {
        tvStatus.setText(getString(R.string.status));

        Data data = new Data.Builder()
                .putString(MyWorker.EXTRA_CITY, etCity.getText().toString().trim())
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("my_tag") //KELEBIHAN PAKAI TAG, KITA BISA MEMBATALKAN LEBIH DARI 1 TASK SEKALIGUS
                .build();

        WorkManager.getInstance().enqueue(periodicWorkRequest);

        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicWorkRequest.getId()).observe(MainActivity.this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                String status = workInfo.getState().name();
                tvStatus.append("\n"+status);
                btnCancel.setEnabled(false);
                if(workInfo.getState() == WorkInfo.State.ENQUEUED) {
                    btnCancel.setEnabled(true);
                }
            }
        });

    }

    private void startOneTimeTsk() {
        tvStatus.setText(getString(R.string.status));

        Data data = new Data.Builder()
                .putString(MyWorker.EXTRA_CITY, etCity.getText().toString().trim())
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
              .setInputData(data)
                .setConstraints(constraints)
                .addTag("my_tag")
              .build();

        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(MainActivity.this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        String status = workInfo.getState().name();
                        tvStatus.append("\n"+status);
                    }
                });
    }
}
