package com.bagasbest.myalarmmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    TextView tvOnceDate, tvOnceTime, tvRepeating;
    EditText etOnceMessage, etRepeating;
    ImageButton btnOnceDate, btnOnceTime, btnRepeating;
    Button btnSetOnce, btnSetRepeating, btnCancel;

    private AlarmReceiver alarmReceiver;

    final String DATE_PICKER_TAG = "DatePicker";
    final String TIME_PICKER_ONCE_TAG = "TimePickerOnce";
    final String TIME_PICKER_REPEAT_TAG = "TimerPickerRepeat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //casting view
        tvOnceDate = findViewById(R.id.tv_once_date);
        tvOnceTime = findViewById(R.id.tv_once_time);
        etOnceMessage = findViewById(R.id.edt_once_message);
        btnOnceDate = findViewById(R.id.btn_once_date);
        btnOnceTime = findViewById(R.id.btn_once_time);
        btnSetOnce = findViewById(R.id.btn_set_once_alarm);
        tvRepeating = findViewById(R.id.tv_repeating_time);
        etRepeating = findViewById(R.id.et_repeating_message);
        btnRepeating = findViewById(R.id.btn_repeating_time);
        btnSetRepeating = findViewById(R.id.btn_set_repeating);
        btnCancel = findViewById(R.id.btn_cancel);


        btnOnceDate.setOnClickListener(this);
        btnOnceTime.setOnClickListener(this);
        btnSetOnce.setOnClickListener(this);
        btnRepeating.setOnClickListener(this);
        btnSetRepeating.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void onClick(View v) {
        switch  (v.getId()) {
            case R.id.btn_once_date :
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
                break;
            case R.id.btn_once_time :
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), TIME_PICKER_ONCE_TAG);
                break;
            case R.id.btn_set_once_alarm :
                String onceDate = tvOnceDate.getText().toString();
                String onceTime = tvOnceTime.getText().toString();
                String onceMessage = etOnceMessage.getText().toString();

                alarmReceiver.setOneTimeAlarm(this, AlarmReceiver.TYPE_ONE_TIME,
                        onceDate,
                        onceTime,
                        onceMessage);
                break;

            case R.id.btn_repeating_time :
                TimePickerFragment timePickerFragment1 = new TimePickerFragment();
                timePickerFragment1.show(getSupportFragmentManager(),TIME_PICKER_REPEAT_TAG);
                break;
            case R.id.btn_set_repeating :
                String repeatTime = tvRepeating.getText().toString().trim();
                String repeatMessage = etRepeating.getText().toString().trim();
                alarmReceiver.setRepeatingAlarm(
                        this,
                        AlarmReceiver.TYPE_REPEATING,
                        repeatTime,
                        repeatMessage);
                break;

            case R.id.btn_cancel :
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING);
                break;
        }
    }

    @Override
    public void onDialogDateSet(String tag, int year, int month, int dayOfMonth) {
        Calendar calendar =Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        tvOnceDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onDialogTimeSet(String tag, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        switch (tag) {
            case TIME_PICKER_ONCE_TAG :
                tvOnceTime.setText(dateFormat.format(calendar.getTime()));

                break;
            case TIME_PICKER_REPEAT_TAG :
                tvRepeating.setText(dateFormat.format(calendar.getTime()));
                break;

            default:
                break;
        }
    }
}
