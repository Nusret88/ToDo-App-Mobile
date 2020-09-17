package com.example.todo_app_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
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

    private EditText textUsername, textPassword;
    HashMap<String, String> usernamePassword;

    AsyncTask<String, Void, String> asyncLoginAPI;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usernamePassword = new HashMap<>();
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


                for(Map.Entry<String, String> entry : usernamePassword.entrySet()) {
                    if (entry.getKey().equals(textUsername.getText().toString()) && entry.getValue().equals(textPassword.getText().toString())) {
                        startActivity(new Intent(MainActivity.this, TaskManager.class));
                        break;
                    }
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
                    usernamePassword.put(userName, operatorPassword);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}