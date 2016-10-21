package computer.bistu.edu.wordtext.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import computer.bistu.edu.wordtext.R;
import computer.bistu.edu.wordtext.Words.Words;
import computer.bistu.edu.wordtext.WordsApplication;
import computer.bistu.edu.wordtext.database.WordDatabase;
import computer.bistu.edu.wordtext.fragment.WordFragment;
import computer.bistu.edu.wordtext.fragment.WordItemFragment;
import computer.bistu.edu.wordtext.fragment.WordLogFragment;

public class MainLogActivity extends AppCompatActivity implements WordFragment.OnFragmentInteractionListener,
        WordLogFragment.OnLogListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_log);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WordDatabase wordDatabase = WordDatabase.getDatabase();
        if (wordDatabase != null) {
            wordDatabase.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_log:
                SearchDialog();
                break;
        }
        return true;
    }

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

    private void RefreshWordItemFragment(String txtSearchWord) {
        WordLogFragment wordLogFragment = (WordLogFragment) getFragmentManager().findFragmentById(R.id.words_log_list);
        wordLogFragment.refreshWordsList(txtSearchWord);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onWordItemClick(String id) {
        Intent intent = new Intent(WordsApplication.getInstance(), WordDetailActivity.class);
        intent.putExtra(WordFragment.ARG_ID, id);
        startActivity(intent);
    }

    @Override
    public void onDeleteDialog(String strId) {
        DeleteDialog(strId);
    }

    private void DeleteDialog(final String strId) {
        new AlertDialog.Builder(this).setTitle(R.string.action_delete)
                .setMessage(R.string.action_yes_or_delete)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WordDatabase wordsDB = WordDatabase.getDatabase();
                        wordsDB.DeleteUseSql(strId, 2);

                        //单词已经删除，更新显示列表
                        RefreshWordItemFragment();
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }

    public void RefreshWordItemFragment() {
        WordLogFragment wordLogFragment = (WordLogFragment) getFragmentManager().findFragmentById(R.id.words_log_list);
        wordLogFragment.refreshWordsList();
    }
}
