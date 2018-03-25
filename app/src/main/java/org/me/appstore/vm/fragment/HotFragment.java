package org.me.appstore.vm.fragment;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.utils.UIUtils;
import org.me.appstore.views.FlowLayout;
import org.me.appstore.vm.DataCache;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by user on 2018/3/25.
 * 排行界面
 */
public class HotFragment extends BaseFragment {
    private List<String> hots;
    private String key;

    public HotFragment(Context context) {
        super(context);
    }

    @Override
    protected void showSuccess() {
        NestedScrollView scrollView = new NestedScrollView(UIUtils.getContext());
        FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());
        for (String item : hots) {
            TextView tv = new TextView(UIUtils.getContext());
            tv.setText(item);
            tv.setPadding(5, 5, 5, 5);
            flowLayout.addView(tv);
        }
        scrollView.addView(flowLayout);
        pager.changeViewTo(scrollView);
    }

    @Override
    protected void loadData() {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("index", 0);

        key = Constants.HOT + ".0";
        String json = DataCache.getDataFromLocal(key);
        if (json != null) {
            parserJson(json);
            pager.runOnUiThread();
        } else {
            LoadNetData(params);
        }
    }

    private void LoadNetData(HashMap<String, Object> params) {
        final Request request = HttpUtils.getRequest(Constants.HOT, params);
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
        hots = MyApplication.getGson().fromJson(jsonString, new TypeToken<List<String>>() {
        }.getType());

        if (hots != null && hots.size() > 0) {
            pager.isNullData = false;
        } else {
            pager.isNullData = true;
        }
    }
}
