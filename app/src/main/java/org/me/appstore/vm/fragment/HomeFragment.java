package org.me.appstore.vm.fragment;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;

import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.module.net.HomeInfo;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.vm.DataCache;
import org.me.appstore.vm.RecyclerViewFactory;
import org.me.appstore.vm.adapter.HomeAdapter;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {
    // 展示 成功界面
    private RecyclerView recyclerView;
    private HomeInfo homeInfo;
    private String key;

    public HomeFragment(Context context) {
        super(context);
    }

    // 加载成功界面
    protected void showSuccess() {
        recyclerView = RecyclerViewFactory.createVertical();
        recyclerView.setAdapter(new HomeAdapter(homeInfo, this.getActivity()));
        pager.changeViewTo(recyclerView);
    }

    // 耗时操作
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void loadData() {
        // 从内存中读取数据:通常的做法是否放入集合中
        // List和Map，优先考虑Map。可以利用Key快速的找到Value信息
        // 由于协议体积比较小，所以我们不考虑删除的动作
        // 由于很多地方都会设计到加载界面，所以我们容器需要放一个公共的环境下

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("index", 0);

        // key: home.0
        key = Constants.HOME + ".0";
        String json = DataCache.getDataFromLocal(key);
        if (json != null) {
            parserJson(json);
            pager.runOnUiThread();
        } else {
            LoadNetData(params);
        }
    }

    /**
     * 获取网络数据
     *
     * @param params
     */
    private void LoadNetData(HashMap<String, Object> params) {
        // 1、创建联网用的客户端
        // 2、创建发送请求（get或post，链接，参数）
        // 3、发送请求
        // 4、结果处理

        // http://localhost:8080/GooglePlayServer/home?index=0
        final Request request = HttpUtils.getRequest(Constants.HOME, params);
        Call call = HttpUtils.getClient().newCall(request);
        // call.execute() 同步
        // 异步

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

    /**
     * 解析json字符串
     *
     * @param json
     */
    private void parserJson(String json) {
        pager.isReadData = true;
        homeInfo = MyApplication.getGson().fromJson(json, HomeInfo.class);
        List<AppInfo> list = homeInfo.list;
        List<String> picture = homeInfo.picture;
        if ((list != null && list.size() > 0) || (picture != null && picture.size() > 0)) {
            pager.isNullData = false;
        } else {
            pager.isNullData = true;
        }
    }
}














