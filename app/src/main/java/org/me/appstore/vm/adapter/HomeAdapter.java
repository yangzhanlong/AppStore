package org.me.appstore.vm.adapter;

import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.R;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.module.net.HomeInfo;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.utils.UIUtils;
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
 * Created by user on 2018/3/21.
 */

public class HomeAdapter extends RecyclerView.Adapter<BaseHolder> {
    private HomeInfo homeInfo;
    private FragmentActivity activity;

    // 如果加载其他样式的Item我们需要做的工作
    // 判断具体有哪些样式 轮播  条目  加载更多
    private static final int NORMAL = 0;
    private static final int CAROUSEL = 1;
    private static final int LOADMORE = 2;

    public HomeAdapter(HomeInfo homeInfo, FragmentActivity activity) {
        this.homeInfo = homeInfo;
        this.activity = activity;
    }

    // 步骤
    // 1. 添加getItemViewType,依据position去判断当前条目的样式
    // 2. 修改onCreateViewHolder, 依据样式加载不同的layout
    // 3. 修改onBindViewHolder, 依据样式绑定不同的数据

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return CAROUSEL;
        } else if (position == homeInfo.list.size() + 1) {
            return LOADMORE;
        } else {
            return NORMAL;
        }
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseHolder holder = null;
        switch (viewType) {
            case NORMAL:
                holder = new AppInfoHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_appinfo, parent,
                        false));
                break;
            case CAROUSEL:
                holder = new CarouselHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.home_fragment_carousel, parent,
                        false));
                break;
            case LOADMORE:
                holder = new LoadMoreHolder(LayoutInflater.from(
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
                AppInfo appInfo = homeInfo.list.get(position - 1); // 这里必须要 -1，不然会越界
                holder.setData(appInfo);
                break;
            case CAROUSEL:
                // 轮播
                holder.setData(homeInfo.picture);
                break;
            case LOADMORE:
                // 加载更多，设置loading状态
                // 最后一项显示出来，这样就不用判断 RecyclerView 是否滚动到底部
                holder.setData(LoadMoreHolder.LOADING);
                loadMoreData((LoadMoreHolder) holder);
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return homeInfo != null ? homeInfo.list.size() + 1 + 1 : 0; // +1是因为多了轮播图  +1加载更多条目
    }

    /**
     * 轮播使用的 holder
     */
    class CarouselHolder extends BaseHolder<List<String>> {

        private final SliderLayout sliderLayout;

        public CarouselHolder(View itemView) {
            super(itemView);
            sliderLayout = (SliderLayout) itemView;

            // 高度的获取，保持图片的宽高比例不变
            // 181 / 480
            // 读取屏幕的宽度
            Display display = activity.getWindowManager().getDefaultDisplay();
            // 计算高度
            int height = display.getWidth() * 181 / 480; // 像素
            // 定义宽高参数
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            // sliderLayout 设置宽高
            sliderLayout.setLayoutParams(params);
        }

        public void setData(List<String> data) {
            // 防止不断添加 data到 sliderLayout
            sliderLayout.removeAllSliders();

            // 创建item项，添加到 sliderLayout
            for (String item : data) {
                // DefaultSliderView 不包括文本，TextSliderView包括文本
                DefaultSliderView sliderView = new DefaultSliderView(UIUtils.getContext());

                StringBuffer buffer = new StringBuffer(Constants.HOST);
                HashMap<String, Object> param = new HashMap<>();
                param.put("name", item);
                buffer.append(Constants.IMAGE);
                buffer.append(HttpUtils.getUrlParamsByMap(param));
                sliderView.image(buffer.toString());
                sliderLayout.addSlider(sliderView);
            }
        }
    }

    /**
     * 加载更多数据
     *
     * @param holder
     */
    public void loadMoreData(final LoadMoreHolder holder) {
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
        String key = Constants.HOME + "." + homeInfo.list.size();
        String json = DataCache.getDataFromLocal(key);
        if (json == null) {
            // 加载网络数据
            OkHttpClient client = new OkHttpClient();
            HashMap<String, Object> params = new HashMap<>();
            params.put("index", homeInfo.list.size());
            final Request request = HttpUtils.getRequest(Constants.HOME, params);
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // 显示重试条目
                    holder.setData(LoadMoreHolder.ERROR);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // 判断是否获取到网络数据
                    if (response.code() == 200) {
                        String json = response.body().string();
                        showNextPagerData(json, holder);
                    } else {
                        // 显示重试条目
                        holder.setData(LoadMoreHolder.ERROR);
                    }
                }
            });

        } else {
            // 加载本地数据
            showNextPagerData(json, holder);
        }
    }

    /**
     * 处理加载到的数据并展示
     *
     * @param json
     * @param holder
     */
    private void showNextPagerData(String json, LoadMoreHolder holder) {
        HomeInfo nextPagerInfo = MyApplication.getGson().fromJson(json, HomeInfo.class);
        if (nextPagerInfo != null) {
            List<AppInfo> nextAppInfoList = nextPagerInfo.list;
            if (nextAppInfoList != null && nextAppInfoList.size() > 0) {
                // 在原来的集合添加更新的数据集合
                homeInfo.list.addAll(nextAppInfoList);
                SystemClock.sleep(2000);
                // 通知更新界面，需要在主线程更新
                MyApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            } else {
                holder.setData(LoadMoreHolder.NULL);
            }
        } else {
            holder.setData(LoadMoreHolder.NULL);
        }
    }
}
