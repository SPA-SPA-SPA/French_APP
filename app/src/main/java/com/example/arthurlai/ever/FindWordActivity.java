package com.example.arthurlai.ever;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FindWordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_word);
    }

    public void deleteWord (View view) {
        EditText editText = (EditText) findViewById(R.id.edit_text);
        editText.setText("");
    }
}
