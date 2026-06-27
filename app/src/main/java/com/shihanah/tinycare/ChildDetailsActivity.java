package com.shihanah.tinycare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChildDetailsActivity extends AppCompatActivity {

    TextView childNameText, childAgeText, parentNameText, parentPhoneText, notesText;
    AppCompatButton addDailyEventButton, viewReportsButton, editChildButton, deleteChildButton;
    FirebaseFirestore db;

    String childId, childName, parentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_details);
        db = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        childNameText = findViewById(R.id.childNameText);
        childAgeText = findViewById(R.id.childAgeText);
        parentNameText = findViewById(R.id.parentNameText);
        parentPhoneText = findViewById(R.id.parentPhoneText);
        notesText = findViewById(R.id.notesText);
        addDailyEventButton = findViewById(R.id.addDailyEventButton);
        viewReportsButton = findViewById(R.id.viewReportsButton);
        editChildButton = findViewById(R.id.editChildButton);
        deleteChildButton = findViewById(R.id.deleteChildButton);

        childId = getIntent().getStringExtra("childId");
        parentEmail = getIntent().getStringExtra("parentEmail");
        childName = getIntent().getStringExtra("childName");
        String childAge = getIntent().getStringExtra("childAge");
        String parentName = getIntent().getStringExtra("parentName");
        String parentPhone = getIntent().getStringExtra("parentPhone");
        String notes = getIntent().getStringExtra("notes");

        childNameText.setText("Child Name: " + childName);
        childAgeText.setText("Age: " + childAge);
        parentNameText.setText("Parent: " + parentName);
        parentPhoneText.setText("Phone: " + parentPhone);
        notesText.setText("Notes: " + notes);

        addDailyEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChildDetailsActivity.this, DailyEventActivity.class);
            intent.putExtra("childId", childId);
            intent.putExtra("childName", childName);
            intent.putExtra("parentEmail", parentEmail);
            startActivity(intent);
        });
        viewReportsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChildDetailsActivity.this, DailyReportsActivity.class);
            intent.putExtra("childId", childId);
            intent.putExtra("parentEmail", parentEmail);
            startActivity(intent);
        });
        editChildButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChildDetailsActivity.this, AddChildActivity.class);
            intent.putExtra("isEditMode", true);
            intent.putExtra("childId", childId);
            intent.putExtra("childName", childName);
            intent.putExtra("childAge", childAge);
            intent.putExtra("parentName", parentName);
            intent.putExtra("parentEmail", parentEmail);
            intent.putExtra("parentPhone", parentPhone);
            intent.putExtra("notes", notes);
            startActivity(intent);
        });
        deleteChildButton.setOnClickListener(v -> {
            if (childId == null) {
                Toast.makeText(this, "Unable to delete child", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("children").document(childId)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Child deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to delete child", Toast.LENGTH_SHORT).show();
                    });
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadLatestChildData();
    }

    private void loadLatestChildData() {
        if (childId == null || childId.isEmpty()) {
            return;
        }

        db.collection("children").document(childId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        childName = document.getString("childName");
                        String childAge = document.getString("childAge");
                        String parentName = document.getString("parentName");
                        parentEmail = document.getString("parentEmail");
                        String parentPhone = document.getString("parentPhone");
                        String notes = document.getString("notes");

                        childNameText.setText("Child Name: " + childName);
                        childAgeText.setText("Age: " + childAge);
                        parentNameText.setText("Parent: " + parentName);
                        parentPhoneText.setText("Phone: " + parentPhone);
                        notesText.setText("Notes: " + notes);
                    }
                });
    }
}