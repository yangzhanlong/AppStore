package org.me.appstore.vm.fragment;

import android.util.Log;

import com.google.gson.Gson;

import org.me.appstore.vm.CommonPager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by user on 2018/3/17.
 * 请求回调
 */

public abstract class BaseCallBack implements okhttp3.Callback {
    private CommonPager pager;

    public BaseCallBack(CommonPager pager) {
        this.pager = pager;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        pager.isReadData = false;
        // 更新界面
        pager.runOnUiThread();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        pager.isReadData = true;
        // 判断：状态码 200
        // 判断: 服务器返回的信息内容
        if (response.code() == 200) {
            pager.isReadData = true;
            String jsonString = response.body().string();
            Log.i("onResponse : ", jsonString);
            Gson gson = new Gson();
            
            onSuccess(gson, jsonString);
        } else {
            pager.isReadData = false;
        }

        // 更新界面
        pager.runOnUiThread();
    }

    protected abstract void onSuccess(Gson gson, String jsonString);

}
