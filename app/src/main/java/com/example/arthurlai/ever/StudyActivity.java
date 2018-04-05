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

import java.util.Calendar;

public class StudyActivity extends AppCompatActivity {
    private TextView Text_should;
    private TextView Text_have;
    private TextView Text_yet;
    private Integer have = 0;
    private Integer yet = 0;
    private Integer year;
    private Integer month;
    private Integer date;
    private Cursor cursor_have;
    private Cursor cursor_yet;
    private Calendar today;
    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        
        Text_have = (TextView)findViewById(R.id.text_have);
        Text_yet = (TextView)findViewById(R.id.text_yet);
        Text_have.setTextColor(Color.CYAN);
        Text_yet.setTextColor(Color.RED);

        new GetNumAboutWords().execute();
    }

    @Override
    public void onRestart() {

        super.onRestart();
        cursor_have.close();
        cursor_yet.close();
        db.close();
        new GetNumAboutWords().execute();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor_have.close();
        cursor_yet.close();
        db.close();
    }

    public void start(View view) {
        if(yet > 0){
            Intent intent = new Intent(this, FibonacciActivity.class);
            startActivity(intent);
        }else {
            Toast toast = Toast.makeText(StudyActivity.this, "今天的内容已经学习完了", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Inner class to GetNumAboutWords
    private class GetNumAboutWords extends AsyncTask<Object, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Object... objects) {
            try {
                today = Calendar.getInstance();
                year = today.get(Calendar.YEAR);
                month = 1+today.get(Calendar.MONTH);
                date = today.get(Calendar.DATE);
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(StudyActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();

                cursor_yet = db.query("FIBONACCI",
                        new String[] {"Text_word"},
                        "year < ? or (year = ? and month < ?) or (year = ? and month = ? and date <= ?)",
                        new String[] {year.toString(), year.toString(), month.toString(), year.toString(), month.toString(), date.toString()},
                        null, null, null);

                cursor_have = db.query("WORDDATE",
                        new String[] {"Text_word"},
                        "year = ? and month = ? and date = ?",
                        new String[] {year.toString(), month.toString(), date.toString()},
                        null, null, null);
                have = cursor_have.getCount();
                yet = cursor_yet.getCount();
                return true;
            }catch(SQLException e){
                return false;
            }
        }

        protected void onPostExecute (Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(StudyActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                Text_have.setText(have.toString());
                Text_yet.setText(yet.toString());
            }
        }
    }
}
