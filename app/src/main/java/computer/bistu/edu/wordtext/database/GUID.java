package computer.bistu.edu.wordtext.database;

import java.util.UUID;

/**
 * Created by Administrator on 2016/9/11.
 */
public class GUID {

    public static String getGUID() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return uuidStr;
    }
}
