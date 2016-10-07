package computer.bistu.edu.wordtext;

import android.app.Application;

/**
 * Created by Administrator on 2016/9/11.
 */
public class WordsApplication extends Application {

    private static WordsApplication instance;

    public static WordsApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
