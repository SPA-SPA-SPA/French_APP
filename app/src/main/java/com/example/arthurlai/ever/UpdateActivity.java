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
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    private TextView word_update;
    private EditText change_update;
    private EditText pronounces_update;
    private EditText trans_update;
    private String change;              // 变体
    private String pronounces;   // 读音
    private String trans;      // 翻译
    private String messageText;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        word_update = (TextView) findViewById(R.id.word_canNotChange);
        change_update = (EditText) findViewById(R.id.change_byhand);
        pronounces_update = (EditText) findViewById(R.id.pronounces_byhand);
        trans_update = (EditText) findViewById(R.id.trans_byhand);
        messageText = getIntent().getStringExtra("word");
        word_update.setText(messageText);
        new GetWhatIWant().execute(messageText);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

    // 清空
    public void DeleteAllText (View view) {
        change_update.setText("");
        pronounces_update.setText("");
        trans_update.setText("\n");
    }

    // 提交修改
    public void Update (View view) {
        if (trans_update.getText().toString().length() != 0){
            ContentValues Values = new ContentValues();
            Values.put("Text_change", change_update.getText().toString());
            Values.put("Text_pronounces", pronounces_update.getText().toString());
            Values.put("Text_trans", trans_update.getText().toString());
            db.update("WORDS",
                    Values,
                    "Text_word = ?",
                    new String[] {messageText});
            finish();
        }
    }

    //Inner class to get everything about word
    class GetWhatIWant extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... word) {
            try {
                SQLiteOpenHelper EverDatabaseHelper = new EverDatabaseHelper(UpdateActivity.this);
                db = EverDatabaseHelper.getReadableDatabase();
                cursor = db.query("WORDS",
                        new String[] {"Text_word", "Text_change", "Text_pronounces", "Text_trans"},
                        "Text_word = ?",
                        new String[] {word[0]},
                        null, null, null);
                return true;
            }catch (SQLiteException e){
                return false;
            }
        }

        protected void onPostExecute(Boolean success){
            if (!success) {
                Toast toast = Toast.makeText(UpdateActivity.this, "EverDataBase unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                    cursor.moveToFirst();
                    change = cursor.getString(1);
                    pronounces = cursor.getString(2);
                    trans = cursor.getString(3);

                    change_update.setText(change);
                    pronounces_update.setText(pronounces);
                    trans_update.setText(trans+"\n");
            }
        }
    }
}
