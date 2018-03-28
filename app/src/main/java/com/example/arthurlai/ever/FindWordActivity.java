package com.example.arthurlai.ever;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import javax.xml.transform.Result;

public class FindWordActivity extends AppCompatActivity {

    private TextView Text_word;
    private TextView Text_change;
    private TextView Text_pronounces;
    private TextView Text_trans;
    private TextView Text_source;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_word);

        Text_word = (TextView) findViewById(R.id.Text_word);
        Text_change = (TextView) findViewById(R.id.Text_change);
        Text_pronounces = (TextView) findViewById(R.id.Text_pronounces);
        Text_trans = (TextView) findViewById(R.id.Text_trans);
        Text_source = (TextView) findViewById(R.id.Text_source);

        dialog = new ProgressDialog(this);
        dialog.setTitle("正在查询");
    }

    public void deleteWord(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_text);
        editText.setText("");
    }

    public void findWord(View view){
        new WordTask().execute();
    }

    // Inner class to findWord
    class WordTask extends AsyncTask {
        private String url = "https://dict.hjenglish.com/jp/jc/";
        private Document doc;
        private String word;
        private String change = "获取失败";              // 变体
        private String pronounces = "获取失败";   // 读音
        private String trans = "获取失败";      // 翻译

        @Override
        protected void onPreExecute() {
            EditText editText = (EditText)findViewById(R.id.edit_text);
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

                if (doc.select(".simple").text().length() != 0)
                    trans = doc.select(".simple").first().text();
                else
                    trans = doc.select(".simple").text();
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
}
