package com.example.arthurlai.ever;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddWordByHandActivity extends AppCompatActivity {
    private EditText word_byhand;
    private EditText change_byhand;
    private EditText pronounces_byhand;
    private EditText trans_byhand;
    private Integer year;
    private Integer month;
    private Integer date;
    private Calendar today;
    private SQLiteDatabase db;
    private Cursor cursor;
    public Boolean notIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word_by_hand);

        today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH)+1;
        date = today.get(Calendar.DATE);

        word_byhand = (EditText)findViewById(R.id.word_byhand);
        change_byhand = (EditText) findViewById(R.id.change_byhand);
        pronounces_byhand = (EditText) findViewById(R.id.pronounces_byhand);
        trans_byhand = (EditText) findViewById(R.id.trans_byhand);
        String messageText = getIntent().getStringExtra("word");
        word_byhand.setText(messageText);
    }

    // 清空
    public void DeleteAllText (View view) {
        word_byhand.setText("");
        change_byhand.setText("");
        pronounces_byhand.setText("");
        trans_byhand.setText("");
    }

    // 添加单词
    public void AddWordByHand (View view) {
        if ((word_byhand.getText().toString().length() != 0) &&
                (word_byhand.getText().toString().charAt(0) != ' ')
                && (trans_byhand.getText().toString().length() != 0))
        {
            new checkWord().execute(word_byhand.getText().toString());
        }
        else {
            Toast toast = Toast.makeText(AddWordByHandActivity.this, "请填好带*项", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Inner class to addwordbyhand
    class AddWordByHand extends AsyncTask <Object, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Object[] objects) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(AddWordByHandActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();
                // 添加单词
                ContentValues wordValues = new ContentValues();
                wordValues.put("Text_Word", word_byhand.getText().toString());
                wordValues.put("Text_change", change_byhand.getText().toString());
                wordValues.put("Text_pronounces", pronounces_byhand.getText().toString());
                wordValues.put("Text_music", "");
                wordValues.put("Text_trans", trans_byhand.getText().toString());
                db.insert("WORDS", null, wordValues);

                // 添加test标志
                ContentValues testValues = new ContentValues();
                testValues.put("Text_word", word_byhand.getText().toString());
                testValues.put("test", "");
                db.insert("TEST",null, testValues);

                // 添加进生词本时加进斐波那契学习进度中
                ContentValues FibonacciValues = new ContentValues();
                FibonacciValues.put("Text_word", word_byhand.getText().toString());
                FibonacciValues.put("year", year);
                FibonacciValues.put("month",month);
                FibonacciValues.put("date", date);
                FibonacciValues.put("pre", 1);
                FibonacciValues.put("R", 0);
                db.insert("FIBONACCI", null, FibonacciValues);
                db.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute (Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(AddWordByHandActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                Toast toast = Toast.makeText(AddWordByHandActivity.this, "添加成功！", Toast.LENGTH_SHORT);
                toast.show();
                word_byhand.setText("");
                change_byhand.setText("");
                pronounces_byhand.setText("");
                trans_byhand.setText("");
            }
        }
    }

    // Inner class to checkWord
    class checkWord extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... editText) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(AddWordByHandActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();
                cursor = db.query("WORDS",
                        new String[] {"Text_word"},
                        "Text_word = ?",
                        new String[] {editText[0]},
                        null, null, null);
                if (cursor.moveToFirst())
                    notIn = false;
                else
                    notIn = true;
                cursor.close();
                db.close();
                return true;
            }catch (SQLiteException e){
                return false;
            }
        }

        protected void onPostExecute(Boolean success){
            if (!success) {
                Toast toast = Toast.makeText(AddWordByHandActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }else if (notIn)
                new AddWordByHand().execute();
            else
            {
                Toast toast = Toast.makeText(AddWordByHandActivity.this, "这个单词已经存在！", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
