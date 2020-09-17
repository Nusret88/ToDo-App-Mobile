package com.example.todo_app_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class TaskManager extends AppCompatActivity {
    AsyncTask<String, Void, String> asyncTaskAPI;
    HashMap<Integer, String> taskList;
    JSONArray jsonArray;
    int operatorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        taskList = new HashMap<>();
        SharedPreferences opID = getSharedPreferences("OperatorID", MODE_PRIVATE);
        operatorID = opID.getInt("ID", 0);
        System.out.println("TaskManager class - op id is: " + operatorID);

        asyncTaskAPI = new RestConnectionTask();
        asyncTaskAPI.execute("https://taskmanager2020-api.herokuapp.com/api/tasks/" + operatorID);


        Button finish1 = (Button)findViewById(R.id.t1finish);

        finish1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("The listTask hashmap: " + taskList);
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

    private class RestConnectionTask extends AsyncTask<String, Void, String> {
        private String responseContent, choice;
        private String Error = null;
        private URL url;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... requestData) {
            BufferedReader reader=null;

            try {
                url = new URL(requestData[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoOutput(false); // True för POST, PUT. False för GET

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null) {
                    sb.append(line + "  \n");
                }

                responseContent = sb.toString();
                jsonArray = new JSONArray(responseContent);
                System.out.println("The response from JSON Operator: " + responseContent);
            }
            catch(Exception ex) {
                Error = ex.getMessage();
                System.out.println("The error message is: " + Error);
            }
            finally {
                try {
                    reader.close();
                }
                catch(Exception ex) {

                }
            }
            return responseContent;
        }

        protected void onPostExecute(final String result) {
            try {
                for(int i=0; i<jsonArray.length();i++){
                    JSONObject results = jsonArray.getJSONObject(i);
                    int taskID = results.getInt("TaskID");
                    String taskDesc = results.getString("Description");

                    taskList.put(taskID, taskDesc);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}