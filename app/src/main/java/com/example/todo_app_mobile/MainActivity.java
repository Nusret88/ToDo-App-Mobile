package com.example.todo_app_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView textloginfail;
    private EditText textUsername, textPassword;
    HashMap<String, String> usernamePassword;
    HashMap<String, String> usernameOperatorID;
    int operatorID = 0;
    private SharedPreferences.Editor editor;

    AsyncTask<String, Void, String> asyncLoginAPI;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usernamePassword = new HashMap<>();
        usernameOperatorID = new HashMap<>();

        SharedPreferences opID = getSharedPreferences("OperatorID",MODE_PRIVATE);
        editor = opID.edit();

        asyncLoginAPI = new RestConnectionLogin();
        asyncLoginAPI.execute("https://taskmanager2020-api.herokuapp.com/api/operators/");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textUsername = (EditText) findViewById(R.id.txtUName);
                textPassword = findViewById(R.id.txtPsw);
                textloginfail = findViewById(R.id.loginfail);


                for(Map.Entry<String, String> entry : usernamePassword.entrySet()) {
                    if (entry.getKey().equals(textUsername.getText().toString()) && entry.getValue().equals(textPassword.getText().toString())) {
                        operatorID =  Integer.parseInt(usernameOperatorID.get(textUsername.getText().toString()));
                        System.out.println("The current operator ID: " + operatorID);
                        editor.clear();
                        editor.putInt("ID",operatorID);
                        editor.commit();

                        startActivity(new Intent(MainActivity.this, TaskManager.class));
                        break;
                    }
                 //   else if (!entry.getKey().equals(textUsername.getText().toString()) && entry.getValue().equals(textPassword.getText().toString())) {
                 //       textloginfail.setText("Your username or password is incorrect \n                     please try again"); }
                }
            }
        });
    }

    private class RestConnectionLogin extends AsyncTask<String, Void, String> {
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
                    String userName = results.getString("Username");
                    String operatorPassword = results.getString("OperatorPassword");
                    String operatorID = results.getString("OperatorID");

                    usernamePassword.put(userName, operatorPassword);
                    usernameOperatorID.put(userName, operatorID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}