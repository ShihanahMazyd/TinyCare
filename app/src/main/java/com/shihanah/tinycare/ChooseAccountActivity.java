package com.shihanah.tinycare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ChooseAccountActivity extends AppCompatActivity {

    AppCompatButton nurseryButton, parentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account);

        nurseryButton = findViewById(R.id.nurseryButton);
        parentButton = findViewById(R.id.parentButton);

        nurseryButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseAccountActivity.this, MainActivity.class);
            intent.putExtra("accountType", "nursery");
            startActivity(intent);
        });

        parentButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseAccountActivity.this, MainActivity.class);
            intent.putExtra("accountType", "parent");
            startActivity(intent);
        });
    }
}