package org.me.appstore.vm.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.me.appstore.R;
import org.me.appstore.utils.ImageUtils;
import org.me.appstore.utils.UIUtils;

import java.util.List;

/**
 * Created by user on 2018/3/28.
 * 详情界面图片信息holder
 */

public class ItemDetailImageHolder extends BaseHolder<List<String>> {
    LinearLayout mAppDetailPicIvContainer;

    public ItemDetailImageHolder(View itemView) {
        super(itemView);

        mAppDetailPicIvContainer = (LinearLayout) itemView.findViewById(R.id.app_detail_pic_iv_container);
    }

    @Override
    public void setData(List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            String url = data.get(i);

            // 获取图片添加到 mAppDetailPicIvContainer 容器中
            ImageView imageView = new ImageView(UIUtils.getContext());
            // 宽度已知-->屏幕的1/3
            // 获取屏幕的宽度
            int screenWidth = UIUtils.getResources().getDisplayMetrics().widthPixels;
            int width = screenWidth / 3;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置图片间隙
            if (i != 0) {
                params.leftMargin = UIUtils.dip2Px(4);
            }

            imageView.setLayoutParams(params);
            ImageUtils.loadIntoUseFitWidth(UIUtils.getContext(), getImageUrl(url), R.drawable.ic_default, imageView);
            //Glide.with(UIUtils.getContext()).load(getImageUrl(safeInfo.safeUrl)).into(imageView);
            mAppDetailPicIvContainer.addView(imageView);
        }
    }
}
