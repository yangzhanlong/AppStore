package org.me.appstore.vm.holder;

import android.databinding.DataBindingUtil;
import android.view.View;

import org.me.appstore.R;
import org.me.appstore.databinding.ItemDetailInfoBinding;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.utils.ImageUtils;
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
        ImageUtils.loadIntoUseFitWidth(
                UIUtils.getContext(),
                getImageUrl(data.iconUrl),
                R.drawable.ic_default,
                binding.appDetailInfoIvIcon);
        //Glide.with(UIUtils.getContext()).load(getImageUrl(data.iconUrl)).into(binding.appDetailInfoIvIcon);
    }
}
