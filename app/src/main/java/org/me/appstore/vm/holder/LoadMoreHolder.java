package org.me.appstore.vm.holder;

import android.view.View;
import android.widget.LinearLayout;

import org.me.appstore.MyApplication;
import org.me.appstore.R;
import org.me.appstore.vm.adapter.BaseRecyclerViewAdapter;

/**
 * Created by user on 2018/3/21.
 *
 * 加载更多的 holder
 */

public class LoadMoreHolder extends BaseHolder<Integer> {
    public static final int LOADING = 1;
    public static final int ERROR = -1;
    public static final int NULL = 0;

    LinearLayout loading;
    LinearLayout retry;

    public LoadMoreHolder(View itemView, final BaseRecyclerViewAdapter adapter) {
        super(itemView);
        loading = (LinearLayout) itemView.findViewById(R.id.item_loadmore_container_loading);
        retry = (LinearLayout) itemView.findViewById(R.id.item_loadmore_container_retry);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.loadMoreData(LoadMoreHolder.this);
                setData(LOADING);
            }
        });
    }

    // 根据设置的状态值显示状态
    public void setData(final Integer state) {
        // 在线程更新界面
        MyApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
                retry.setVisibility(View.GONE);
                switch (state) {
                    case LOADING:
                        loading.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        retry.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
