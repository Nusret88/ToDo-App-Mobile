package com.example.todo_app_mobile;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UpdateTime {

    AsyncTask<String, Void, String> asyncUpdateTime;
    JSONArray jsonArray;

    public UpdateTime(int taskID, double totalTime, double overTime, double travelTime) {
        asyncUpdateTime = new RestConnectionUpdateTime();
        asyncUpdateTime.execute("https://taskmanager2020-api.herokuapp.com/api/updatetime/" + taskID + "/" +
                totalTime + "/" + overTime + "/" + travelTime);
    }

    private class RestConnectionUpdateTime extends AsyncTask<String, Void, String> {
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
                //jsonArray = new JSONArray(responseContent);
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

        }
    }
}
