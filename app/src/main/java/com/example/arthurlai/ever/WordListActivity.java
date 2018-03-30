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
import android.widget.Toast;

public class WordListActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        new GetWordList().execute();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
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

                cursor = db.query("WORDS",
                        new String[]{"_id", "Text_word"},
                        null, null, null, null, null);
                CursorAdapter listAdapter = new SimpleCursorAdapter(WordListActivity.this,
                        android.R.layout.simple_list_item_1,
                        cursor,
                        new String[]{"Text_word"},
                        new int[]{android.R.id.text1},
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
}
