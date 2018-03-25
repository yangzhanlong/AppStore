package org.me.appstore.vm.adapter;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.me.appstore.R;
import org.me.appstore.databinding.ItemCategoryNormalBinding;
import org.me.appstore.module.net.CategoryInfo;
import org.me.appstore.utils.UIUtils;
import org.me.appstore.vm.holder.BaseHolder;

import java.util.List;

/**
 * Created by user on 2018/3/25.
 * 类别界面 适配器
 * item 分标题和非标题两种情况
 */
public class CategoryAdapter extends RecyclerView.Adapter<BaseHolder> {
    private static final int NORMAL = 0;
    private static final int TITLE = 1;

    private List<CategoryInfo> categories;

    public CategoryAdapter(List<CategoryInfo> categories) {
        this.categories = categories;
    }

    @Override
    public int getItemViewType(int position) {
        CategoryInfo categoryInfo = categories.get(position);
        if (categoryInfo.isTitle) {
            return TITLE;
        }
        return NORMAL;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseHolder holder = null;
        if (viewType == TITLE) {
            holder = new CategoryTitleHolder(View.inflate(UIUtils.getContext(), android.R.layout.simple_list_item_1, null));
        } else {
            holder = new CategoryInfosHolder(View.inflate(UIUtils.getContext(), R.layout.item_category_normal, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        CategoryInfo categoryInfo = categories.get(position);
        if (holder.getItemViewType() == TITLE) {
            ((CategoryTitleHolder)holder).setData(categoryInfo.title);
        } else {
            ((CategoryInfosHolder)holder).setData(categoryInfo);
        }
    }

    private class CategoryTitleHolder extends BaseHolder<String> {
        public CategoryTitleHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(String data) {
            ((TextView)itemView).setTextColor(Color.DKGRAY);
            ((TextView)itemView).setText(data);
        }
    }

    private class CategoryInfosHolder extends BaseHolder<CategoryInfo> {
        ItemCategoryNormalBinding binding;
        public CategoryInfosHolder(View itemView) {
            super(itemView);
            // 设置item的布局参数，默认宽不是MATCH_PARENT
            itemView.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ));
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void setData(CategoryInfo data) {
            binding.setCategory(data);
            Glide.with(UIUtils.getContext()).load(getImageUrl(data.url1)).into(binding.itemCategoryIcon1);
            Glide.with(UIUtils.getContext()).load(getImageUrl(data.url2)).into(binding.itemCategoryIcon2);
            Glide.with(UIUtils.getContext()).load(getImageUrl(data.url2)).into(binding.itemCategoryIcon3);
        }
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }
}
