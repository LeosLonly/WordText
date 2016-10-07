package computer.bistu.edu.wordtext.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import computer.bistu.edu.wordtext.R;
import computer.bistu.edu.wordtext.Words.Words;
import computer.bistu.edu.wordtext.database.WordDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordFragment extends Fragment {
    private static final String TAG = "myTag";
    public static final String ARG_ID = "id";

    private String mID;//单词主键
    private OnFragmentInteractionListener mListener;

    public WordFragment() {
        // Required empty public constructor
    }

    public static WordFragment newInstance(String wordID) {
        WordFragment fragment = new WordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, wordID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mID = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_word_detail, container, false);

        WordDatabase wordsDB = WordDatabase.getDatabase();

        if (wordsDB != null && mID != null) {
            TextView textViewWord = (TextView) view.findViewById(R.id.word);
            TextView textViewWordMeaning = (TextView) view.findViewById(R.id.wordmeaning);
            TextView textViewWordSample = (TextView) view.findViewById(R.id.wordsample);

            Words.WordAttribute item = wordsDB.getSingleWord(mID);
            if (item != null) {
                textViewWord.setText(item.word);
                textViewWordMeaning.setText(item.meaning);
                textViewWordSample.setText(item.sample);
            } else {
                textViewWord.setText("");
                textViewWordMeaning.setText("");
                textViewWordSample.setText("");
            }

        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
