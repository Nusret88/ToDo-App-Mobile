package com.example.todo_app_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TaskManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        Button finish1 = (Button)findViewById(R.id.t1finish);

        finish1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TaskManager.this, WorkTime.class));
            }
        });

        Button finish2 = (Button)findViewById(R.id.t2finish);

        finish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TaskManager.this, WorkTime.class));
            }
        });

        Button finish3 = (Button)findViewById(R.id.t3finish);

        finish3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TaskManager.this, WorkTime.class));
            }
        });

        Button finish4 = (Button)findViewById(R.id.t4finish);

        finish4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TaskManager.this, WorkTime.class));
            }
        });
    }
}