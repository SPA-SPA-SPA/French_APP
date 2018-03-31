package com.example.arthurlai.ever;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by Arthur.Lai on 2018/3/29.
 */


class EverDatabaseHelper extends SQLiteOpenHelper {
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
                + "Text_trans TEXT);"
        );
        // 输入例子
        insertWord(db, "ありがとう", null, "[ありがとう] [arigatou] ②",
                "【感叹词】 1.谢谢。");
        insertWord(db, "かつて", null, "[かつて] [katsute] ①",
                "【副词】1.曾经，以前。2.（后接否定）从来（没有）…，至今（未曾）…");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 插入
    private static void insertWord(SQLiteDatabase db, String Text_word, String Text_change,
                                   String Text_pronounces, String Text_trans){
        ContentValues wordValues = new ContentValues();
        wordValues.put("Text_Word", Text_word);
        wordValues.put("Text_change", Text_change);
        wordValues.put("Text_pronounces", Text_pronounces);
        wordValues.put("Text_trans", Text_trans);
        db.insert("WORDS", null, wordValues);
    }
}