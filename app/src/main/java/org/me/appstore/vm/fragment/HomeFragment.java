package org.me.appstore.vm.fragment;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.me.appstore.Constants;
import org.me.appstore.R;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.module.net.HomeInfo;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.utils.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {
    // 展示 成功界面
    private RecyclerView recyclerView;
    private HomeInfo homeInfo;

    public HomeFragment(Context context) {
        super(context);
    }

    // 加载成功界面
    protected void showSuccess() {
        recyclerView = (RecyclerView) View.inflate(getContext(), R.layout.home_success, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //testData();
        recyclerView.setAdapter(new HomeAdapter());

        pager.commonContainer.removeAllViews();
        pager.commonContainer.addView(recyclerView);
    }

    // 耗时操作
    protected void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                 联网获取数据
//                1、创建联网用的客户端
//                2、创建发送请求（get或post，链接，参数）
//                3、发送请求
//                4、结果处理

                // 1、创建联网用的客户端
                OkHttpClient client = new OkHttpClient();
                // 2、创建发送请求（get或post，链接，参数）
                Request.Builder builder = new Request.Builder();
                builder.get();
                // http://localhost:8080/GooglePlayServer/home?index=0
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("index", 0);
                String urlParamsByMap = HttpUtils.getUrlParamsByMap(map);
                String url = Constants.HOST + Constants.HOME + urlParamsByMap;
                builder.url(url);
                final Request request = builder.build();
                // 3、发送请求
                Call call = client.newCall(request);
                // call.execute() 同步
                // 异步
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        pager.isReadData = false;
                        // 更新界面
                        pager.runOnUiThread();
                    }

                    // 4、结果处理
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
                            homeInfo = gson.fromJson(jsonString, HomeInfo.class);
                            List<AppInfo> list = homeInfo.list;
                            List<String> picture = homeInfo.picture;
                            if ((list != null && list.size() > 0) || (picture != null && picture.size() > 0)) {
                                pager.isNullData = false;
                            } else {
                                pager.isNullData = true;
                            }
                        } else {
                            pager.isReadData = false;
                        }

                        // 更新界面
                        pager.runOnUiThread();
                    }
                });
            }
        }).start();
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.item_home, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            AppInfo appInfo = homeInfo.list.get(position);
            holder.setData(appInfo);
        }

        @Override
        public int getItemCount() {
            return homeInfo != null ? homeInfo.list.size() : 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView icon;
            TextView title;
            TextView size;
            RatingBar stars;
            TextView des;

            TextView tv;
            public MyViewHolder(View view) {
                super(view);
                icon = (ImageView) view.findViewById(R.id.item_appinfo_iv_icon);
                title = (TextView) view.findViewById(R.id.item_appinfo_tv_title);
                size = (TextView) view.findViewById(R.id.item_appinfo_tv_size);
                stars = (RatingBar) view.findViewById(R.id.item_appinfo_rb_stars);
                des = (TextView) view.findViewById(R.id.item_appinfo_tv_des);
            }

            public void setData(AppInfo data) {
                title.setText(data.name);
                des.setText(data.des);
                stars.setRating(data.stars);
                size.setText(StringUtil.formatFileSize(data.size));
            }
        }
    }
}