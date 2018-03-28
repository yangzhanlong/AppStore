package org.me.appstore.vm.holder;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.bumptech.glide.Glide;

import org.me.appstore.databinding.ItemDetailInfoBinding;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.utils.UIUtils;

/**
 * Created by user on 2018/3/28.
 * 详情界面holder
 */

public class ItemDetailInfoHolder extends BaseHolder<AppInfo> {
    ItemDetailInfoBinding binding;
    public ItemDetailInfoHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    @Override
    public void setData(AppInfo data) {
        binding.setApp(data);
        Glide.with(UIUtils.getContext()).load(getImageUrl(data.iconUrl)).into(binding.appDetailInfoIvIcon);
    }
}
