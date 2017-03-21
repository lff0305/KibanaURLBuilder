package org.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuff on 2016/3/15 11:00
 */
public class JSONObjectBuilder {
    public static <T extends FromJSON> T buildObject(JSONObject obj, Class<T> clz) {
        T t = null;
        try {
            t = clz.newInstance();
            return (T)t.fromJSON(obj);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static<T extends FromJSON> List<T> buildList(JSONArray array, Class<T> clz) {
        if (array == null) {
            return new ArrayList<T>();
        }
        List<T> result = new ArrayList<T>();
        for (int i=0; i<array.length(); i++) {
            JSONObject o = array.optJSONObject(i);
            if (o == null) {
                continue;
            }
            T t = buildObject(o, clz);
            result.add(t);
        }
        return result;
    }
}
