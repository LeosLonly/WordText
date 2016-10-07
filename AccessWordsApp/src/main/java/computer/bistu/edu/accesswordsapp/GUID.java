package computer.bistu.edu.accesswordsapp;

import java.util.UUID;

/**
 * Created by Administrator on 2016/9/22.
 */

public class GUID {

    public static String getGUID() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return uuidStr;
    }
}
