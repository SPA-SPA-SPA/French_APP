package com.example.arthurlai.ever;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WordListActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor_getlist;
    private Cursor cursor_getnum;
    private Integer word_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        new GetNumOfWords().execute();
        new GetWordList().execute();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor_getlist.close();
        db.close();
    }

    // Inner class to get wordlist
    private class GetWordList extends AsyncTask {
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Object[] objects) {
            ListView listView = (ListView)findViewById(R.id.List_Words);
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(WordListActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();

                // 这个游标给列表提供单词
                cursor_getlist = db.query("WORDS",
                        new String[]{"_id", "Text_word", "Text_trans"},
                        null, null, null, null, null);
                CursorAdapter listAdapter = new SimpleCursorAdapter(WordListActivity.this,
                        android.R.layout.simple_list_item_2,
                        cursor_getlist,
                        new String[]{"Text_word", "Text_trans"},
                        new int[]{android.R.id.text1, android.R.id.text2},
                        0);
                listView.setAdapter(listAdapter);
                return true;
            } catch(SQLException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(WordListActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // Inner class to getnum
    private class GetNumOfWords extends AsyncTask<Object, Void, Boolean> {
        TextView justAView = (TextView) findViewById(R.id.Text_JustAView);

        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Object[] objects) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(WordListActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();

                // 这个游标给列表提供单词总数
                cursor_getnum = db.query("WORDS",
                        new String[]{"Text_word"},
                        null, null, null, null, null);
                word_num = cursor_getnum.getCount();
                cursor_getnum.close();
                db.close();
                return true;
            } catch (SQLException e){
                return false;
            }
        }

        protected void onPostExecute (Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(WordListActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                // 更新单词数
                String word_justAView = word_num.toString() + "个单词";
                justAView.setText(word_justAView);
            }
        }
    }
}
