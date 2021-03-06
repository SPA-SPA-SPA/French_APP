// 这是我的签名：LJY
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

    // 这个方法打开WordListActivity
    public void WordList(View view) {
        Intent intent = new Intent(this, WordListActivity.class);
        startActivity(intent);
    }

    // 这个方法打开StudyActivity
    public void Study(View view) {
        Intent intent = new Intent(this, StudyActivity.class);
        startActivity(intent);
    }
}
