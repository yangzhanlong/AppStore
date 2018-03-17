package org.me.appstore.vm.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.me.appstore.Constants;
import org.me.appstore.R;
import org.me.appstore.databinding.ItemAppinfoBinding;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.module.net.HomeInfo;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.vm.RecyclerViewFactory;

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

    public HomeFragment(Context context) {
        super(context);
    }

    // 加载成功界面
    protected void showSuccess() {
        recyclerView = RecyclerViewFactory.createVertical();
        recyclerView.setAdapter(new HomeAdapter());
        pager.changeViewTo(recyclerView);
    }

    // 耗时操作
    protected void loadData() {
//                 联网获取数据
//                1、创建联网用的客户端
//                2、创建发送请求（get或post，链接，参数）
//                3、发送请求
//                4、结果处理

        // 1、创建联网用的客户端
        // 2、创建发送请求（get或post，链接，参数）
        // http://localhost:8080/GooglePlayServer/home?index=0
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("index", 0);
        final Request request = HttpUtils.getRequest(Constants.HOME, params);
        // 3、发送请求
        Call call = HttpUtils.getClient().newCall(request);
        // call.execute() 同步
        // 异步

       call.enqueue(new BaseCallBack(pager) {
           @Override
           protected void onSuccess(Gson gson, String jsonString) {
               homeInfo = gson.fromJson(jsonString, HomeInfo.class);
               List<AppInfo> list = homeInfo.list;
               List<String> picture = homeInfo.picture;
               if ((list != null && list.size() > 0) || (picture != null && picture.size() > 0)) {
                   pager.isNullData = false;
               } else {
                   pager.isNullData = true;
               }
           }
       });
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.item_appinfo, parent,
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
            ItemAppinfoBinding binding;
            ImageView icon;

            public MyViewHolder(View view) {
                super(view);
                binding = DataBindingUtil.bind(view);
            }

            public void setData(AppInfo data) {
                binding.setApp(data);
                // http://localhost:8080/GooglePlayServer/image?name=
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