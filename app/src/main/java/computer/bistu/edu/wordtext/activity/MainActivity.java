package computer.bistu.edu.wordtext.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import computer.bistu.edu.wordtext.HttpCallBack;
import computer.bistu.edu.wordtext.HttpCallBackListener;
import computer.bistu.edu.wordtext.R;
import computer.bistu.edu.wordtext.Words.Words;
import computer.bistu.edu.wordtext.WordsApplication;
import computer.bistu.edu.wordtext.database.GUID;
import computer.bistu.edu.wordtext.database.WordDatabase;
import computer.bistu.edu.wordtext.fragment.WordFragment;
import computer.bistu.edu.wordtext.fragment.WordItemFragment;

public class MainActivity extends AppCompatActivity implements WordFragment.OnFragmentInteractionListener,
        WordItemFragment.OnListFragmentInteractionListener {

    private static final String TAG = "myTag";
    private final String basicUrl = "http://fanyi.youdao.com/openapi.do?";
    private final String keyfrom = "WordText";
    private final String key = "93015927";
    private final String doctype = "json";
    private final String Type = "data";
    private final String version = "1.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新增单词
                InsertDialog(1);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WordDatabase wordsDB = WordDatabase.getDatabase();
        if (wordsDB != null)
            wordsDB.close();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:
                //查找
                SearchDialog();
                return true;
            case R.id.action_insert:
                //新增单词
                InsertDialog(1);
                return true;
            case R.id.action_inert1:
                InsertDialog(2);
                return true;
            case R.id.action_log:
                Intent intent = new Intent(MainActivity.this, MainLogActivity.class);
                startActivity(intent);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * update wordlist
     */
    public void RefreshWordItemFragment() {
        WordItemFragment wordItemFragment = (WordItemFragment) getFragmentManager().findFragmentById(R.id.wordslist);
        wordItemFragment.refreshWordsList();
    }

    /**
     * update wordlist
     *
     * @param strWord
     */
    private void RefreshWordItemFragment(String strWord) {
        WordItemFragment wordItemFragment = (WordItemFragment) getFragmentManager().findFragmentById(R.id.wordslist);
        wordItemFragment.refreshWordsList(strWord);
    }

    /**
     * insert word dialog
     *
     * @param j insert type
     */
    private void InsertDialog(final int j) {
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.insert, null);
        final EditText etWord = (EditText) linearLayout.findViewById(R.id.txtWord);
        final EditText etMeaning = (EditText) linearLayout.findViewById(R.id.txtMeaning);
        final EditText etSample = (EditText) linearLayout.findViewById(R.id.txtSample);
        if (j == 2) {
            etMeaning.setEnabled(false);
            etSample.setEnabled(false);
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.action_insert)//标题
                .setView(linearLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = etWord.getText().toString();
                        String strMeaning = "";
                        String strSample = "";
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                Words.WordAttribute wordAttribute = (Words.WordAttribute) msg.obj;
                                WordDatabase wordsDB = WordDatabase.getDatabase();
                                wordsDB.InsertUserSql(GUID.getGUID(), wordAttribute.word, wordAttribute.meaning, wordAttribute.sample, 1);
                                //单词已经插入到数据库，更新显示列表
                                RefreshWordItemFragment();
                            }
                        };

                        if (j == 1) {
                            strMeaning = etMeaning.getText().toString();
                            strSample = etSample.getText().toString();
                            Message message = new Message();
                            Words.WordAttribute wordAttribute = new Words.WordAttribute();
                            wordAttribute.word = strWord;
                            wordAttribute.meaning = strMeaning;
                            wordAttribute.sample = strSample;
                            message.obj = wordAttribute;
                            handler.sendMessage(message);

                        } else {
                            String url = basicUrl + "keyfrom=" + keyfrom + "&key=" + key
                                    + "&type=" + Type + "&doctype=" + doctype + "&version=" + version + "&q=" + strWord;
                            HttpCallBack.responseData(url, new HttpCallBackListener() {
                                @Override
                                public void onFinish(Words.WordAttribute response) {
                                    Message message = new Message();
                                    message.obj = response;
                                    handler.sendMessage(message);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                        }
                    }
                })
                //取消按钮及其动作
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框

    }


    /**
     * delete word data dialog
     *
     * @param strId
     */
    private void DeleteDialog(final String strId) {
        new AlertDialog.Builder(this).setTitle(R.string.action_delete)
                .setMessage(R.string.action_yes_or_delete)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WordDatabase wordsDB = WordDatabase.getDatabase();
                        wordsDB.DeleteUseSql(strId, 1);

                        //单词已经删除，更新显示列表
                        RefreshWordItemFragment();
                    }
                }).setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }


    /**
     * update word data dialog
     *
     * @param strId
     * @param strWord
     * @param strMeaning
     * @param strSample
     */
    private void UpdateDialog(final String strId, final String strWord, final String strMeaning, final String strSample) {
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.insert, null);
        EditText textWord = (EditText) linearLayout.findViewById(R.id.txtWord);
        textWord.setEnabled(false);
        textWord.setText(strWord);
        ((EditText) linearLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
        ((EditText) linearLayout.findViewById(R.id.txtSample)).setText(strSample);
        new AlertDialog.Builder(this)
                .setTitle(R.string.action_update)//标题
                .setView(linearLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewMeaning = ((EditText) linearLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strNewSample = ((EditText) linearLayout.findViewById(R.id.txtSample)).getText().toString();

                        WordDatabase wordsDB = WordDatabase.getDatabase();
                        wordsDB.UpdateUseSql(strId, strWord, strNewMeaning, strNewSample);

                        //单词已经更新，更新显示列表
                        RefreshWordItemFragment();
                    }
                })
                //取消按钮及其动作
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框


    }


    /**
     * search word dialog
     */
    private void SearchDialog() {
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.searchterm, null);
        new AlertDialog.Builder(this)
                .setTitle(R.string.action_search)//标题
                .setView(linearLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtSearchWord = ((EditText) linearLayout.findViewById(R.id.txtSearchWord)).getText().toString();

                        //单词已经插入到数据库，更新显示列表
                        RefreshWordItemFragment(txtSearchWord);
                    }
                })
                //取消按钮及其动作
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框

    }

    /**
     * 当用户在单词列表Fragment中单击某个单词时回调此函数
     * 判断如果横屏的话，则需要在右侧单词详细Fragment中显示
     */
    @Override
    public void onWordItemClick(String id) {

        if (isLand()) {//横屏的话则在右侧的WordDetailFragment中显示单词详细信息
            ChangeWordDetailFragment(id);
        } else {
            Intent intent = new Intent(WordsApplication.getInstance(), WordDetailActivity.class);
            intent.putExtra(WordFragment.ARG_ID, id);
            startActivity(intent);
        }

    }

    //是否是横屏
    private boolean isLand() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        return false;
    }

    private void ChangeWordDetailFragment(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(WordFragment.ARG_ID, id);

        WordFragment fragment = new WordFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction().replace(R.id.worddetail, fragment).commit();
    }

    /**
     * delete word data
     *
     * @param strId
     */
    @Override
    public void onDeleteDialog(String strId) {
        DeleteDialog(strId);
    }

    /**
     * update word data
     *
     * @param strId
     */
    @Override
    public void onUpdateDialog(String strId) {
        WordDatabase wordsDB = WordDatabase.getDatabase();
        if (wordsDB != null && strId != null) {
            Words.WordAttribute item = wordsDB.getSingleWord(strId);
            if (item != null) {
                UpdateDialog(strId, item.word, item.meaning, item.sample);
            }
        }
    }

    @Override
    public void onAddToLog(String strId) {
        WordDatabase wordsDB = WordDatabase.getDatabase();
        if (wordsDB != null && strId != null) {
            Words.WordAttribute item = wordsDB.getSingleWord(strId);
            if (item != null) {
                AddWordLogToDB(strId, item.word, item.meaning, item.sample);
            }
        }
    }

    private void AddWordLogToDB(String strId, String word, String meaning, String sample) {
        WordDatabase wordsDB = WordDatabase.getDatabase();
        wordsDB.InsertUserSql(strId, word, meaning, sample, 2);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
