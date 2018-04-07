package com.example.arthurlai.ever;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Arthur.Lai on 2018/3/29.
 */


class EverDatabaseHelper extends SQLiteOpenHelper {
    Calendar c = Calendar.getInstance();
    private static final String DB_NAME = "Ever";   // the name of our database
    private static final int DB_VERSION = 1;            //  the version of database

    EverDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建了一个WORDS表
        db.execSQL("CREATE TABLE WORDS ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Text_word TEXT,"
                + "Text_change TEXT,"
                + "Text_pronounces TEXT,"
                + "Text_music, TEXT,"
                + "Text_trans TEXT);"
        );

        // 创建了一个TEST表
        db.execSQL("CREATE TABLE TEST ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Text_word TEXT ,"
                + "Test TEXT,"
                + "FOREIGN KEY(Text_word)REFERENCES WORDS(Text_word));"
        );

        // 创建了一个FIBONACCI表
        db.execSQL("CREATE TABLE FIBONACCI ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Text_word TEXT ,"
                + "Test TEXT,"
                + "year INTEGER,"
                + "month INTEGER,"
                + "date INTEGER,"
                + "pre INTEGER,"
                + "R INTEGER,"
                + "FOREIGN KEY(Text_word)REFERENCES WORDS(Text_word));");

        // 创建了一个WORDDATE表
        db.execSQL("CREATE TABLE WORDDATE ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Text_word TEXT,"
                + "year INTEGER,"
                + "month INTEGER,"
                + "date INTEGER,"
                + "FOREIGN KEY(Text_word)REFERENCES WORDS(Text_word));");

        // 输入例子
        insertWord(db, "Je t'aime", null, "[ʒətεm] ",
                "http://tts.hjapi.com/fr/AD87C85A7AB36C35ADE7C420D269B877",
                "loc. 我爱你");
        insertWord(db, "曾经", null, "[céngjīng] ",
                "http://tts.hjapi.com/py/F72309CBA170FDF9?phoneticsymbol=%E6%9B%BE%E7%BB%8F",
                "详细释义\n" +
                        "adv.\n" +
                        "une fois;autrefois\n" +
                        "\n" +
                        "他们曾经是好友,现在不是了.\n" +
                        "\n" +
                        "Ils ont été très amis,mais ils ne le sont plus maintenant. ");
        insertTEST(db, "Je t'aime", null);
        insertTEST(db, "曾经", null);
        insertFIBONACCI(db, "Je t'aime", c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1, c.get(Calendar.DATE),1, 0);
        insertFIBONACCI(db, "曾经",  c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1, c.get(Calendar.DATE),1, 0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 插入WORDS
    private static void insertWord(SQLiteDatabase db, String Text_word, String Text_change,
                                   String Text_pronounces, String Text_music, String Text_trans){
        ContentValues wordValues = new ContentValues();
        wordValues.put("Text_Word", Text_word);
        wordValues.put("Text_change", Text_change);
        wordValues.put("Text_pronounces", Text_pronounces);
        wordValues.put("Text_music", Text_music);
        wordValues.put("Text_trans", Text_trans);
        db.insert("WORDS", null, wordValues);
    }

    // 插入TEST
    private static void insertTEST(SQLiteDatabase db, String Text_word, String TOF){
        ContentValues TestValues = new ContentValues();
        TestValues.put("Text_word", Text_word);
        TestValues.put("test", TOF);
        db.insert("TEST", null, TestValues);
    }

    // 插入FIBONACCI
    private static void insertFIBONACCI(SQLiteDatabase db, String Text_word, Integer year, Integer month, Integer date, Integer pre, Integer R) {
        ContentValues FibonacciValues = new ContentValues();
        FibonacciValues.put("Text_word", Text_word);
        FibonacciValues.put("year", year);
        FibonacciValues.put("month",month);
        FibonacciValues.put("date", date);
        FibonacciValues.put("pre", pre);
        FibonacciValues.put("R", R);
        db.insert("FIBONACCI", null, FibonacciValues);
    }

}