package com.example.careerr;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    private CardView cardCareerGuidance, cardResumeBuilder;
    private Database databaseHelper;
    private static final String GEMINI_API_KEY = "AIzaSyDg16PjS4-sgQg-itv05yteqi_Psz9wQGo";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cardCareerGuidance = findViewById(R.id.cardLabTest);
        cardResumeBuilder = findViewById(R.id.cardResumeBuilder); // Ensure this ID matches your XML

        databaseHelper = new Database(this);

        cardCareerGuidance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPersonalDetails();
            }
        });

        cardResumeBuilder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ResumeBuilderActivity
                Intent intent = new Intent(HomeActivity.this, ResumeBuilderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchPersonalDetails() {
        Cursor cursor = databaseHelper.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            String fullName = cursor.getString(1);
            String address = cursor.getString(2);
            String education = cursor.getString(3);
            String contact = cursor.getString(4);
            String email = cursor.getString(5);
            String dob = cursor.getString(6);

            String userDetails = "Full Name: " + fullName + "\nEducation: " + education + "\nDOB: " + dob;
            new GetCareerGuidanceTask().execute(userDetails);
        } else {
            Toast.makeText(this, "No personal details found.", Toast.LENGTH_SHORT).show();
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private class GetCareerGuidanceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String userDetails = params[0];
            HttpURLConnection connection = null;
            try {
                URL url = new URL(GEMINI_API_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject requestBody = new JSONObject();
                JSONObject contents = new JSONObject();
                contents.put("parts", new JSONArray().put(new JSONObject().put("text", "Based on these personal details, suggest suitable career paths: " + userDetails)));
                requestBody.put("contents", new JSONArray().put(contents));

                OutputStream os = connection.getOutputStream();
                os.write(requestBody.toString().getBytes("UTF-8"));
                os.close();

                int responseCode = connection.getResponseCode();
                Log.d("API_RESPONSE_CODE", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Scanner scanner = new Scanner(connection.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();
                    Log.d("API_SUCCESS", "Response: " + response.toString());
                    return response.toString();
                } else {
                    Scanner errorScanner = new Scanner(connection.getErrorStream());
                    StringBuilder errorResponse = new StringBuilder();
                    while (errorScanner.hasNext()) {
                        errorResponse.append(errorScanner.nextLine());
                    }
                    errorScanner.close();
                    Log.e("API_ERROR", "Error Response: " + errorResponse.toString());
                    return null;
                }
            } catch (Exception e) {
                Log.e("API_EXCEPTION", "Exception occurred: " + e.getMessage(), e);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray candidates = jsonResponse.getJSONArray("candidates");
                    if (candidates.length() > 0) {
                        JSONObject firstCandidate = candidates.getJSONObject(0);
                        String suggestion = firstCandidate.getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");

                        Intent intent = new Intent(HomeActivity.this, CareerGuidanceResultActivity.class);
                        intent.putExtra("careerSuggestions", suggestion);
                        startActivity(intent);
                    } else {
                        Toast.makeText(HomeActivity.this, "No career suggestions found.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e("JSON_PARSE_ERROR", "Error parsing response: " + e.getMessage());
                    Toast.makeText(HomeActivity.this, "Error parsing career suggestions.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(HomeActivity.this, "Failed to fetch career guidance. Please check API key and logs.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
