package org.me.appstore.vm;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.me.appstore.utils.UIUtils;

/**
 * Created by user on 2018/3/17.
 */

public class RecyclerViewFactory {

    /**
     * 获取垂直滚动的 RecyclerView
     * @return
     */
    public static RecyclerView createVertical() {
        RecyclerView recyclerView = new RecyclerView(UIUtils.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(), LinearLayoutManager.VERTICAL, false));
        return recyclerView;
    }
}
