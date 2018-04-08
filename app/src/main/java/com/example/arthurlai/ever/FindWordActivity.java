package com.example.arthurlai.ever;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Calendar;

public class FindWordActivity extends AppCompatActivity {

    private TextView Text_word;
    private TextView Text_change;
    private TextView Text_pronounces;
    private TextView Text_trans;
    private TextView Text_source;
    private TextView Text_InOrNot;
    private Button Text_Button_addOrDelete;
    private ProgressDialog dialog;
    private EditText editText;
    private Integer year;
    private Integer month;
    private Integer date;
    private Calendar today;
    String Text_Word;
    String Text_Change;
    String Text_Pronounces;
    String Text_Trans;
    String Text_Music = "";
    public SQLiteDatabase db;
    public Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_word);

        today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH)+1;
        date = today.get(Calendar.DATE);

        Text_word = (TextView) findViewById(R.id.Text_word);
        Text_change = (TextView) findViewById(R.id.Text_change);
        Text_pronounces = (TextView) findViewById(R.id.Text_pronounces);
        Text_trans = (TextView) findViewById(R.id.Text_trans);
        Text_source = (TextView) findViewById(R.id.Text_source);
        Text_InOrNot = (TextView) findViewById(R.id.Text_InOrNot);
        Text_Button_addOrDelete = (Button) findViewById(R.id.Button_addOrDelete);
        editText = (EditText) findViewById(R.id.edit_text);

        dialog = new ProgressDialog(this);
        dialog.setTitle("正在查询");
    }

    // 删除输入框的单词
    public void deleteWord(View view) {
        editText.setText("");
    }

    // 查询单词
    public void findWord(View view){
        // 需要获取输入框的词和wordlist表的单词对比，看是否已经出现在单词表中
        // 如果有，显示已经存储在生词本中
        // 如果没有，显示没有存储在生词本中，并上网查找
        // 如果单词已经在单词表中，按钮显示“-”
        // 如果单词不在单词表中，按钮显示“+”
        // 下面这个判断是用来筛掉输入框为空或输入首字符为空格的情况
        if ((editText.getText().toString().length() != 0) &&
                (editText.getText().toString().charAt(0) != ' '))
            new checkWord().execute(editText.getText().toString());
    }

    // 添加单词进单词本中
    public void addOrDelete(View view) {
        // 如果单词在表中，删除单词，并更新显示
        // 如果单词不在表中，增加单词，并更新显示
        // 如果单词没有资料，跳到手动添加的页面
        if ((Text_word.getText().toString().length() != 0) && (Text_Button_addOrDelete.getText().toString().equals("—"))){
            new deleteWord().execute();
        }
        if ((Text_word.getText().toString().length() != 0) && (Text_Button_addOrDelete.getText().toString().equals("+"))){
            if (Text_trans.getText().toString().equals("无资料显示")) {
                Intent intent = new Intent(this, AddWordByHandActivity.class);
                intent.putExtra("word", Text_word.getText().toString());
                startActivity(intent);
            } else {
                new addWord().execute();
            }
        }
    }

    // 播放读音
    public void music(View view) {
        String urlPath = Text_Music;
        if (Text_Music.length()!=0) {
            MediaPlayer mPlayer = MediaPlayer.create( this, Uri.parse(urlPath) );
            mPlayer.start();
        }
    }

    // Inner class to findWord
    class WordTask extends AsyncTask {
        private String url = "https://dict.hjenglish.com/fr/";
        private Document doc;
        private String word;
        private String change = "";              // 变体
        private String pronounces = "";   // 读音
        private String trans = "";      // 翻译

        @Override
        protected void onPreExecute() {
            word = editText.getText().toString();
            url = url+ word;
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                doc = Jsoup.connect(url).timeout(3000).get();
                if (doc.select(".redirection").text().length() != 0)
                    change = doc.select(".redirection").first().text();
                else
                    change = doc.select(".redirection").text();

                if (doc.select(".pronounces").text().length() != 0)
                    pronounces = doc.select(".pronounces").first().text();
                else
                    pronounces = doc.select(".pronounces").text();

                if (doc.select(".word-audio").attr("data-src").length() != 0)
                    Text_Music = doc.select(".word-audio").attr("data-src");
                else
                    Text_Music = "";

                if (doc.select(".word-details-item-content").text().length() != 0)
                    trans = doc.select(".word-details-item-content").first().text();
                else
                    trans = doc.select(".word-details-item-content").text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(Object o) {
            Text_word.setText(word);
            Text_change.setText(change);
            if (pronounces.length() !=0) Text_pronounces.setText(pronounces);
            else Text_pronounces.setText("无资料显示");
            if (trans.length() !=0) Text_trans.setText(trans);
            else Text_trans.setText("无资料显示");
            Text_source.setText("资料来源：沪江小D");
            dialog.dismiss();
            super.onPostExecute(o);
        }
    }

    // inner class to checkWord（and then findWord）
    class checkWord extends AsyncTask<String, Void, Integer> {
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(String... editText) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(FindWordActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();
                cursor = db.query("WORDS",
                        new String[] {"Text_word", "Text_change", "Text_pronounces", "Text_music", "Text_trans"},
                        "Text_word = ?",
                        new String[] {editText[0]},
                        null, null, null);
                if (cursor.moveToFirst()) {
                    Text_Word = cursor.getString(0);
                    Text_Change = cursor.getString(1);
                    Text_Pronounces = cursor.getString(2);
                    Text_Music = cursor.getString(3);
                    Text_Trans = cursor.getString(4);
                    cursor.close();
                    db.close();
                    return 1;
                }
                else{
                    cursor.close();
                    db.close();
                    return 0;
                }
            }catch (SQLiteException e){
                return 2;
            }
        }

        protected void onPostExecute(Integer success){
            if (success == 2) {
                Toast toast = Toast.makeText(FindWordActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (success == 1) {
                Text_InOrNot.setText("已加入生词本");
                Text_InOrNot.setTextColor(Color.RED);
                Text_word.setText(Text_Word);
                Text_change.setText(Text_Change);
                Text_pronounces.setText(Text_Pronounces);
                Text_trans.setText(Text_Trans);
                Text_source.setText("资料来源：本地生词本");
                Text_Button_addOrDelete.setText("—");
            }
            else {
                // 如果没有，显示没有存储在生词本中，并上网查找
                Text_InOrNot.setText("还没有加入生词本");
                Text_InOrNot.setTextColor(Color.DKGRAY);
                Text_Button_addOrDelete.setText("+");
                new WordTask().execute();
            }
        }
    }

    // inner class to addword
    class addWord extends AsyncTask <Object, Void, Boolean>{
        public SQLiteDatabase db;

        @Override
        protected Boolean doInBackground(Object[] objects) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(FindWordActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();
                ContentValues wordValues = new ContentValues();
                wordValues.put("Text_Word", Text_word.getText().toString());
                wordValues.put("Text_change", Text_change.getText().toString());
                wordValues.put("Text_pronounces", Text_pronounces.getText().toString());
                wordValues.put("Text_music", Text_Music);
                wordValues.put("Text_trans", Text_trans.getText().toString());
                db.insert("WORDS", null, wordValues);

                // 添加test标志
                ContentValues testValues = new ContentValues();
                testValues.put("Text_word", Text_word.getText().toString());
                testValues.put("test", "");
                db.insert("TEST",null, testValues);

                // 添加进生词本时加进斐波那契学习进度中
                ContentValues FibonacciValues = new ContentValues();
                FibonacciValues.put("Text_word", Text_word.getText().toString());
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
                Toast toast = Toast.makeText(FindWordActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                Text_InOrNot.setText("已加入生词本");
                Text_InOrNot.setTextColor(Color.RED);
                Text_Button_addOrDelete.setText("—");
            }
        }
    }

    // inner class to deleteWord
    class deleteWord extends AsyncTask <Object, Void, Boolean>{
        public SQLiteDatabase db;

        @Override
        protected Boolean doInBackground(Object[] objects) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(FindWordActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();
                db.delete("WORDS",
                        "Text_word = ?",
                        new String[] {Text_word.getText().toString()});
                db.delete("TEST",
                        "Text_word = ?",
                        new String[] {Text_word.getText().toString()});
                db.delete("FIBONACCI",
                        "Text_word = ?",
                        new String[] {Text_word.getText().toString()});
                db.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute (Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(FindWordActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                Text_InOrNot.setText("还没有加入生词本");
                Text_InOrNot.setTextColor(Color.DKGRAY);
                Text_Button_addOrDelete.setText("+");
            }
        }
    }

}
