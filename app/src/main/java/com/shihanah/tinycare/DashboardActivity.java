package com.shihanah.tinycare;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    AppCompatButton addChildButton, childrenButton, attendanceButton, reportsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addChildButton = findViewById(R.id.addChildButton);
        childrenButton = findViewById(R.id.childrenButton);
        attendanceButton = findViewById(R.id.attendanceButton);
        reportsButton = findViewById(R.id.reportsButton);

        // Add Child
        addChildButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AddChildActivity.class);
            startActivity(intent);
        });

        // Children List
        childrenButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ChildrenActivity.class);
            startActivity(intent);
        });

        // Attendance
        attendanceButton.setOnClickListener(v -> {
            Toast.makeText(DashboardActivity.this, "Attendance screen coming soon", Toast.LENGTH_SHORT).show();
        });

        // Daily Reports
        reportsButton.setOnClickListener(v -> {
            Toast.makeText(DashboardActivity.this, "Daily Reports screen coming soon", Toast.LENGTH_SHORT).show();
        });
    }
}