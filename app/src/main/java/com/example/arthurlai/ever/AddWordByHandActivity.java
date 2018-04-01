package com.example.arthurlai.ever;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddWordByHandActivity extends AppCompatActivity {
    private EditText word_byhand;
    private EditText change_byhand;
    private EditText pronounces_byhand;
    private EditText trans_byhand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        word_byhand = (EditText)findViewById(R.id.word_byhand);
        change_byhand = (EditText) findViewById(R.id.change_byhand);
        pronounces_byhand = (EditText) findViewById(R.id.pronounces_byhand);
        trans_byhand = (EditText) findViewById(R.id.trans_byhand);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word_by_hand);
    }

    // 清空
    public void DeleteAllText (View view) {
        word_byhand.setText("");
        change_byhand.setText("");
        pronounces_byhand.setText("");
        trans_byhand.setText("");
    }
}
