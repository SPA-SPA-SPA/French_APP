package com.example.arthurlai.ever;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 这个方法打开FindWordActivity
    public void findWord(View view) {
        Intent intent = new Intent(this, FindWordActivity.class);
        startActivity(intent);
    }

    public void WordList(View view) {
        Intent intent = new Intent(this, WordListActivity.class);
        startActivity(intent);
    }
}
