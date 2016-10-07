package computer.bistu.edu.accesswordsapp;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myTag";
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resolver = getContentResolver();

        Button allInfo = (Button) findViewById(R.id.all_info);
        Button addInfo = (Button) findViewById(R.id.add_info);
        Button deleteInfo = (Button) findViewById(R.id.delete_info);
        Button updateInfo = (Button) findViewById(R.id.update_info);
        Button selectInfo = (Button) findViewById(R.id.selecte_info);
        Button deleteAllInfo = (Button) findViewById(R.id.delete_all_info);

        deleteInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog();
            }
        });

        deleteAllInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resolver.delete(Words.Word.CONTENT_URI, null, null);
            }
        });

        allInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = resolver.query(Words.Word.CONTENT_URI,
                        new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD,
                                Words.Word.COLUMN_NAME_MEANING, Words.Word.COLUMN_NAME_SAMPLE},
                        null, null, null);

                if (cursor == null) {
                    Toast.makeText(MainActivity.this, "没有找到记录", Toast.LENGTH_LONG).show();
                    return;
                }


                String msg = "";
                if (cursor.moveToFirst()) {
                    do {
                        msg += "ID:" + cursor.getString(cursor.getColumnIndex(Words.Word._ID)) + ",";
                        msg += "单词：" + cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)) + ",";
                        msg += "含义：" + cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)) + ",";
                        msg += "示例" + cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE)) + "\n";
                    } while (cursor.moveToNext());
                }

                Log.v(TAG, msg);
            }
        });

        addInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDialog();
            }
        });

        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog();
            }
        });

        selectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDialog();
            }
        });
    }

    private void insertDialog() {
        final LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.insert_main, null);
        new AlertDialog.Builder(this)
                .setTitle("Insert Word")
                .setView(layout)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = ((EditText) layout.findViewById(R.id.et_word)).getText().toString();
                        String strMeaning = ((EditText) layout.findViewById(R.id.et_meaning)).getText().toString();
                        String strSample = ((EditText) layout.findViewById(R.id.et_sample)).getText().toString();
                        ContentValues values = new ContentValues();

                        values.put(Words.Word._ID, GUID.getGUID());
                        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
                        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
                        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);

                        Uri newUri = resolver.insert(Words.Word.CONTENT_URI, values);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }

    private void searchDialog() {
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.search_main, null);
        new AlertDialog.Builder(this)
                .setTitle("Search Word")
                .setView(linearLayout)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String id = ((EditText) linearLayout.findViewById(R.id.et_search)).getText().toString();
                        Uri uri = Uri.parse(Words.Word.CONTENT_URI_STRING + "/" + id);
                        Cursor cursor = resolver.query(uri,
                                new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD, Words.Word.COLUMN_NAME_MEANING, Words.Word.COLUMN_NAME_SAMPLE},
                                null, null, null);
                        if (cursor == null) {
                            Toast.makeText(MainActivity.this, "没有找到记录", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String msg = "";
                        if (cursor.moveToFirst()) {
                            do {
                                msg += "ID:" + cursor.getString(cursor.getColumnIndex(Words.Word._ID)) + ",";
                                msg += "单词：" + cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)) + ",";
                                msg += "含义：" + cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)) + ",";
                                msg += "示例" + cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE)) + "\n";
                            } while (cursor.moveToNext());
                        }

                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, msg);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }

    private void deleteDialog() {
        final LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.search_main, null);
        new AlertDialog.Builder(this)
                .setTitle("Delete Word")
                .setView(layout)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String id = ((EditText) layout.findViewById(R.id.et_search)).getText().toString();
                        Uri uri = Uri.parse(Words.Word.CONTENT_URI_STRING + "/" + id);
                        int result = resolver.delete(uri, null, null);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }

    private void updateDialog() {
        final LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.update_main, null);
        new AlertDialog.Builder(this)
                .setTitle("Update Word")
                .setView(layout)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String id = ((EditText) layout.findViewById(R.id.et_id)).getText().toString();
                        String strWord = ((EditText) layout.findViewById(R.id.et_word)).getText().toString();
                        String strMeaning = ((EditText) layout.findViewById(R.id.et_meaning)).getText().toString();
                        String strSample = ((EditText) layout.findViewById(R.id.et_sample)).getText().toString();
                        ContentValues values = new ContentValues();

                        values.put(Words.Word._ID, GUID.getGUID());
                        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
                        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
                        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);

                        Uri uri = Uri.parse(Words.Word.CONTENT_URI_STRING + "/" + id);
                        int result = resolver.update(uri, values, null, null);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }
}
