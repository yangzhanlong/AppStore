package org.me.appstore.utils;


import org.me.appstore.Constants;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtils {
    private static OkHttpClient client = new OkHttpClient();

    public static OkHttpClient getClient() {
        return client;
    }

    public static Request getRequest(String path, HashMap<String, Object> params) {
        // http://localhost:8080/GooglePlayServer/home?index=0
        String buffer = Constants.HOST + path +
                HttpUtils.getUrlParamsByMap(params);
        return new Request.Builder().get().url(buffer).build();
    }

    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer("?");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
