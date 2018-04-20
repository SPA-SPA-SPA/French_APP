package com.example.arthurlai.ever;

import android.content.ContentValues;
import android.content.Intent;
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

import java.util.Calendar;

public class FibonacciActivity extends AppCompatActivity {
    private TextView wordQ;
    private TextView Text_changeA;
    private TextView Text_pronounceA;
    private TextView Text_transA;
    private Button ButtonYes;
    private Button ButtonNo;
    private Button ButtonAnswer;
    private Integer year;
    private Integer month;
    private Integer date;
    private Integer i = 0;
    private Integer j = 0;
    private Calendar today;
    private Calendar tool;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibonacci);

        wordQ = (TextView)findViewById(R.id.Text_word_q);
        Text_changeA = (TextView)findViewById(R.id.Text_change_a);
        Text_pronounceA = (TextView)findViewById(R.id.Text_pronounces_a);
        Text_transA = (TextView)findViewById(R.id.Text_trans_a);

        ButtonYes = (Button)findViewById(R.id.Button_yes);
        ButtonNo = (Button)findViewById(R.id.Button_no);
        ButtonAnswer = (Button)findViewById(R.id.Button_answer);

        today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH)+1;
        date = today.get(Calendar.DATE);

        ButtonYes.setVisibility(View.INVISIBLE);
        ButtonNo.setVisibility(View.INVISIBLE);
        new openthegate().execute();
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

        if (cursor.moveToNext()) {
            wordQ.setText(cursor.getString(0));
            Text_changeA.setText("");
            Text_pronounceA.setText("");
            Text_transA.setText("");
        } else {
            cursor.close();
            db.close();
            new openthegate().execute();
        }
    }

    // 说认识
    public void Yes (View view) {
        tool = Calendar.getInstance();
        ButtonYes.setVisibility(View.INVISIBLE);
        ButtonNo.setVisibility(View.INVISIBLE);
        ButtonAnswer.setVisibility(View.VISIBLE);
        ContentValues WValues = new ContentValues();
        WValues.put("Text_word", wordQ.getText().toString());
        WValues.put("year", tool.get(Calendar.YEAR));
        WValues.put("month",(tool.get(Calendar.MONTH))+1);
        WValues.put("date", tool.get(Calendar.DATE));
        db.insert("WORDDATE", null, WValues);

        i = cursor.getInt(5);
        j = cursor.getInt(4)+cursor.getInt(5);
        tool.add(Calendar.DATE, j);
        ContentValues FValues = new ContentValues();
        FValues.put("year", tool.get(Calendar.YEAR));
        FValues.put("month",tool.get(Calendar.MONTH)+1);
        FValues.put("date", tool.get(Calendar.DATE));
        FValues.put("pre", i);
        FValues.put("R", cursor.getInt(4)+cursor.getInt(5));
        db.update("FIBONACCI",
                FValues,
                "Text_word = ?",
                new String[] {cursor.getString(0)});

        if (cursor.moveToNext()) {
            wordQ.setText(cursor.getString(0));
            Text_changeA.setText("");
            Text_pronounceA.setText("");
            Text_transA.setText("");
        } else {
            cursor.close();
            db.close();
            new openthegate().execute();
        }
    }

    // inner class to openthegate
    class  openthegate extends AsyncTask<Object, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Object[] objects) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(FibonacciActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();
                String q = "SELECT WORDS.Text_word, Text_change, Text_pronounces," +
                        " Text_trans, pre, R FROM WORDS INNER JOIN FIBONACCI ON WORDS.Text_word " +
                        "= FIBONACCI.Text_word where (year < ?) or (year = ? and month < ?) or " +
                        "(year = ? and month = ? and date <= ?)";
                cursor = db.rawQuery(q,new String[] {year.toString(), year.toString(),
                        month.toString(), year.toString(), month.toString(), date.toString()});
                return true;
            }catch (SQLiteException e){
                return false;
            }
        }

        @Override
        protected void onPostExecute (Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(FibonacciActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                if(cursor.moveToFirst()){
                    wordQ.setText(cursor.getString(0));
                    Text_changeA.setText("");
                    Text_pronounceA.setText("");
                    Text_transA.setText("");
                }
                else {
                    Intent intent = new Intent(FibonacciActivity.this, finishActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}
