package com.shihanah.tinycare;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    Spinner childSpinner, statusSpinner;
    EditText notesEditText;
    AppCompatButton saveAttendanceButton;

    FirebaseFirestore db;
    ArrayList<String> childNames = new ArrayList<>();
    ArrayList<String> childIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendance);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        childSpinner = findViewById(R.id.childSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        notesEditText = findViewById(R.id.notesEditText);
        saveAttendanceButton = findViewById(R.id.saveAttendanceButton);

        setupStatusSpinner();
        loadChildren();

        saveAttendanceButton.setOnClickListener(v -> saveAttendance());
    }

    private void setupStatusSpinner() {
        String[] statuses = {"Present", "Absent", "Late"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                statuses
        );

        statusSpinner.setAdapter(adapter);
    }

    private void loadChildren() {
        db.collection("children")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    childNames.clear();
                    childIds.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        childIds.add(document.getId());
                        childNames.add(document.getString("childName"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item,
                            childNames
                    );

                    childSpinner.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load children", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveAttendance() {
        if (childNames.isEmpty()) {
            Toast.makeText(this, "No children available", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = childSpinner.getSelectedItemPosition();

        String childId = childIds.get(selectedIndex);
        String childName = childNames.get(selectedIndex);
        String status = statusSpinner.getSelectedItem().toString();
        String notes = notesEditText.getText().toString().trim();

        Map<String, Object> attendance = new HashMap<>();
        attendance.put("childId", childId);
        attendance.put("childName", childName);
        attendance.put("status", status);
        attendance.put("notes", notes);

        db.collection("attendance")
                .add(attendance)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Attendance saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save attendance", Toast.LENGTH_SHORT).show();
                });
    }
}