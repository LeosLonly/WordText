package computer.bistu.edu.wordtext.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import computer.bistu.edu.wordtext.R;
import computer.bistu.edu.wordtext.Words.Words;
import computer.bistu.edu.wordtext.database.WordDatabase;

/**
 * Created by Administrator on 2016/10/21.
 */

public class WordLogFragment extends ListFragment {

    private WordLogFragment.OnLogListFragmentInteractionListener mListener;

    public WordLogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshWordsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        mListener = (WordLogFragment.OnLogListFragmentInteractionListener) getActivity();
        TextView textId;

        AdapterView.AdapterContextMenuInfo info;
        View itemView;

        switch (item.getItemId()) {
            case R.id.action_delete_log:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                itemView = info.targetView;
                textId = (TextView) itemView.findViewById(R.id.textId);
                if (textId != null) {
                    String strId = textId.getText().toString();
                    mListener.onDeleteDialog(strId);
                }
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.wordsloglist, menu);
    }

    public void refreshWordsList() {
        WordDatabase wordsDB = WordDatabase.getDatabase();
        if (wordsDB != null) {
            ArrayList<Map<String, String>> items = wordsDB.getAllWords(2);

            SimpleAdapter adapter = new SimpleAdapter(getActivity(), items, R.layout.fragment_worditem,
                    new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD},
                    new int[]{R.id.textId, R.id.textViewWord});

            setListAdapter(adapter);
        }
    }

    public void refreshWordsList(String txtWord) {
        WordDatabase wordsDB = WordDatabase.getDatabase();
        if (wordsDB != null) {
            ArrayList<Map<String, String>> items = wordsDB.SearchUseSql(txtWord, 2);

            SimpleAdapter adapter = new SimpleAdapter(getActivity(), items, R.layout.fragment_worditem,
                    new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD},
                    new int[]{R.id.textId, R.id.textViewWord});

            setListAdapter(adapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener = (WordLogFragment.OnLogListFragmentInteractionListener) getActivity();
        if (null != mListener) {
            TextView textView = (TextView) v.findViewById(R.id.textId);
            if (textView != null) {
                mListener.onWordItemClick(textView.getText().toString());
            }
        }
    }


    public interface OnLogListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onWordItemClick(String id);

        void onDeleteDialog(String strId);
    }
}
