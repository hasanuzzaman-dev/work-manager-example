package com.hasan.workmanager;

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
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button doWorkBtn;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doWorkBtn = findViewById(R.id.button);
        textView = findViewById(R.id.resultTV);

        // android recommend data size only 10 kb
        Data inputData = new Data.Builder()
                .putString("data","This is input data")
                .build();

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(inputData)
                .build();


        doWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance(MainActivity.this).enqueue(oneTimeWorkRequest);
            }
        });



        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        //String status = workInfo.getState().name();

                        if (workInfo != null && workInfo.getState().isFinished()){
                            Data retrieveData = workInfo.getOutputData();
                            String outputString = retrieveData.getString("output_data");

                            textView.append(outputString + "\n");
                        }

                    }
                });

    }
}