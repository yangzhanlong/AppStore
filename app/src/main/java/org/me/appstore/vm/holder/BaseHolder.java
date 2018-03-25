package org.me.appstore.vm.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.me.appstore.Constants;
import org.me.appstore.utils.HttpUtils;

import java.util.HashMap;


/**
 * Created by user on 2018/3/20.
 * 通用的 holder
 */

public abstract class BaseHolder<D> extends RecyclerView.ViewHolder {
    public BaseHolder(View itemView) {
        super(itemView);
    }

    public String getImageUrl(String url) {
        StringBuffer buffer = new StringBuffer(Constants.HOST);
        buffer.append(Constants.IMAGE);
        HashMap<String, Object> param = new HashMap<>();
        param.put("name", url);
        buffer.append(HttpUtils.getUrlParamsByMap(param));
        return buffer.toString();
    }

    public abstract void setData(D data);
}
