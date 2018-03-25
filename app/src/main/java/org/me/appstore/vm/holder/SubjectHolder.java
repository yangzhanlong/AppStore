package org.me.appstore.vm.holder;

import android.databinding.DataBindingUtil;
import android.view.View;

import org.me.appstore.R;
import org.me.appstore.databinding.ItemSubjectBinding;
import org.me.appstore.module.net.SubjectInfo;
import org.me.appstore.utils.ImageUtils;
import org.me.appstore.utils.UIUtils;

/**
 * Created by user on 2018/3/24.
 * 专题的 holder
 */
public class SubjectHolder extends BaseHolder<SubjectInfo> {
    private ItemSubjectBinding binding;
    public SubjectHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    @Override
    public void setData(SubjectInfo data) {
        binding.setSubject(data);
        //Glide.with(UIUtils.getContext()).load(data.url).into(binding.itemSubjectIvIcon);
        ImageUtils.loadIntoUseFitWidth(UIUtils.getContext(), getImageUrl(data.url), R.drawable.ic_default, binding.itemSubjectIvIcon);
    }
}
