package org.me.appstore.vm.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by user on 2018/3/20.
 */

public abstract class BaseHolder<D> extends RecyclerView.ViewHolder {
    public BaseHolder(View itemView) {
        super(itemView);
    }

    public abstract void setData(D data);
}
