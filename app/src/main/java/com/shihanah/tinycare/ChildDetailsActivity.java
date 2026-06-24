package com.shihanah.tinycare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChildDetailsActivity extends AppCompatActivity {

    TextView childNameText, childAgeText, parentNameText, parentPhoneText, notesText;
    AppCompatButton addDailyEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_details);

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

        String childName = getIntent().getStringExtra("childName");
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
            startActivity(intent);
        });
    }
}