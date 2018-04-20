package com.example.arthurlai.ever;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SeeTheWordActivity extends AppCompatActivity {
    private TextView word_see;
    private TextView change_see;
    private TextView pronounce_see;
    private TextView trans_see;
    private Button Button_pre;
    private Button Button_next;
    private Button Button_music;
    private String TheWord;
    private String TheChange;
    private String Thepronounce;
    private String TheTrans;
    private String TheMusic = "";
    private Cursor cursor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_the_word);

        word_see = (TextView)findViewById(R.id.Text_word_see);
        change_see = (TextView)findViewById(R.id.Text_change_see);
        pronounce_see = (TextView) findViewById(R.id.Text_pronounces_see);
        trans_see = (TextView)findViewById(R.id.Text_trans_see);
        Button_pre = (Button)findViewById(R.id.Button_backward);
        Button_next = (Button)findViewById(R.id.Button_forward);
        Button_music = (Button)findViewById(R.id.Button_music);

        TheWord = getIntent().getStringExtra("word");
        word_see.setText(TheWord);
        new GetTheWord().execute(TheWord);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        new GetTheWord().execute(TheWord);
    }

    @Override
    public void onDestroy(){
        cursor.close();
        db.close();
        super.onDestroy();
    }

    // 播放读音
    public void music(View view) {
        String urlPath = TheMusic;
        if (TheMusic.length()!=0) {
            MediaPlayer mPlayer = MediaPlayer.create( this, Uri.parse(urlPath) );
            mPlayer.start();
        }
    }

    // 上一个
    public void preWord(View view) {
        if(cursor.moveToPrevious()){
            TheWord = cursor.getString(0);
            TheChange = cursor.getString(1);
            Thepronounce = cursor.getString(2);
            TheMusic = cursor.getString(3);
            TheTrans = cursor.getString(4);

            word_see.setText(TheWord);
            change_see.setText(TheChange);
            pronounce_see.setText(Thepronounce);
            trans_see.setText(TheTrans);
            if(TheMusic.length()!=0)
                Button_music.setVisibility(View.VISIBLE);
            else
                Button_music.setVisibility(View.INVISIBLE);
        }
        else {
            Toast toast = Toast.makeText(SeeTheWordActivity.this, "已经是第一个了", Toast.LENGTH_SHORT);
            toast.show();
            cursor.moveToNext();
        }
    }

    // 下一个
    public void nextWord(View view) {
        if(cursor.moveToNext()){
            TheWord = cursor.getString(0);
            TheChange = cursor.getString(1);
            Thepronounce = cursor.getString(2);
            TheMusic = cursor.getString(3);
            TheTrans = cursor.getString(4);

            word_see.setText(TheWord);
            change_see.setText(TheChange);
            pronounce_see.setText(Thepronounce);
            trans_see.setText(TheTrans);
            if(TheMusic.length()!=0)
                Button_music.setVisibility(View.VISIBLE);
            else
                Button_music.setVisibility(View.INVISIBLE);
        }
        else {
            Toast toast = Toast.makeText(SeeTheWordActivity.this, "已经是最后一个了", Toast.LENGTH_SHORT);
            toast.show();
            cursor.moveToPrevious();
        }
    }

    // 修改
    public void update(View view) {
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra("word", word_see.getText().toString());
        startActivity(intent);
    }

    // 删除
    public void delete(View view) {
        new deleteWord().execute();
    }

    // Inner class to get the word
    private class GetTheWord extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... word) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(SeeTheWordActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();
                cursor = db.query("WORDS",
                        new String[] {"Text_word", "Text_change", "Text_pronounces", "Text_music", "Text_trans"},
                        null,
                        null,
                        null, null, null);
                cursor.moveToFirst();
                do {
                    if (cursor.getString(0).equals(TheWord)) {
                        TheChange = cursor.getString(1);
                        Thepronounce = cursor.getString(2);
                        TheMusic = cursor.getString(3);
                        TheTrans = cursor.getString(4);
                        break;
                    }
                }while (cursor.moveToNext());
                return true;
            } catch(SQLException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(SeeTheWordActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }else {
                change_see.setText(TheChange);
                pronounce_see.setText(Thepronounce);
                if(TheMusic.length()!=0)
                    Button_music.setVisibility(View.VISIBLE);
                else
                    Button_music.setVisibility(View.INVISIBLE);
                trans_see.setText(TheTrans);
            }
        }
    }

    // inner class to deleteWord
    class deleteWord extends AsyncTask <Object, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Object[] objects) {
            try {
                db.delete("WORDS",
                        "Text_word = ?",
                        new String[] {TheWord});
                db.delete("TEST",
                        "Text_word = ?",
                        new String[] {TheWord});
                db.delete("FIBONACCI",
                        "Text_word = ?",
                        new String[] {TheWord});
                if(cursor.moveToNext())
                    TheWord = cursor.getString(0);
                else {
                    cursor.moveToPrevious();
                    cursor.moveToPrevious();
                    TheWord = cursor.getString(0);
                }
                cursor.close();
                cursor = db.query("WORDS",
                        new String[] {"Text_word", "Text_change", "Text_pronounces", "Text_music", "Text_trans"},
                        null,
                        null,
                        null, null, null);
                cursor.moveToFirst();
                do {
                    if (cursor.getString(0).equals(TheWord)) {
                        TheChange = cursor.getString(1);
                        Thepronounce = cursor.getString(2);
                        TheMusic = cursor.getString(3);
                        TheTrans = cursor.getString(4);
                        break;
                    }
                }while (cursor.moveToNext());
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute (Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(SeeTheWordActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                word_see.setText(TheWord);
                change_see.setText(TheChange);
                pronounce_see.setText(Thepronounce);
                trans_see.setText(TheTrans);
            }
        }
    }
}
