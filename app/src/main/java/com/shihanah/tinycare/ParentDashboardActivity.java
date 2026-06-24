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

public class ParentDashboardActivity extends AppCompatActivity {

    AppCompatButton dailyUpdateButton, childInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dailyUpdateButton = findViewById(R.id.dailyUpdateButton);
        childInfoButton = findViewById(R.id.childInfoButton);

        dailyUpdateButton.setOnClickListener(v -> {
            Intent intent = new Intent(ParentDashboardActivity.this, DailyReportsActivity.class);
            startActivity(intent);
        });

        childInfoButton.setOnClickListener(v -> {
            Toast.makeText(
                    ParentDashboardActivity.this,
                    "Child information will appear here after linking the parent account to a child profile.",
                    Toast.LENGTH_LONG
            ).show();
        });
    }
}