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

public class AddChildActivity extends AppCompatActivity {

    EditText childNameEditText, childAgeEditText, parentNameEditText, parentPhoneEditText, notesEditText;
    AppCompatButton saveChildButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_child);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        childNameEditText = findViewById(R.id.childNameEditText);
        childAgeEditText = findViewById(R.id.childAgeEditText);
        parentNameEditText = findViewById(R.id.parentNameEditText);
        parentPhoneEditText = findViewById(R.id.parentPhoneEditText);
        notesEditText = findViewById(R.id.notesEditText);
        saveChildButton = findViewById(R.id.saveChildButton);

        saveChildButton.setOnClickListener(v -> saveChild());
    }

    private void saveChild() {
        String childName = childNameEditText.getText().toString().trim();
        String childAge = childAgeEditText.getText().toString().trim();
        String parentName = parentNameEditText.getText().toString().trim();
        String parentPhone = parentPhoneEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();

        if (childName.isEmpty() || childAge.isEmpty() || parentName.isEmpty() || parentPhone.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> child = new HashMap<>();
        child.put("childName", childName);
        child.put("childAge", childAge);
        child.put("parentName", parentName);
        child.put("parentPhone", parentPhone);
        child.put("notes", notes);

        db.collection("children")
                .add(child)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Child saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save child", Toast.LENGTH_SHORT).show();
                });
    }
}