package com.shihanah.tinycare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.appbar.MaterialToolbar;

public class ParentDashboardActivity extends AppCompatActivity {

    AppCompatButton dailyUpdateButton, childInfoButton;
    MaterialToolbar topAppBar;
    FirebaseFirestore db;

    String childId, childName, childAge, parentName, parentEmail, parentPhone, notes;

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

        db = FirebaseFirestore.getInstance();

        dailyUpdateButton = findViewById(R.id.dailyUpdateButton);
        childInfoButton = findViewById(R.id.childInfoButton);
        topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logoutMenuItem) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ParentDashboardActivity.this, ChooseAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                return true;
            }
            return false;
        });

        loadLinkedChild();

        dailyUpdateButton.setOnClickListener(v -> {
            if (childId == null) {
                Toast.makeText(this, "No child linked to this parent account", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(ParentDashboardActivity.this, DailyReportsActivity.class);
            intent.putExtra("childId", childId);
            startActivity(intent);
        });

        childInfoButton.setOnClickListener(v -> {
            if (childId == null) {
                Toast.makeText(this, "No child linked to this parent account", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(ParentDashboardActivity.this, ChildDetailsActivity.class);
            intent.putExtra("childId", childId);
            intent.putExtra("childName", childName);
            intent.putExtra("childAge", childAge);
            intent.putExtra("parentName", parentName);
            intent.putExtra("parentEmail", parentEmail);
            intent.putExtra("parentPhone", parentPhone);
            intent.putExtra("notes", notes);
            startActivity(intent);
        });
    }

    private void loadLinkedChild() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "No parent user logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        db.collection("children")
                .whereEqualTo("parentEmail", currentEmail)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "No child linked to this email", Toast.LENGTH_LONG).show();
                        return;
                    }

                    var document = queryDocumentSnapshots.getDocuments().get(0);

                    childId = document.getId();
                    childName = document.getString("childName");
                    childAge = document.getString("childAge");
                    parentName = document.getString("parentName");
                    parentEmail = document.getString("parentEmail");
                    parentPhone = document.getString("parentPhone");
                    notes = document.getString("notes");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load child information", Toast.LENGTH_SHORT).show();
                });
    }
}