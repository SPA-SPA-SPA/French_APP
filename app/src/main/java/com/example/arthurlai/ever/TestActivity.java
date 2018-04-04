package com.example.arthurlai.ever;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TestActivity extends AppCompatActivity {
    private TextView Text_wrong_num;
    private TextView Text_right_num;
    private TextView Text_sum_num;
    private Cursor cursor_T;
    private Cursor cursor_F;
    public Integer word_num_T;
    public Integer word_num_F;
    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Text_right_num = (TextView)findViewById(R.id.text_right_num);
        Text_wrong_num = (TextView)findViewById(R.id.text_wrong_num);
        Text_sum_num = (TextView)findViewById(R.id.text_sum_num);
        Text_right_num.setTextColor(Color.CYAN);
        Text_wrong_num.setTextColor(Color.RED);
        Text_sum_num.setText(getIntent().getStringExtra("num"));
        new GetNum().execute();
    }

    @Override
    public void onRestart() {

        super.onRestart();
        new GetNum().execute();
    }

    // 测试所有单词
    public void TestAll(View view) {
        if (Integer.valueOf(getIntent().getStringExtra("num")) != 0) {
            Intent intent = new Intent(this, TestQActivity.class);
            startActivity(intent);
        }
    }

    // 测试部分单词
    public void TestSome(View view) {
        if(word_num_F > 0) {
            Intent intent = new Intent(this, TestQ2Activity.class);
            startActivity(intent);
        }
    }

    // Inner class to getnum
    private class GetNum extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object[] objects) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(TestActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();

                // 这个游标提供认识的单词数
                cursor_T = db.query("TEST",
                        new String[]{"test"},
                        "test = ?", new String[] {"true"}, null, null, null);

                // 这个游标提供不认识的单词数
                cursor_F = db.query("TEST",
                        new String[]{"test"},
                        "test = ?", new String[] {"false"}, null, null, null);

                word_num_T = cursor_T.getCount();
                word_num_F = cursor_F.getCount();
                cursor_T.close();
                cursor_F.close();
                return true;
            } catch (SQLException e){
                return false;
            }
        }

        protected void onPostExecute (Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(TestActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                // 更新单词数
                ((TextView)findViewById(R.id.text_right_num)).setText(word_num_T.toString());
                ((TextView)findViewById(R.id.text_wrong_num)).setText(word_num_F.toString());
            }
        }
    }

}
