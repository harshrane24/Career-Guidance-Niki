package com.example.careerr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalDetailsActivity extends AppCompatActivity {

    private EditText fullName, address, education, contact, email, dob;
    private Button submitButton;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        // Initialize views
        fullName = findViewById(R.id.editTextPDFullName2);
        address = findViewById(R.id.editTextPDAddress);
        education = findViewById(R.id.editTextPDEducation);
        contact = findViewById(R.id.editTextPDContact);
        email = findViewById(R.id.editTextPDEmail);
        dob = findViewById(R.id.editTextPDDOB);
        submitButton = findViewById(R.id.buttonPDSubmit);

        // Initialize database
        database = new Database(this);

        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePersonalDetails();
            }
        });
    }

    private void savePersonalDetails() {
        // Get values from input fields
        String name = fullName.getText().toString().trim();
        String addr = address.getText().toString().trim();
        String edu = education.getText().toString().trim();
        String contactNumber = contact.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String birthDate = dob.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || addr.isEmpty() || edu.isEmpty() || contactNumber.isEmpty() || mail.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert data into database
        boolean isInserted = database.insertData(name, addr, edu, contactNumber, mail, birthDate);

        // Show confirmation and redirect
        if (isInserted) {
            Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
            // Navigate to HomeActivity
            Intent intent = new Intent(PersonalDetailsActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Optional: closes the current activity so user can't go back to it using the back button
        } else {
            Toast.makeText(this, "Failed to save details", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        fullName.setText("");
        address.setText("");
        education.setText("");
        contact.setText("");
        email.setText("");
        dob.setText("");
    }
}
