package org.me.appstore.vm.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.module.net.SubjectInfo;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.vm.DataCache;
import org.me.appstore.vm.RecyclerViewFactory;
import org.me.appstore.vm.adapter.SubjectAdapter;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by user on 2018/3/24.
 * 专题的界面
 */
public class SubjectFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<SubjectInfo> subjects;
    private String key;

    public SubjectFragment(Context context) {
        super(context);
    }

    @Override
    protected void showSuccess() {
        recyclerView = RecyclerViewFactory.createVertical();
        recyclerView.setAdapter(new SubjectAdapter(subjects));
        pager.changeViewTo(recyclerView);
    }

    @Override
    protected void loadData() {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("index", 0);

        key = Constants.SUBJECT + ".0";
        String json = DataCache.getDataFromLocal(key);
        if (json != null) {
            parserJson(json);
            pager.runOnUiThread();
        } else {
            LoadNetData(params);
        }
    }

    private void LoadNetData(HashMap<String, Object> params) {
        final Request request = HttpUtils.getRequest(Constants.SUBJECT, params);
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
        subjects = MyApplication.getGson().fromJson(jsonString, new TypeToken<List<SubjectInfo>>(){}.getType());

        if (subjects != null && subjects.size() > 0) {
            pager.isNullData = false;
        } else {
            pager.isNullData = true;
        }
    }
}
