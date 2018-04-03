package com.example.arthurlai.ever;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void TestAll(View view) {
        Intent intent = new Intent(this, TestQActivity.class);
        startActivity(intent);
    }
}
