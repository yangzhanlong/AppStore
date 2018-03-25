package org.me.appstore.vm.adapter;

import com.google.gson.reflect.TypeToken;

import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.R;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.module.net.SubjectInfo;

import java.util.List;

/**
 * Created by user on 2018/3/24.
 * 专题的适配器
 */
public class SubjectAdapter extends BaseRecyclerViewAdapter<SubjectInfo> {

    public SubjectAdapter(List<SubjectInfo> datas) {
        super(datas);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == datas.size()) {
            return LOADMORE;
        }
        return NORMAL;
    }

    @Override
    protected SubjectInfo getItemPosition(int position) {
        return datas.get(position);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.item_subject;
    }

    @Override
    public String getPath() {
        return Constants.SUBJECT;
    }

    @Override
    protected List<AppInfo> getNextAppInfoList(String json) {
        return MyApplication.getGson().fromJson(json, new TypeToken<List<SubjectInfo>>(){}.getType());
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() + 1: 0;
    }
}
