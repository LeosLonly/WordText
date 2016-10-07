package computer.bistu.edu.wordtext;

import computer.bistu.edu.wordtext.Words.Words;

/**
 * Created by Administrator on 2016/10/6.
 */

public interface HttpCallBackListener {

    void onFinish(Words.WordAttribute response);

    void onError(Exception e);
}
