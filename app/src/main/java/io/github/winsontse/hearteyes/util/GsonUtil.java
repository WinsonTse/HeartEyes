package io.github.winsontse.hearteyes.util;

import com.google.gson.Gson;

/**
 * Created by winson on 16/5/31.
 */
public class GsonUtil {
    private static volatile GsonUtil instance;
    private Gson gson;

    private GsonUtil() {
        gson = new Gson();
    }

    public static GsonUtil getInstance() {
        if (instance == null) {
            synchronized (GsonUtil.class) {
                if (instance == null) {
                    instance = new GsonUtil();
                }
            }
        }
        return instance;
    }

    public <T> String toJson(T t) {
        return gson.toJson(t);
    }

    public <T> T fromJson(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }
}
