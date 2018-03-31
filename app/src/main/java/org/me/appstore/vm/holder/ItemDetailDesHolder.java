package org.me.appstore.vm.holder;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.TextView;

import org.me.appstore.databinding.ItemDetailDesBinding;
import org.me.appstore.module.net.AppInfo;

/**
 * Created by user on 2018/3/31.
 * 详情界面的简介信息holder
 */
public class ItemDetailDesHolder extends BaseHolder<AppInfo> implements View.OnClickListener {
    ItemDetailDesBinding binding;
    private int wholeHeight = 0;
    private AppInfo info;

    public ItemDetailDesHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        itemView.setOnClickListener(this);
        // 默认TextView显示5行
        binding.appDetailDesTvDes.setLines(5);
    }

    @Override
    public void setData(AppInfo data) {
        binding.setApp(data);
    }

    @Override
    public void onClick(View v) {
        // itemView 点击事件
        changeDetailDesHeight(true);
    }

    // 显示 TextView 总高度
    private void changeDetailDesHeight(boolean b) {
        wholeHeight = getTextViewHeight(binding.appDetailDesTvDes);
        binding.appDetailDesTvDes.setHeight(wholeHeight);
    }

    // 计算 TextView 总高度
    private int getTextViewHeight(TextView tv) {
        int count = tv.getLineCount();
        int contentHeight=tv.getLineHeight()*count;
        int padding = tv.getPaddingTop() + tv.getPaddingBottom();
        return contentHeight + padding;
    }
}
