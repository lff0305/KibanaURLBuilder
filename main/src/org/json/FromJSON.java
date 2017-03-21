package org.json;

/**
 * Created by liuff on 2016/3/15 11:01
 */
public interface FromJSON<T> {
    public T fromJSON(JSONObject o);
}
