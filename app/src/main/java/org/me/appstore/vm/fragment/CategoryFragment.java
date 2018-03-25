package org.me.appstore.vm.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.module.net.CategoryInfo;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.vm.DataCache;
import org.me.appstore.vm.RecyclerViewFactory;
import org.me.appstore.vm.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by user on 2018/3/25.
 * 分类界面
 */
public class CategoryFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<CategoryInfo> categories;
    private String key;

    public CategoryFragment(Context context) {
        super(context);
    }

    @Override
    protected void showSuccess() {
        recyclerView = RecyclerViewFactory.createVertical();
        recyclerView.setAdapter(new CategoryAdapter(categories));
        pager.changeViewTo(recyclerView);
    }

    @Override
    protected void loadData() {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("index", 0);

        key = Constants.CATEGORY + ".0";
        String json = DataCache.getDataFromLocal(key);
        if (json != null) {
            parserJson(json);
            pager.runOnUiThread();
        } else {
            LoadNetData(params);
        }
    }

    private void LoadNetData(HashMap<String, Object> params) {
        final Request request = HttpUtils.getRequest(Constants.CATEGORY, params);
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
        try {
            JSONArray infoArrays = new JSONArray(jsonString);
            // 遍历获取到的 json 数组
            categories = new ArrayList<>();
            for (int i = 0; i < infoArrays.length(); i++) {
                JSONObject infoObjects = infoArrays.getJSONObject(i);
                String title = infoObjects.get("title").toString();

                CategoryInfo category = new CategoryInfo();
                category.title = title;
                category.isTitle = true;
                categories.add(category);

                JSONArray infos = infoObjects.getJSONArray("infos");
                for (int j = 0; j < infos.length(); j++) {
                    JSONObject infoObject = infos.getJSONObject(j);
                    String url1 = infoObject.getString("url1");
                    String url2 = infoObject.getString("url2");
                    String url3 = infoObject.getString("url3");
                    String name1 = infoObject.getString("name1");
                    String name2 = infoObject.getString("name2");
                    String name3 = infoObject.getString("name3");

                    CategoryInfo category2 = new CategoryInfo();
                    category2.url1 = url1;
                    category2.url2 = url2;
                    category2.url3 = url3;
                    category2.name1 = name1;
                    category2.name2 = name2;
                    category2.name3 = name3;
                    category2.isTitle = false;
                    categories.add(category2);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            pager.isReadData = false; // 解析数据失败
        }

        if (categories != null && categories.size() > 0) {
            pager.isNullData = false;
        } else {
            pager.isNullData = true;
        }
    }
}