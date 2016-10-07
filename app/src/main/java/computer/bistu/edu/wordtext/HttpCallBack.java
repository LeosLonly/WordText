package computer.bistu.edu.wordtext;

import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import computer.bistu.edu.wordtext.Words.Words;

/**
 * Created by Administrator on 2016/10/6.
 */

public class HttpCallBack {

    /*通过url向有道获取网络上词典内容*/
    public static void responseData(final String url, final HttpCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Words.WordAttribute wordAttribute = new Words.WordAttribute();
                try {
                    URL connectURL = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) connectURL.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    /**
                     * 在这里添加处理json的各种方法
                     */
                    JSONArray array = new JSONArray("[" + builder + "]");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        if (jsonObject != null) {
                            wordAttribute.word = jsonObject.getString("query");
                            if (jsonObject.has("basic")) {
                                JSONObject basic = jsonObject.getJSONObject("basic");
                                String meaning = "";
                                if (basic.has("explains")) {
                                    JSONArray exp = basic.getJSONArray("explains");
                                    for (int j = 0; j < exp.length(); j++) {
                                        meaning += exp.getString(j) + "\n";
                                    }
                                    wordAttribute.meaning = meaning;
                                }
                            }
                            if (jsonObject.has("web")) {
                                JSONArray web = jsonObject.getJSONArray("web");
                                int count = 0;
                                String sample = "";
                                while (!web.isNull(count)) {
                                    if (web.getJSONObject(count).has("key")) {
                                        String key = web.getJSONObject(count).getString("key");
                                        sample += "\n（" + (count + 1) + "）" + key + "\n";
                                    }
                                    if (web.getJSONObject(count).has("value")) {
                                        JSONArray value = web.getJSONObject(count).getJSONArray("value");
                                        for (int j = 0; j < value.length() - 1; j++) {
                                            sample += value.get(j) + ",";
                                        }
                                        sample += value.get(value.length() - 1) + "\n";
                                    }
                                    count++;
                                }
                                wordAttribute.sample = sample;
                            }
                        }
                    }

                    if (listener != null) {
                        listener.onFinish(wordAttribute);
                    }
                } catch (IOException e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } catch (JSONException e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }
}
