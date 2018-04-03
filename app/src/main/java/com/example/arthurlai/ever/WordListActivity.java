package com.example.arthurlai.ever;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WordListActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor_getlist;
    private Cursor cursor_getnum;
    private Cursor cursor_getlist_new;
    private Cursor cursor_getlist_new_2;
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

    @Override
    public void onRestart() {
        super.onRestart();
        ListView listView = (ListView)findViewById(R.id.List_Words);
        cursor_getlist_new_2 = db.query("WORDS",
                new String[]{"_id", "Text_word", "Text_trans"},
                null, null, null, null, null);
        CursorAdapter adapter = (CursorAdapter)listView.getAdapter();
        adapter.changeCursor(cursor_getlist_new_2);
        cursor_getlist = cursor_getlist_new_2;
        new GetNumOfWords().execute();
    }

    // 一键删除所有单词
    public void deleteAllWords(View view) {
        new DeleteAllWords().execute();
    }

    // 手动添加单词
    public void addWordByhand2 (View view) {
        Intent intent = new Intent(this, AddWordByHandActivity.class);
        startActivity(intent);
    }

    // 测试按钮
    public void TestTouch (View view) {
        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("num", word_num.toString());
        startActivity(intent);
    }

    // Inner class to get wordlist
    private class GetWordList extends AsyncTask <Object, Void, Boolean>{
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Object[] objects) {
            ListView listView = (ListView)findViewById(R.id.List_Words);
            try {
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

    // Inner class to delete all words
    private class DeleteAllWords extends AsyncTask <Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object[] objects) {
            try {
                db.delete("WORDS",
                        null,
                        null);
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute (Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(WordListActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                ListView listView = (ListView)findViewById(R.id.List_Words);
                cursor_getlist_new = db.query("WORDS",
                        new String[]{"_id", "Text_word", "Text_trans"},
                        null, null, null, null, null);
                CursorAdapter adapter = (CursorAdapter)listView.getAdapter();
                adapter.changeCursor(cursor_getlist_new);
                cursor_getlist = cursor_getlist_new;
                cursor_getlist_new.close();
                new GetNumOfWords().execute();
            }
        }
    }
}
