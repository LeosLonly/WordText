package computer.bistu.edu.wordtext.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import computer.bistu.edu.wordtext.Words.Words;
import computer.bistu.edu.wordtext.WordsApplication;

/**
 * Created by Administrator on 2016/9/11.
 */
public class WordDatabase {
    private static WordDatabaseHelper helper;
    private static WordDatabase instance = new WordDatabase();

    public static WordDatabase getDatabase() {
        return WordDatabase.instance;
    }

    private WordDatabase() {
        if (helper == null) {
            helper = new WordDatabaseHelper(WordsApplication.getInstance());
        }
    }

    public void close() {
        if (helper != null)
            helper.close();
    }

    //获得单个单词的全部信息
    public Words.WordAttribute getSingleWord(String id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "select * from words where _ID=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        if (cursor.moveToNext()) {
            Words.WordAttribute item = new Words.WordAttribute(cursor.getString(cursor.getColumnIndex(Words.Word._ID)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE)));
            return item;
        }
        return null;

    }

    public boolean getWord(String word, int type) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql;
        if (type == 1) {
            sql = "select * from words where word=?";
        } else {
            sql = "select * from words_log where word=?";
        }
        Cursor cursor = db.rawQuery(sql, new String[]{word});
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    //得到全部单词列表
    public ArrayList<Map<String, String>> getAllWords(int type) {
        if (helper == null) {
            return null;
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD
        };

        //排序
        String sortOrder =
                Words.Word.COLUMN_NAME_WORD + " ASC";

        String tableName = "";
        if (type == 1) {
            tableName = Words.Word.TABLE_NAME;
        } else {
            tableName = Words.Word.TABLE_NAME_LOG;
        }

        Cursor c = db.query(
                tableName,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return ConvertCursor2WordList(c);
    }


    //将游标转化为单词列表
    private ArrayList<Map<String, String>> ConvertCursor2WordList(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Words.Word._ID, String.valueOf(cursor.getString(cursor.getColumnIndex(Words.Word._ID))));
            map.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)));
            result.add(map);
        }
        return result;
    }

    //使用Sql语句插入单词
    public void InsertUserSql(String id, String strWord, String strMeaning, String strSample, int type) {
        String sql;
        if (type == 1) {
            sql = "insert into  words(_id,word,meaning,sample) values(?,?,?,?)";
        } else {
            sql = "insert into  words_log(_id,word,meaning,sample) values(?,?,?,?)";
        }

        //Gets the data repository in write mode*/
        /*判断单词是否已经存在*/
        if (!getWord(strWord, type)) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL(sql, new String[]{id, strWord, strMeaning, strSample});
        }
    }


    //使用Sql语句删除单词
    public void DeleteUseSql(String strId, int type) {
        String sql;
        if (type == 1) {
            sql = "delete from words where _id='" + strId + "'";
        } else {
            sql = "delete from words_log where _id='" + strId + "'";
        }

        //Gets the data repository in write mode*/
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL(sql);
    }


    //使用Sql语句更新单词
    public void UpdateUseSql(String strId, String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "update words set word=?,meaning=?,sample=? where _id=?";
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample, strId});
    }


    //使用Sql语句查找
    public ArrayList<Map<String, String>> SearchUseSql(String strWordSearch, int type) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql;
        if (type == 1) {
            sql = "select * from words where word like ? order by word asc";
        } else {
            sql = "select * from words_log where word like ? order by word asc";
        }
        Cursor c = db.rawQuery(sql, new String[]{"%" + strWordSearch + "%"});

        return ConvertCursor2WordList(c);
    }
}
