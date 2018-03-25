package org.me.appstore.vm.holder;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.bumptech.glide.Glide;

import org.me.appstore.databinding.ItemAppinfoBinding;
import org.me.appstore.module.net.AppInfo;

import static org.me.appstore.utils.UIUtils.getContext;

/**
 * Created by user on 2018/3/21.
 * 应用条目 holder
 */

public class AppInfoHolder extends BaseHolder<AppInfo> {
    private ItemAppinfoBinding binding;

    public AppInfoHolder(View view) {
        super(view);
        binding = DataBindingUtil.bind(view);
    }

    public void setData(AppInfo data) {
        binding.setApp(data);
        // http://localhost:8080/GooglePlayServer/image?name=
        Glide.with(getContext()).load(getImageUrl(data.iconUrl)).into(binding.itemAppinfoIvIcon);
    }
}
