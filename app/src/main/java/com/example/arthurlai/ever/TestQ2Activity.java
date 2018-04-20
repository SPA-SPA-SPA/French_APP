package com.example.arthurlai.ever;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestQ2Activity extends AppCompatActivity {
    private TextView wordQ;
    private TextView Text_changeA;
    private TextView Text_pronounceA;
    private TextView Text_transA;
    private Button ButtonYes;
    private Button ButtonNo;
    private Button ButtonAnswer;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_q2);

        wordQ = (TextView)findViewById(R.id.Text_word_q);
        Text_changeA = (TextView)findViewById(R.id.Text_change_a);
        Text_pronounceA = (TextView)findViewById(R.id.Text_pronounces_a);
        Text_transA = (TextView)findViewById(R.id.Text_trans_a);

        ButtonYes = (Button)findViewById(R.id.Button_yes);
        ButtonNo = (Button)findViewById(R.id.Button_no);
        ButtonAnswer = (Button)findViewById(R.id.Button_answer);

        ButtonYes.setVisibility(View.INVISIBLE);
        ButtonNo.setVisibility(View.INVISIBLE);
        new TestQ2Activity.openthegate().execute();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

    // 显示答案
    public void SeeAnswer (View view) {
        ButtonYes.setVisibility(View.VISIBLE);
        ButtonNo.setVisibility(View.VISIBLE);
        ButtonAnswer.setVisibility(View.INVISIBLE);

        Text_changeA.setText(cursor.getString(1));
        Text_pronounceA.setText(cursor.getString(2));
        Text_transA.setText(cursor.getString(3));
    }

    // 说不认识
    public void No (View view) {
        ButtonYes.setVisibility(View.INVISIBLE);
        ButtonNo.setVisibility(View.INVISIBLE);
        ButtonAnswer.setVisibility(View.VISIBLE);
        ContentValues Values = new ContentValues();
        Values.put("test", "false");
        db.update("TEST",
                Values,
                "Text_word = ?",
                new String[] {cursor.getString(0)});

        if (cursor.moveToNext()) {
            wordQ.setText(cursor.getString(0));
            Text_changeA.setText("");
            Text_pronounceA.setText("");
            Text_transA.setText("");
        } else {
            finish();
        }
    }

    // 说认识
    public void Yes (View view) {
        ButtonYes.setVisibility(View.INVISIBLE);
        ButtonNo.setVisibility(View.INVISIBLE);
        ButtonAnswer.setVisibility(View.VISIBLE);
        ContentValues Values = new ContentValues();
        Values.put("test", "true");
        db.update("TEST",
                Values,
                "Text_word = ?",
                new String[] {cursor.getString(0)});

        if (cursor.moveToNext()) {
            wordQ.setText(cursor.getString(0));
            Text_changeA.setText("");
            Text_pronounceA.setText("");
            Text_transA.setText("");
        } else {
            finish();
        }
    }

    // inner class to openthegate
    class  openthegate extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object[] objects) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(TestQ2Activity.this);
                db = EverDatabaseHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT WORDS.Text_word, Text_change, Text_pronounces, Text_trans FROM WORDS INNER JOIN TEST ON WORDS.Text_word = TEST.Text_word where TEST.test like 'false'",null);
                return true;
            }catch (SQLiteException e){
                return false;
            }
        }

        @Override
        protected void onPostExecute (Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(TestQ2Activity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                if(cursor.moveToFirst())
                    wordQ.setText(cursor.getString(0));
            }
        }
    }
}
