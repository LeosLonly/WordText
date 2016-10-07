package computer.bistu.edu.wordtext.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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


    /*private static final String SQL_CREATE_DATABASE = "create table "
            + Words.Word.TABLE_NAME
            + "(" + Words.Word._ID + " integer primary key autoincrement,"
            + Words.Word.COLUMN_NAME_WORD + " text" + ","
            + Words.Word.COLUMN_NAME_MEANING + " text" + ","
            + Words.Word.COLUMN_NAME_SAMPLE + " text" + ")";*/

    //删表SQL
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + Words.Word.TABLE_NAME;

    public WordDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }
}
