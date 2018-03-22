package org.me.appstore.vm.fragment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.vm.DataCache;
import org.me.appstore.vm.RecyclerViewFactory;
import org.me.appstore.vm.adapter.AppAdapter;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 应用
 */
public class AppFragment extends BaseFragment {
    // 展示 成功界面
    private RecyclerView recyclerView;
    private List<AppInfo> apps;
    private String key;

    public AppFragment(Context context) {
        super(context);
    }

    protected void showSuccess() {
        recyclerView = RecyclerViewFactory.createVertical();
        recyclerView.setAdapter(new AppAdapter(apps));
        pager.changeViewTo(recyclerView);
    }

    protected void loadData() {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("index", 0);

        key = Constants.APP + ".0";
        String json = DataCache.getDataFromLocal(key);
        if (json != null) {
            parserJson(json);
            pager.runOnUiThread();
        } else {
            LoadNetData(params);
        }
    }

    private void LoadNetData(HashMap<String, Object> params) {
        final Request request = HttpUtils.getRequest(Constants.APP, params);
        Call call = HttpUtils.getClient().newCall(request);
        call.enqueue(new BaseCallBack(pager) {
            @Override
            protected void onSuccess(String jsonString) {
                // 缓存数据到内存
                MyApplication.getDataCache().put(key, jsonString);
                // 缓存数据到文件
                DataCache.cacheFile(key, jsonString);
                parserJson(jsonString);
            }
        });
    }

    private void parserJson(String jsonString) {
        pager.isReadData = true;
        apps = MyApplication.getGson().fromJson(jsonString, new TypeToken<List<AppInfo>>() {
        }.getType());

        if (apps != null && apps.size() > 0) {
            pager.isNullData = false;
        } else {
            pager.isNullData = true;
        }
    }
}