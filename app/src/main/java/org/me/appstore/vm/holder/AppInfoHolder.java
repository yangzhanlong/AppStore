package org.me.appstore.vm.holder;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.bumptech.glide.Glide;

import org.me.appstore.databinding.ItemAppinfoBinding;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.utils.UIUtils;
import org.me.appstore.vm.activity.DetailActivity;

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

    public void setData(final AppInfo data) {
        binding.setApp(data);
        // http://localhost:8080/GooglePlayServer/image?name=
        Glide.with(getContext()).load(getImageUrl(data.iconUrl)).into(binding.itemAppinfoIvIcon);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(UIUtils.getContext(), data.packageName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
                intent.putExtra("packageName", data.packageName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                UIUtils.getContext().startActivity(intent);
            }
        });
    }
}
