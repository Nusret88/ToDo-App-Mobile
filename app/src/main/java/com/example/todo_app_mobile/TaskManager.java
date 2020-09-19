package com.example.todo_app_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrinterId;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class TaskManager extends AppCompatActivity {
    AsyncTask<String, Void, String> asyncTaskAPI;
    HashMap<Integer, String> taskList;
    JSONArray jsonArray;
    int operatorID, maxRowNumber = 6, chosenTaskID;
    Button[] buttonAccept, buttonReject, buttonFinish;
    TableRow[] tableRowList;
    TextView[] textViewList;

    int[] aarrayOfAcceptButtons= {R.id.t1accept, R.id.t2accept, R.id.t3accept, R.id.t4accept, R.id.t5accept,R.id.t6accept};
    int[] aarrayOfDeclineButtons= {R.id.t1decline, R.id.t2decline, R.id.t3decline, R.id.t4decline, R.id.t5decline,R.id.t6decline};
    int[] aarrayOfFinishButtons= {R.id.t1finish, R.id.t2finish, R.id.t3finish, R.id.t4finish, R.id.t5finish,R.id.t6finish};
    int[] arrayofTableRows = {R.id.row1, R.id.row2, R.id.row3, R.id.row4, R.id.row5, R.id.row6};
    int[] arrayOfTextViews = {R.id.task1txt, R.id.task2txt, R.id.task3txt, R.id.task4txt, R.id.task5txt, R.id.task6txt};
    int clickedAcceptButtonIndex=0, clickedRejectButtonIndex=0, clickedFinishButtonIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        taskList = new LinkedHashMap<>();
        SharedPreferences opID = getSharedPreferences("OperatorID", MODE_PRIVATE);
        operatorID = opID.getInt("ID", 0);
        System.out.println("TaskManager class - op id is: " + operatorID);

        asyncTaskAPI = new RestConnectionTask();
        asyncTaskAPI.execute("https://taskmanager2020-api.herokuapp.com/api/tasks/" + operatorID);

        /*Button finish1 = (Button)findViewById(R.id.t1finish);

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
        });*/
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

        /**
         * Knapparna för varje task
         * @param result
         */
        protected void onPostExecute(final String result) {
            try {
                for(int i=0; i<jsonArray.length();i++){
                    JSONObject results = jsonArray.getJSONObject(i);
                    int taskID = results.getInt("TaskID");
                    String taskDesc = results.getString("Description");

                    taskList.put(taskID, taskDesc);
                }



            tableRowList = new TableRow[maxRowNumber];
            for(int a=0; a<maxRowNumber; a++){
                tableRowList[a] = (TableRow) findViewById(arrayofTableRows[a]);

                if(a>=taskList.size()){
                    tableRowList[a].setVisibility(View.INVISIBLE);
                }
            }

            textViewList = new TextView[taskList.size()];
            buttonAccept = new Button[taskList.size()];
            buttonReject = new Button[taskList.size()];
            buttonFinish = new Button[taskList.size()];
            for(int i=0; i<buttonAccept.length; i++){
                buttonAccept[i] = (Button) findViewById(aarrayOfAcceptButtons[i]);
                buttonReject[i] = (Button) findViewById(aarrayOfDeclineButtons[i]);
                buttonFinish[i] = (Button) findViewById(aarrayOfFinishButtons[i]);
                buttonFinish[i].setEnabled(false);
                buttonFinish[i].setAlpha(.5f);
                textViewList[i] = (TextView) findViewById(arrayOfTextViews[i]);
                String value = (String) taskList.values().toArray()[i];
                textViewList[i].setText(value);
            }

            for(Button btn:buttonAccept){
                btn.setOnClickListener(accListener);
            }
                for(Button btn:buttonReject){
                    btn.setOnClickListener(rejListener);
                }
                for(Button btn:buttonFinish){
                    btn.setOnClickListener(finListener);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private View.OnClickListener accListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            for (int i = 0; i < buttonAccept.length; i++)
            {
                if (buttonAccept[i].getId() == v.getId())
                {
                    clickedAcceptButtonIndex = i;
                    buttonAccept[i].setEnabled(false);
                    buttonAccept[i].setAlpha(.5f);
                    buttonReject[i].setEnabled(false);
                    buttonReject[i].setAlpha(.5f);
                    buttonFinish[i].setEnabled(true);
                    buttonFinish[i].setAlpha(1f);
                    break;
                }
            }
            chosenTaskID = (int) taskList.keySet().toArray()[clickedAcceptButtonIndex];
            System.out.println("TaskID of chosen task; " + chosenTaskID);
        }


    };
    private View.OnClickListener rejListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            for (int i = 0; i < buttonAccept.length; i++)
            {
                if (buttonReject[i].getId() == v.getId())
                {
                    clickedRejectButtonIndex = i;
                    tableRowList[i].setVisibility(View.INVISIBLE);

                    // Jag tror hashmapen skapas på nytt när man kommer in från finish
                    // så set status finish och rejected kanske räcker
                    //taskList.remove(i);
                    break;
                }
            }
            chosenTaskID = (int) taskList.keySet().toArray()[clickedRejectButtonIndex];
            System.out.println("TaskID of chosen task; " + chosenTaskID);
        }

    };
    private View.OnClickListener finListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            for (int i = 0; i < buttonFinish.length; i++)
            {
                if (buttonFinish[i].getId() == v.getId())
                {
                    clickedFinishButtonIndex = i;
                    startActivity(new Intent(TaskManager.this, WorkTime.class));
                    break;
                }
            }
            chosenTaskID = (int) taskList.keySet().toArray()[clickedFinishButtonIndex];
            System.out.println("TaskID of chosen task; " + chosenTaskID);
        }

    };
}