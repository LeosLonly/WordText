package computer.bistu.edu.wordtext.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import computer.bistu.edu.wordtext.Words.Words;

/**
 * Created by Administrator on 2016/9/11.
 */
public class WordDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "myTag";
    private final static String DATABASE_NAME = "wordsdb";//数据库名字
    private final static int DATABASE_VERSION = 1;//数据库版本

    private final static String SQL_CREATE_DATABASE = "CREATE TABLE " + Words.Word.TABLE_NAME + " (" +
            Words.Word._ID + " VARCHAR(32) PRIMARY KEY NOT NULL," +
            Words.Word.COLUMN_NAME_WORD + " TEXT UNIQUE NOT NULL," +
            Words.Word.COLUMN_NAME_MEANING + " TEXT,"
            + Words.Word.COLUMN_NAME_SAMPLE + " TEXT)";

    private final static String SQL_CREATE_DATABASE_LOG = "CREATE TABLE " + Words.Word.TABLE_NAME_LOG + " (" +
            Words.Word._ID + " VARCHAR(32) PRIMARY KEY NOT NULL," +
            Words.Word.COLUMN_NAME_WORD + " TEXT UNIQUE NOT NULL," +
            Words.Word.COLUMN_NAME_MEANING + " TEXT,"
            + Words.Word.COLUMN_NAME_SAMPLE + " TEXT)";


    //删表SQL
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + Words.Word.TABLE_NAME;
    private final static String SQL_DELETE_DATABASE_LOG = "DROP TABLE IF EXISTS " + Words.Word.TABLE_NAME_LOG;

    public WordDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
        Log.e(TAG, SQL_CREATE_DATABASE_LOG);
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE_LOG);
        onCreate(sqLiteDatabase);
    }
}
