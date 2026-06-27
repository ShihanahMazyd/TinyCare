package com.shihanah.tinycare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import androidx.appcompat.app.AlertDialog;

public class DailyReportsActivity extends AppCompatActivity {

    LinearLayout reportsContainer;
    FirebaseFirestore db;
    String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_reports);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        reportsContainer = findViewById(R.id.reportsContainer);
        db = FirebaseFirestore.getInstance();

        childId = getIntent().getStringExtra("childId");

        loadDailyReports();
    }

    private void loadDailyReports() {
        reportsContainer.removeAllViews();

        Query query = db.collection("dailyEvents");

        if (childId != null) {
            query = query.whereEqualTo("childId", childId);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        String reportId = document.getId();
                        String reportChildId = document.getString("childId");
                        String childName = document.getString("childName");
                        String meal = document.getString("meal");
                        String nap = document.getString("nap");
                        String mood = document.getString("mood");
                        String notes = document.getString("notes");

                        LinearLayout reportLayout = new LinearLayout(DailyReportsActivity.this);
                        reportLayout.setOrientation(LinearLayout.VERTICAL);
                        reportLayout.setPadding(dp(18), dp(14), dp(18), dp(14));
                        reportLayout.setBackgroundColor(Color.WHITE);

                        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        cardParams.setMargins(0, 0, 0, dp(16));
                        reportLayout.setLayoutParams(cardParams);

                        TextView reportText = new TextView(DailyReportsActivity.this);
                        reportText.setText(
                                "Child: " + childName +
                                        "\nMeal: " + meal +
                                        "\nNap: " + nap +
                                        "\nMood: " + mood +
                                        "\nNotes: " + notes
                        );
                        reportText.setTextSize(17);
                        reportText.setTextColor(getResources().getColor(R.color.tinycare_dark));
                        reportText.setGravity(Gravity.CENTER_VERTICAL);

                        AppCompatButton editReportButton = new AppCompatButton(DailyReportsActivity.this);
                        editReportButton.setText("Edit Report");
                        editReportButton.setTextColor(Color.WHITE);
                        editReportButton.setTextSize(15);
                        editReportButton.setAllCaps(false);
                        editReportButton.setBackgroundResource(R.drawable.button_background);

                        LinearLayout.LayoutParams editButtonParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                dp(50)
                        );
                        editButtonParams.setMargins(0, dp(12), 0, 0);
                        editReportButton.setLayoutParams(editButtonParams);

                        editReportButton.setOnClickListener(v -> {
                            Intent intent = new Intent(DailyReportsActivity.this, DailyEventActivity.class);
                            intent.putExtra("isEditMode", true);
                            intent.putExtra("reportId", reportId);
                            intent.putExtra("childId", reportChildId);
                            intent.putExtra("childName", childName);
                            intent.putExtra("meal", meal);
                            intent.putExtra("nap", nap);
                            intent.putExtra("mood", mood);
                            intent.putExtra("notes", notes);
                            startActivity(intent);
                        });

                        AppCompatButton deleteReportButton = new AppCompatButton(DailyReportsActivity.this);
                        deleteReportButton.setText("Delete Report");
                        deleteReportButton.setTextColor(Color.WHITE);
                        deleteReportButton.setTextSize(15);
                        deleteReportButton.setAllCaps(false);
                        deleteReportButton.setBackgroundResource(R.drawable.button_background);

                        LinearLayout.LayoutParams deleteButtonParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                dp(50)
                        );
                        deleteButtonParams.setMargins(0, dp(12), 0, 0);
                        deleteReportButton.setLayoutParams(deleteButtonParams);

                        deleteReportButton.setOnClickListener(v -> {
                            new AlertDialog.Builder(this)
                                    .setTitle("Delete Report")
                                    .setMessage("This action cannot be undone. Are you sure you want to delete this report?")
                                    .setPositiveButton("Delete", (dialog, which) -> {
                                        db.collection("dailyEvents").document(reportId)
                                                .delete()
                                                .addOnSuccessListener(unused -> {
                                                    Toast.makeText(this, "Report deleted successfully", Toast.LENGTH_SHORT).show();
                                                    loadDailyReports();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(this, "Failed to delete report", Toast.LENGTH_SHORT).show();
                                                });
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        });

                        reportLayout.addView(reportText);
                        reportLayout.addView(editReportButton);
                        reportLayout.addView(deleteReportButton);

                        reportsContainer.addView(reportLayout);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDailyReports();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}