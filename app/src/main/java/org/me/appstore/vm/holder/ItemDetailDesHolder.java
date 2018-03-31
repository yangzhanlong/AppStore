package org.me.appstore.vm.holder;

import android.databinding.DataBindingUtil;
import android.view.View;

import org.me.appstore.databinding.ItemDetailDesBinding;
import org.me.appstore.module.net.AppInfo;

/**
 * Created by user on 2018/3/31.
 * 详情界面的简介信息holder
 */
public class ItemDetailDesHolder extends BaseHolder<AppInfo>{
    ItemDetailDesBinding binding;
    public ItemDetailDesHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    @Override
    public void setData(AppInfo data) {
        binding.setApp(data);
    }
}
