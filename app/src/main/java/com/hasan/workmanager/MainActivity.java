package com.hasan.workmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button doWorkBtn, cancelBtn;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doWorkBtn = findViewById(R.id.button);
        textView = findViewById(R.id.resultTV);
        cancelBtn = findViewById(R.id.cancelBtn);

        // You can not set less then 15 as time interval
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                MyWorker.class,
                15,
                TimeUnit.MINUTES
                ).build();

        doWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance(MainActivity.this).enqueue(periodicWorkRequest);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance(MainActivity.this).cancelWorkById(periodicWorkRequest.getId());
            }
        });

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        String status = workInfo.getState().name();

                        textView.append(status + "\n");
                    }
                });

    }
}