package org.me.appstore.vm.adapter;


import com.google.gson.reflect.TypeToken;

import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.R;
import org.me.appstore.module.net.AppInfo;

import java.util.List;

/**
 * Created by user on 2018/3/21.
 * 应用适配器
 */

public class AppAdapter extends BaseRecyclerViewAdapter<AppInfo> {
    public AppAdapter(List<AppInfo> apps) {
        this.datas = apps;
    }

    // 步骤
    // 1. 添加getItemViewType,依据position去判断当前条目的样式
    // 2. 修改onCreateViewHolder, 依据样式加载不同的layout
    // 3. 修改onBindViewHolder, 依据样式绑定不同的数据

    @Override
    public int getItemViewType(int position) {
        if (position == datas.size()) {
            return LOADMORE;
        }
        return NORMAL;
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() + 1 : 0; // +1加载更多条目
    }

    @Override
    protected int getLayoutID() {
        return R.layout.item_appinfo;
    }

    @Override
    protected AppInfo getItemPosition(int position) {
        return datas.get(position);
    }

    @Override
    public String getPath() {
        return Constants.APP;
    }

    @Override
    protected List<AppInfo> getNextAppInfoList(String json) {
        return MyApplication.getGson().fromJson(json, new TypeToken<List<AppInfo>>() {
        }.getType());
    }
}
