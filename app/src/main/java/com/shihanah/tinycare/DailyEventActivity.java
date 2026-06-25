package com.shihanah.tinycare;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DailyEventActivity extends AppCompatActivity {

    EditText mealEditText, napEditText, moodEditText, notesEditText;
    AppCompatButton saveEventButton;
    FirebaseFirestore db;

    String childId, childName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_event);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        childId = getIntent().getStringExtra("childId");
        childName = getIntent().getStringExtra("childName");

        mealEditText = findViewById(R.id.mealEditText);
        napEditText = findViewById(R.id.napEditText);
        moodEditText = findViewById(R.id.moodEditText);
        notesEditText = findViewById(R.id.notesEditText);
        saveEventButton = findViewById(R.id.saveEventButton);

        saveEventButton.setOnClickListener(v -> saveDailyEvent());
    }

    private void saveDailyEvent() {
        String meal = mealEditText.getText().toString().trim();
        String nap = napEditText.getText().toString().trim();
        String mood = moodEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();

        if (meal.isEmpty() || nap.isEmpty() || mood.isEmpty()) {
            Toast.makeText(this, "Please fill meal, nap, and mood", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> event = new HashMap<>();
        event.put("childId", childId);
        event.put("childName", childName);
        event.put("meal", meal);
        event.put("nap", nap);
        event.put("mood", mood);
        event.put("notes", notes);

        db.collection("dailyEvents")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Daily event saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save event", Toast.LENGTH_SHORT).show();
                });
    }
}