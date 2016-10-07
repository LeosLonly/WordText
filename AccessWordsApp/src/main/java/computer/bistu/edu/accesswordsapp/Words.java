package computer.bistu.edu.accesswordsapp;

/**
 * Created by Administrator on 2016/9/22.
 */

import android.net.Uri;
import android.provider.BaseColumns;

public class Words {

    public static final String AUTHORITY = "computer.bistu.edu.wordsprovider2";

    public Words() {
    }

    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_NAME = _ID;
        public static final String COLUMN_NAME_WORD = "word";//column:word
        public static final String COLUMN_NAME_MEANING = "meaning";//column:word meaning
        public static final String COLUMN_NAME_SAMPLE = "sample";//colum:word sample

        public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
        public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
        public static final String MINE_ITEM = "vnd.bistu.cs.se.word";

        public static final String MINE_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MINE_ITEM;
        public static final String MINE_TYPE_MULTIPLE = MIME_DIR_PREFIX + "/" + MINE_ITEM;
        public static final String PATH_SINGLE = "word/#";//单条数据的路径
        public static final String PATH_MULTIPLE = "word";//多条数据的路径

        public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
    }
}
