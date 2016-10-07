package computer.bistu.edu.wordtext.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import computer.bistu.edu.wordtext.R;
import computer.bistu.edu.wordtext.fragment.WordFragment;

public class WordDetailActivity extends AppCompatActivity implements WordFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);


        if (savedInstanceState == null) {
            WordFragment detailFragment = new WordFragment();
            detailFragment.setArguments(getIntent().getExtras());
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, detailFragment)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
