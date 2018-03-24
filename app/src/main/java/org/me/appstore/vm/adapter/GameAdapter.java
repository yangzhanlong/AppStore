package org.me.appstore.vm.adapter;

import com.google.gson.reflect.TypeToken;

import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.R;
import org.me.appstore.module.net.AppInfo;

import java.util.List;

/**
 * Created by user on 2018/3/24.
 * 游戏界面
 */
public class GameAdapter extends BaseRecyclerViewAdapter {
    public GameAdapter(List<AppInfo> apps) {
        super(apps);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == datas.size()) {
            return LOADMORE;
        }
        return NORMAL;
    }

    @Override
    protected Object getItemPosition(int position) {
        return datas.get(position);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.item_appinfo;
    }

    @Override
    public String getPath() {
        return Constants.GAME;
    }

    @Override
    protected List<AppInfo> getNextAppInfoList(String json) {
        return MyApplication.getGson().fromJson(json, new TypeToken<List<AppInfo>>() {
        }.getType());
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }
}
