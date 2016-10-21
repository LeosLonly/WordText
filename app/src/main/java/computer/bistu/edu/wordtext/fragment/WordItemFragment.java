package computer.bistu.edu.wordtext.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import computer.bistu.edu.wordtext.HttpCallBack;
import computer.bistu.edu.wordtext.HttpCallBackListener;
import computer.bistu.edu.wordtext.R;
import computer.bistu.edu.wordtext.Words.Words;
import computer.bistu.edu.wordtext.database.GUID;
import computer.bistu.edu.wordtext.database.WordDatabase;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class WordItemFragment extends ListFragment {

    private static final String TAG = "myTag";
    private OnListFragmentInteractionListener mListener;

    private final String basicUrl = "http://fanyi.youdao.com/openapi.do?";
    private final String keyfrom = "WordText";
    private final String key = "93015927";
    private final String doctype = "json";
    private final String Type = "data";
    private final String version = "1.1";
    private String url;
    private Words.WordAttribute wordAttribute = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public WordItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WordItemFragment newInstance() {
        WordItemFragment fragment = new WordItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshWordsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView listView = getListView();
        registerForContextMenu(listView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //更新单词列表，从数据库中找到所有单词，然后在列表中显示出来
    public void refreshWordsList() {
        WordDatabase wordsDB = WordDatabase.getDatabase();
        if (wordsDB != null) {
            ArrayList<Map<String, String>> items = wordsDB.getAllWords(1);

            SimpleAdapter adapter = new SimpleAdapter(getActivity(), items, R.layout.fragment_worditem,
                    new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD},
                    new int[]{R.id.textId, R.id.textViewWord});

            setListAdapter(adapter);
        }
    }

    //更新单词列表，从数据库中找到同strWord向匹配的单词，然后在列表中显示出来
    public void refreshWordsList(final String strWord) {
        final WordDatabase wordsDB = WordDatabase.getDatabase();
        if (wordsDB != null) {

            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    setListAdapter((ListAdapter) msg.obj);
                }
            };

            final ArrayList<Map<String, String>> items = wordsDB.SearchUseSql(strWord, 1);
            url = basicUrl + "keyfrom=" + keyfrom + "&key=" + key
                    + "&type=" + Type + "&doctype=" + doctype + "&version=" + version + "&q=" + strWord;
            final Map<String, String> map = new HashMap<>();
            String id = GUID.getGUID();
            map.put(Words.Word._ID, id);
            HttpCallBack.responseData(url, new HttpCallBackListener() {
                @Override
                public void onFinish(Words.WordAttribute response) {
                    map.put(Words.Word.COLUMN_NAME_WORD, response.word);
                    if (items.size() <= 0) {
                        items.add(map);
                        wordsDB.InsertUserSql(GUID.getGUID(), response.word, response.meaning, response.sample, 1);
                    }
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), items, R.layout.fragment_worditem,
                            new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD},
                            new int[]{R.id.textId, R.id.textViewWord});
                    Message message = new Message();
                    message.obj = adapter;
                    handler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {

                }
            });

        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        mListener = (OnListFragmentInteractionListener) getActivity();
        TextView textId;

        AdapterView.AdapterContextMenuInfo info;
        View itemView;

        switch (item.getItemId()) {
            case R.id.action_delete:
                //删除单词
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                itemView = info.targetView;
                textId = (TextView) itemView.findViewById(R.id.textId);
                if (textId != null) {
                    String strId = textId.getText().toString();
                    mListener.onDeleteDialog(strId);
                }
                break;
            case R.id.action_update:
                //修改单词
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                itemView = info.targetView;
                textId = (TextView) itemView.findViewById(R.id.textId);

                if (textId != null) {
                    String strId = textId.getText().toString();
                    mListener.onUpdateDialog(strId);
                }
                break;
            case R.id.action_jsb:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                itemView = info.targetView;
                textId = (TextView) itemView.findViewById(R.id.textId);

                if (textId != null) {
                    String strId = textId.getText().toString();
                    mListener.onAddToLog(strId);
                }
                break;
        }
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener = (OnListFragmentInteractionListener) getActivity();
        if (null != mListener) {
            TextView textView = (TextView) v.findViewById(R.id.textId);
            if (textView != null) {
                Log.e("JJ", "LLLL111");
                mListener.onWordItemClick(textView.getText().toString());
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.wordslist, menu);
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onWordItemClick(String id);

        void onDeleteDialog(String strId);

        void onUpdateDialog(String strId);

        void onAddToLog(String strId);

    }
}
