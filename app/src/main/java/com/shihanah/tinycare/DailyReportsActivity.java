package com.shihanah.tinycare;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
        Query query = db.collection("dailyEvents");

        if (childId != null) {
            query = query.whereEqualTo("childId", childId);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        String childName = document.getString("childName");
                        String meal = document.getString("meal");
                        String nap = document.getString("nap");
                        String mood = document.getString("mood");
                        String notes = document.getString("notes");

                        TextView reportCard = new TextView(DailyReportsActivity.this);

                        reportCard.setText(
                                "Child: " + childName +
                                        "\nMeal: " + meal +
                                        "\nNap: " + nap +
                                        "\nMood: " + mood +
                                        "\nNotes: " + notes
                        );

                        reportCard.setTextSize(17);
                        reportCard.setTextColor(getResources().getColor(R.color.tinycare_dark));
                        reportCard.setGravity(Gravity.CENTER_VERTICAL);
                        reportCard.setPadding(dp(18), dp(14), dp(18), dp(14));
                        reportCard.setBackgroundColor(Color.WHITE);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 0, 0, dp(16));
                        reportCard.setLayoutParams(params);

                        reportsContainer.addView(reportCard);
                    }
                });
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}