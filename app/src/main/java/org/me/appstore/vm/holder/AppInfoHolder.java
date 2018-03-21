package org.me.appstore.vm.holder;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.me.appstore.Constants;
import org.me.appstore.databinding.ItemAppinfoBinding;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.utils.HttpUtils;

import java.util.HashMap;

import static org.me.appstore.utils.UIUtils.getContext;

/**
 * Created by user on 2018/3/21.
 * 应用条目 holder
 */

public class AppInfoHolder extends BaseHolder<AppInfo> {
    ItemAppinfoBinding binding;
    ImageView icon;

    public AppInfoHolder(View view) {
        super(view);
        binding = DataBindingUtil.bind(view);
    }

    public void setData(AppInfo data) {
        binding.setApp(data);
        // http://localhost:8080/GooglePlayServer/image?name=
        StringBuffer buffer = new StringBuffer(Constants.HOST);
        buffer.append(Constants.IMAGE);
        HashMap<String, Object> param = new HashMap<>();
        param.put("name", data.iconUrl);
        buffer.append(HttpUtils.getUrlParamsByMap(param));
        Glide.with(getContext()).load(buffer.toString()).into(binding.itemAppinfoIvIcon);
    }
}
