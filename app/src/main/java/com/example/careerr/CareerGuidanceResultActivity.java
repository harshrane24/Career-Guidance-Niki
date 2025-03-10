package com.example.careerr;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CareerGuidanceResultActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_guidance_result);

        // Initialize views
        resultTextView = findViewById(R.id.textViewCareerResult);

        // Get career suggestions from intent
        String careerSuggestions = getIntent().getStringExtra("careerSuggestions");

        // Display the suggestions
        if (careerSuggestions != null && !careerSuggestions.isEmpty()) {
            resultTextView.setText(careerSuggestions);
        } else {
            resultTextView.setText("No career suggestions available.");
        }
    }
}
