package com.bagasbest.myjobscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bagasbest.myjobscheduler.service.GetCurrentWeatherJobService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStart, btnCancel;
    private int jobId = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnCancel = findViewById(R.id.btnCancel);

        btnStart.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart :
                startJob();
                break;

            case R.id.btnCancel :
                cancelJob();
                break;
        }
    }

    private void cancelJob() {
        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancel(jobId);
        Toast.makeText(this, "Job service canceled", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void startJob() {
        if(isRunning(this)) {
            Toast.makeText(this, "Job service is already schduled", Toast.LENGTH_SHORT).show();
            return;
        }
        ComponentName mServiceComponent = new ComponentName(this, GetCurrentWeatherJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, mServiceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);

        //1000 ms = 1 detik
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPeriodic(900000); //15 menit
        } else {
            builder.setPeriodic(180000); // 3 menit
        }

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());

        Toast.makeText(this, "Job service started", Toast.LENGTH_SHORT).show();

    }

    private boolean isRunning(Context context) {
        boolean isSchduled = false;

        JobScheduler schduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if(schduler != null) {
            for(JobInfo jobInfo : schduler.getAllPendingJobs()) {
                if(jobInfo.getId() == jobId) {
                    isSchduled = true;
                    break;
                }
            }
        }

        return isSchduled;
    }
}
