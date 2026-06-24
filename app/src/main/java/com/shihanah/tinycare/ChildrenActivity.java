package com.shihanah.tinycare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ChildrenActivity extends AppCompatActivity {

    LinearLayout childrenListContainer;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_children);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        childrenListContainer = findViewById(R.id.childrenListContainer);
        db = FirebaseFirestore.getInstance();

        loadChildren();
    }

    private void loadChildren() {
        childrenListContainer.removeAllViews();

        db.collection("children")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        String name = document.getString("childName");
                        String age = document.getString("childAge");
                        String parentName = document.getString("parentName");
                        String parentPhone = document.getString("parentPhone");
                        String notes = document.getString("notes");

                        TextView childCard = new TextView(ChildrenActivity.this);

                        childCard.setText(name + "\nAge: " + age + " years");
                        childCard.setTextSize(18);
                        childCard.setTextColor(getResources().getColor(R.color.tinycare_dark));
                        childCard.setGravity(Gravity.CENTER_VERTICAL);
                        childCard.setPadding(dp(18), dp(12), dp(18), dp(12));
                        childCard.setBackgroundColor(Color.WHITE);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                dp(90)
                        );

                        params.setMargins(0, 0, 0, dp(16));
                        childCard.setLayoutParams(params);

                        childCard.setOnClickListener(v -> {
                            Intent intent = new Intent(ChildrenActivity.this, ChildDetailsActivity.class);
                            intent.putExtra("childName", name);
                            intent.putExtra("childAge", age);
                            intent.putExtra("parentName", parentName);
                            intent.putExtra("parentPhone", parentPhone);
                            intent.putExtra("notes", notes);
                            startActivity(intent);
                        });

                        childrenListContainer.addView(childCard);
                    }
                });
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}