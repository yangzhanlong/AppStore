package org.me.appstore.vm.adapter;

import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.me.appstore.MyApplication;
import org.me.appstore.R;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.vm.DataCache;
import org.me.appstore.vm.holder.AppInfoHolder;
import org.me.appstore.vm.holder.BaseHolder;
import org.me.appstore.vm.holder.LoadMoreHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 2018/3/22.
 * 通用的适配器
 */

public abstract class BaseRecyclerViewAdapter<D> extends RecyclerView.Adapter<BaseHolder<D>> {
    protected List<D> datas;

    // 如果加载其他样式的Item我们需要做的工作
    // 判断具体有哪些样式 轮播  条目  加载更多
    protected static final int NORMAL = 0;
    protected static final int LOADMORE = 2;
    protected LoadMoreHolder loadMoreHolder;


    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseHolder holder = null;
        switch (viewType) {
            case NORMAL:
                holder = new AppInfoHolder(LayoutInflater.from(
                        parent.getContext()).inflate(getLayoutID(), parent,
                        false));
                break;
            case LOADMORE:
                holder = loadMoreHolder = new LoadMoreHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_loadmore, parent,
                        false), this);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case NORMAL:
                D appInfo = getItemPosition(position);
                holder.setData(appInfo);
                break;
            case LOADMORE:
                // 加载更多，设置loading状态
                // 最后一项显示出来，这样就不用判断 RecyclerView 是否滚动到底部
                holder.setData(LoadMoreHolder.LOADING);
                loadMoreData();
            default:
                break;
        }
    }

    /**
     * 加载更多数据
     */
    public void loadMoreData() {
        // 加载本地
        // 是否加载到数据
        // 没加载到，获取网络数据
        // 是否加载到数据
        // 加载到，展示界面
        // 没有加载到，重试界面显示

        // 判断是否还有下一页数据
        // 有，显示加载中条目
        // 没有，不显示

        //key:下一页开始的 index的值与集合数据大小相等
        String key = getPath() + "." + datas.size();
        String json = DataCache.getDataFromLocal(key);
        if (json == null) {
            // 加载网络数据
            OkHttpClient client = new OkHttpClient();
            HashMap<String, Object> params = new HashMap<>();
            params.put("index", datas.size());
            final Request request = HttpUtils.getRequest(getPath(), params);
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // 显示重试条目
                    loadMoreHolder.setData(LoadMoreHolder.ERROR);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // 判断是否获取到网络数据
                    if (response.code() == 200) {
                        String json = response.body().string();
                        showNextPagerData(json);
                    } else {
                        // 显示重试条目
                        loadMoreHolder.setData(LoadMoreHolder.ERROR);
                    }
                }
            });

        } else {
            // 加载本地数据
            showNextPagerData(json);
        }
    }

    /**
     * 处理加载到的数据并展示
     *
     * @param json
     */
    protected void showNextPagerData(String json) {
        List<D> nextAppInfoList = (List<D>) getNextAppInfoList(json);
        if (nextAppInfoList != null && nextAppInfoList.size() > 0) {
            // 在原来的集合添加更新的数据集合
            datas.addAll(nextAppInfoList);
            SystemClock.sleep(2000);
            // 通知更新界面，需要在主线程更新
            MyApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        } else {
            loadMoreHolder.setData(LoadMoreHolder.NULL);
        }
    }

    /**
     * 获取当前条目的 position
     * @return
     * @param position
     */
    protected abstract D getItemPosition(int position);

    /**
     * 获取布局的id
     * @return
     */
    protected abstract int getLayoutID();

    /**
     * 获取服务器不同模块的链接
     * @return
     */
    public abstract String getPath();

    /**
     * 获取下一页数据
     * @param json
     * @return
     */
    protected abstract List<AppInfo> getNextAppInfoList(String json);
}