package org.me.appstore.vm.adapter;

import android.support.v7.widget.RecyclerView;

import org.me.appstore.vm.holder.BaseHolder;
import org.me.appstore.vm.holder.LoadMoreHolder;

/**
 * Created by user on 2018/3/22.
 * 通用的适配器
 */

public abstract class BaseRecyclerViewAdapter<D> extends RecyclerView.Adapter<BaseHolder<D>> {
    public abstract void loadMoreData(LoadMoreHolder holder);
}