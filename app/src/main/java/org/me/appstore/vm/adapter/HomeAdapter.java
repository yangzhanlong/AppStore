package org.me.appstore.vm.adapter;

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
import org.me.appstore.vm.holder.BaseHolder;
import org.me.appstore.vm.holder.LoadMoreHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2018/3/21.
 * 主页适配器
 */

public class HomeAdapter extends BaseRecyclerViewAdapter<AppInfo> {
    private HomeInfo homeInfo;
    private FragmentActivity activity;

    // 如果加载其他样式的Item我们需要做的工作
    // 判断具体有哪些样式 轮播  条目  加载更多
    private static final int CAROUSEL = 1;

    public HomeAdapter(HomeInfo homeInfo, FragmentActivity activity) {
        this.homeInfo = homeInfo;
        this.activity = activity;
        this.datas = homeInfo.list;
    }

    // 步骤
    // 1. 添加getItemViewType,依据position去判断当前条目的样式
    // 2. 修改onCreateViewHolder, 依据样式加载不同的layout
    // 3. 修改onBindViewHolder, 依据样式绑定不同的数据

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return CAROUSEL;
        } else if (position == datas.size() + 1) {
            return LOADMORE;
        } else {
            return NORMAL;
        }
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseHolder holder = super.onCreateViewHolder(parent, viewType);
        if (viewType == CAROUSEL) {
            holder = new CarouselHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.home_fragment_carousel, parent,
                    false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        switch (holder.getItemViewType()) {
            case CAROUSEL:
                // 轮播
                holder.setData(homeInfo.picture);
                break;
            default:
                break;
        }
    }

    @Override
    public String getPath() {
        return Constants.HOME;
    }

    @Override
    protected List<AppInfo> getNextAppInfoList(String json) {
        HomeInfo nextPagerInfo = MyApplication.getGson().fromJson(json, HomeInfo.class);
        if (nextPagerInfo != null) {
            return nextPagerInfo.list;
        } else {
            loadMoreHolder.setData(LoadMoreHolder.NULL);
        }
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.item_appinfo;
    }

    @Override
    protected AppInfo getItemPosition(int position) {
        return datas.get(position - 1); // -1是因为有轮播,不然会越界
    }

    @Override
    public int getItemCount() {
        return homeInfo != null ? datas.size() + 1 + 1 : 0; // +1是因为多了轮播图  +1加载更多条目
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
}
