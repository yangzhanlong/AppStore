package org.me.appstore.vm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.R;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.utils.LogUtil;
import org.me.appstore.vm.CommonPager;
import org.me.appstore.vm.DataCache;
import org.me.appstore.vm.fragment.BaseCallBack;
import org.me.appstore.vm.holder.BaseHolder;
import org.me.appstore.vm.holder.ItemDetailDesHolder;
import org.me.appstore.vm.holder.ItemDetailImageHolder;
import org.me.appstore.vm.holder.ItemDetailInfoHolder;
import org.me.appstore.vm.holder.ItemDetailSafeHolder;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 详情界面
 */
public class DetailActivity extends AppCompatActivity {

    private CommonPager pager;
    private String packageName;
    private String key;
    private AppInfo appInfos;
    private Toolbar toolbar;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pager = new CommonPager(this) {
            @Override
            public void showSuccess() {
                DetailActivity.this.showSuccess();
            }

            @Override
            public void loadData() {
                DetailActivity.this.loadData();
            }
        };
        setContentView(pager.commonContainer);

        Intent intent = getIntent();
        packageName = intent.getStringExtra("packageName");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Activity 起点
        pager.dynamic();
    }

    private void showSuccess() {
        View view = View.inflate(this, R.layout.activity_detail, null);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 显示返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 添加标题
        toolbar.setTitle(appInfos.name);

        // 设置应用详情数据
        BaseHolder holder;
        holder = new ItemDetailInfoHolder(view.findViewById(R.id.item_detail_info));
        holder.setData(appInfos);


        // 设置安全信息
        holder = new ItemDetailSafeHolder(view.findViewById(R.id.item_detail_safe));
        holder.setData(appInfos.safe);

        // 设置图片信息
        holder = new ItemDetailImageHolder(view.findViewById(R.id.item_detail_pic));
        holder.setData(appInfos.screen);

        // 设置描述信息
        holder = new ItemDetailDesHolder(view.findViewById(R.id.item_detail_des));
        holder.setData(appInfos);
        pager.changeViewTo(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 点击返回箭头
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("packageName", packageName);

        key = Constants.DETAIL + packageName;
        String json = DataCache.getDataFromLocal(key);
        if (json != null) {
            LogUtil.s(json);
            parserJson(json);
            pager.runOnUiThread();
        } else {
            LoadNetData(params);
        }
    }

    private void LoadNetData(HashMap<String, Object> params) {
        LogUtil.s("LoadNetData");
        final Request request = HttpUtils.getRequest(Constants.DETAIL, params);
        Call call = HttpUtils.getClient().newCall(request);
        call.enqueue(new BaseCallBack(pager) {
            @Override
            protected void onSuccess(String jsonString) {
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String str = object.get("des").toString();
                    String str1 = str.replace(" ", "");
                    object.put("des", str1);
                    json = object.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LogUtil.s(json);
                // 缓存数据到内存
                MyApplication.getDataCache().put(key, json);
                // 缓存数据到文件
                DataCache.cacheFile(key, json);
                parserJson(json);
            }
        });
    }

    private void parserJson(String jsonString) {
        pager.isReadData = true;
        appInfos = MyApplication.getGson().fromJson(jsonString, AppInfo.class);

        if (appInfos != null) {
            pager.isNullData = false;
        } else {
            pager.isNullData = true;
        }
    }
}
