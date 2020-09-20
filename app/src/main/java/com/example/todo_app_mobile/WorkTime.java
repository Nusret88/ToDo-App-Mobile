package com.example.todo_app_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WorkTime extends AppCompatActivity {

    TextView totaltime;
    EditText overtime, inctime, traveltime;
    Intent in;
    int taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_time);

        in = getIntent();
        taskID = in.getIntExtra("taskID", 0);

        totaltime = findViewById(R.id.totaltime);
        overtime = findViewById(R.id.overtime);
        traveltime = findViewById(R.id.traveltime);

        Button send = (Button)findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTime updateTime = new UpdateTime(taskID, Double.parseDouble(totaltime.getText().toString()),
                        Double.parseDouble(overtime.getText().toString()), Double.parseDouble(traveltime.getText().toString()));
                startActivity(new Intent(WorkTime.this, TaskManager.class));
            }
        });
    }


}