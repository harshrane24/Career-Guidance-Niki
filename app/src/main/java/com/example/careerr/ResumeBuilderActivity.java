package com.example.careerr;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResumeBuilderActivity extends AppCompatActivity {

    private EditText editFullName, editContact, editEmail, editObjective;
    private EditText editDegree, editInstitution, editYear;
    private EditText editJobTitle, editCompany, editDuration, editResponsibilities;
    private EditText editSkills, editCertifications, editProjects;
    private Button btnDownloadResume;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_builder);

        // Initialize EditText fields
        editFullName = findViewById(R.id.editFullName);
        editContact = findViewById(R.id.editContact);
        editEmail = findViewById(R.id.editEmail);
        editObjective = findViewById(R.id.editObjective);
        editDegree = findViewById(R.id.editDegree);
        editInstitution = findViewById(R.id.editInstitution);
        editYear = findViewById(R.id.editYear);
        editJobTitle = findViewById(R.id.editJobTitle);
        editCompany = findViewById(R.id.editCompany);
        editDuration = findViewById(R.id.editDuration);
        editResponsibilities = findViewById(R.id.editResponsibilities);
        editSkills = findViewById(R.id.editSkills);
        editCertifications = findViewById(R.id.editCertifications);
        editProjects = findViewById(R.id.editProjects);

        // Initialize button
        btnDownloadResume = findViewById(R.id.btnDownloadResume);

        // Set button click listener
        btnDownloadResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    createPDF();
                } else {
                    requestPermission();
                }
            }
        });
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // No need to request permission for Android 10+
            return true;
        } else {
            // For Android 9 and below, check for storage permission
            return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createPDF();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createPDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int x = 50;
        int y = 50;

        paint.setTextSize(18);
        canvas.drawText("Resume", x, y, paint);
        y += 40;

        paint.setTextSize(14);
        canvas.drawText("Full Name: " + editFullName.getText().toString(), x, y, paint);
        y += 30;
        canvas.drawText("Contact: " + editContact.getText().toString(), x, y, paint);
        y += 30;
        canvas.drawText("Email: " + editEmail.getText().toString(), x, y, paint);
        y += 40;
        canvas.drawText("Objective: " + editObjective.getText().toString(), x, y, paint);
        y += 40;
        canvas.drawText("Education:", x, y, paint);
        y += 30;
        canvas.drawText("Degree: " + editDegree.getText().toString(), x, y, paint);
        y += 30;
        canvas.drawText("Institution: " + editInstitution.getText().toString(), x, y, paint);
        y += 30;
        canvas.drawText("Year of Graduation: " + editYear.getText().toString(), x, y, paint);
        y += 40;
        canvas.drawText("Work Experience:", x, y, paint);
        y += 30;
        canvas.drawText("Job Title: " + editJobTitle.getText().toString(), x, y, paint);
        y += 30;
        canvas.drawText("Company: " + editCompany.getText().toString(), x, y, paint);
        y += 30;
        canvas.drawText("Duration: " + editDuration.getText().toString(), x, y, paint);
        y += 30;
        canvas.drawText("Responsibilities: " + editResponsibilities.getText().toString(), x, y, paint);
        y += 40;
        canvas.drawText("Skills: " + editSkills.getText().toString(), x, y, paint);
        y += 40;
        canvas.drawText("Certifications: " + editCertifications.getText().toString(), x, y, paint);
        y += 40;
        canvas.drawText("Projects: " + editProjects.getText().toString(), x, y, paint);

        pdfDocument.finishPage(page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Resume.pdf");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

            if (uri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                    pdfDocument.writeTo(outputStream);
                    Toast.makeText(this, "Resume saved to Downloads!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to create file in Downloads", Toast.LENGTH_SHORT).show();
            }
        } else {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Resume.pdf");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                pdfDocument.writeTo(fos);
                Toast.makeText(this, "Resume downloaded to: " + file.getPath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
            }
        }

        pdfDocument.close();
    }
}
