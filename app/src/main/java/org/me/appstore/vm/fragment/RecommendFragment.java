package org.me.appstore.vm.fragment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.utils.UIUtils;
import org.me.appstore.views.StellarMap;
import org.me.appstore.vm.DataCache;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by user on 2018/3/25.
 * 推荐界面
 */
public class RecommendFragment extends BaseFragment {
    private List<String> recommends;
    private String key;

    public RecommendFragment(Context context) {
        super(context);
    }

    @Override
    protected void showSuccess() {
        StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        StellarMap.Adapter adapter = new StellarMap.Adapter() {
            @Override
            public int getCount() {
                return recommends.size();
            }

            @Override
            protected View getView(int index, View convertView) {
                TextView textView = new TextView(UIUtils.getContext());
                textView.setText(recommends.get(index));
                return textView;
            }
        };
        stellarMap.setAdapter(adapter);
        pager.changeViewTo(stellarMap);
    }

    @Override
    protected void loadData() {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("index", 0);

        key = Constants.RECOMMEND + ".0";
        String json = DataCache.getDataFromLocal(key);
        if (json != null) {
            parserJson(json);
            pager.runOnUiThread();
        } else {
            LoadNetData(params);
        }
    }

    private void LoadNetData(HashMap<String, Object> params) {
        final Request request = HttpUtils.getRequest(Constants.RECOMMEND, params);
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
        recommends = MyApplication.getGson().fromJson(jsonString, new TypeToken<List<String>>() {
        }.getType());

        if (recommends != null && recommends.size() > 0) {
            pager.isNullData = false;
        } else {
            pager.isNullData = true;
        }
    }
}
