package org.me.appstore.vm.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.me.appstore.Constants;
import org.me.appstore.R;
import org.me.appstore.databinding.ItemAppinfoBinding;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 应用
 */
public class AppFragment extends BaseFragment {
    // 展示 成功界面
    private RecyclerView recyclerView;
    private List<AppInfo> apps;

    public AppFragment(Context context) {
        super(context);
    }

    protected void showSuccess() {
        recyclerView = (RecyclerView) View.inflate(getContext(), R.layout.home_success, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(new AppAdapter());

        pager.commonContainer.removeAllViews();
        pager.commonContainer.addView(recyclerView);
    }

    protected void loadData() {
//                1、创建联网用的客户端
//                2、创建发送请求（get或post，链接，参数）
//                3、发送请求
//                4、结果处理

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        StringBuffer buffer = new StringBuffer(Constants.HOST);
        buffer.append(Constants.APP);
        final HashMap<String, Object> param = new HashMap<>();
        param.put("index", 0);
        buffer.append(HttpUtils.getUrlParamsByMap(param));
        final Request request = new Request.Builder().get().url(buffer.toString()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pager.isReadData = false;
                pager.runOnUiThread();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    pager.isReadData = true;
                    String jsonString = response.body().string();
                    Log.i("jsonstring", jsonString);
                    Gson gson = new Gson();
                    apps = gson.fromJson(jsonString, new TypeToken<List<AppInfo>>() {
                    }.getType());

                    if (apps != null && apps.size() > 0) {
                        pager.isNullData = false;
                    } else {
                        pager.isNullData = true;
                    }
                } else {
                    pager.isReadData = false;
                }
                pager.runOnUiThread();
            }
        });

    }

    class AppAdapter extends RecyclerView.Adapter<AppAdapter.ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemHolder itemHolder = new ItemHolder(View.inflate(
                    getContext(), R.layout.item_appinfo, null));
            return itemHolder;
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            AppInfo appInfo = apps.get(position);
            holder.setData(appInfo);
        }

        @Override
        public int getItemCount() {
            return apps != null ? apps.size() : 0;
        }

        class ItemHolder extends RecyclerView.ViewHolder {

            private final ItemAppinfoBinding binding;

            public ItemHolder(View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
            }

            public void setData(AppInfo data) {
                binding.setApp(data);
                StringBuffer buffer = new StringBuffer(Constants.HOST);
                buffer.append(Constants.IMAGE);
                HashMap<String, Object> param = new HashMap<>();
                param.put("name", data.iconUrl);
                buffer.append(HttpUtils.getUrlParamsByMap(param));
                Glide.with(getContext()).load(buffer.toString()).into(binding.itemAppinfoIvIcon);
            }
        }
    }
}
