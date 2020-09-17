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
    EditText overtime,inctime,traveltime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_time);

        Button send = (Button)findViewById(R.id.send);
        Button calc = (Button)findViewById(R.id.calc);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TotalTimeCalc();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WorkTime.this, TaskManager.class));
            }
        });
    }

    public void TotalTimeCalc () {

        totaltime = findViewById(R.id.totaltime);

        overtime = findViewById(R.id.overtime);
        inctime = findViewById(R.id.inctime);
        traveltime = findViewById(R.id.traveltime);

        int overT = Integer.parseInt(overtime.getText().toString());;
        int inc = Integer.parseInt(inctime.getText().toString());;
        int travel = Integer.parseInt(traveltime.getText().toString());;

        totaltime.setText(overT + inc + travel + " minutes");
    }


}