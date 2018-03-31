package org.me.appstore.vm.holder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.me.appstore.R;
import org.me.appstore.module.net.SafeInfo;
import org.me.appstore.utils.ImageUtils;
import org.me.appstore.utils.UIUtils;

import java.util.List;

/**
 * Created by user on 2018/3/28.
 * 详情界面安全信息holder
 */

public class ItemDetailSafeHolder extends BaseHolder<List<SafeInfo>> implements View.OnClickListener {
    ImageView mAppDetailSafeIvArrow;
    LinearLayout mAppDetailSafePicContainer;
    LinearLayout mAppDetailSafeDesContainer;
    private int height;
    private boolean isClosed = true;

    public ItemDetailSafeHolder(View itemView) {
        super(itemView);

        mAppDetailSafeIvArrow = (ImageView) itemView.findViewById(R.id.app_detail_safe_iv_arrow);
        mAppDetailSafePicContainer = (LinearLayout) itemView.findViewById(R.id.app_detail_safe_pic_container);
        mAppDetailSafeDesContainer = (LinearLayout) itemView.findViewById(R.id.app_detail_safe_des_container);

        // 初始化容器的高度为0
        setHeight(0);

        // 缩放点击事件
        itemView.setOnClickListener(this);
    }

    @Override
    public void setData(List<SafeInfo> data) {
        for (int i = 0; i < data.size(); i++) {
            // 获取图片添加到 mAppDetailSafePicContainer 容器中
            ImageView imageView = new ImageView(UIUtils.getContext());
            // 设置宽高
            imageView.setLayoutParams(new LinearLayout.LayoutParams(UIUtils.dip2Px(50), ViewGroup.LayoutParams.WRAP_CONTENT));
            SafeInfo safeInfo = data.get(i);
            ImageUtils.loadIntoUseFitWidth(UIUtils.getContext(), getImageUrl(safeInfo.safeUrl), R.drawable.ic_default, imageView);
            //Glide.with(UIUtils.getContext()).load(getImageUrl(safeInfo.safeUrl)).into(imageView);
            mAppDetailSafePicContainer.addView(imageView);


            // 向 mAppDetailSafeDesContainer 容器添加数据
            LinearLayout linearLayout = new LinearLayout(UIUtils.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            // 获取图片添加到ivDesIcon
            ImageView ivDesIcon = new ImageView(UIUtils.getContext());
            ivDesIcon.setLayoutParams(new LinearLayout.LayoutParams(UIUtils.dip2Px(18), ViewGroup.LayoutParams.WRAP_CONTENT));
            ImageUtils.loadIntoUseFitWidth(UIUtils.getContext(), getImageUrl(data.get(i).safeDesUrl), R.drawable.ic_default, ivDesIcon);
            //Glide.with(UIUtils.getContext()).load(getImageUrl(data.get(i).safeDesUrl)).into(ivDesIcon);

            // 设置文本数据
            TextView textView = new TextView(UIUtils.getContext());
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setSingleLine(true); // 设置单行数据
            textView.setText(data.get(i).safeDes);

            // 添加图片和文本到 linearLayout
            linearLayout.addView(ivDesIcon);
            linearLayout.addView(textView);

            mAppDetailSafeDesContainer.addView(linearLayout);
        }
    }

    @Override
    public void onClick(View v) {
        if (isClosed) {
            // 设置容器原始高度
            setWholeHeight();
            // 设置高度渐变动画
            setAnimator(0, height);
            // 设置图片旋转动画
            ObjectAnimator animator = ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 0, 180);
            animator.setDuration(300).start();
        } else {
            setWholeHeight();
            setAnimator(height, 0);
            ObjectAnimator animator = ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 180, 0);
            animator.setDuration(300).start();
        }

        isClosed = !isClosed;
    }

    private void setWholeHeight() {
        if (height == 0) {
            mAppDetailSafeDesContainer.measure(0, 0);
            height = mAppDetailSafeDesContainer.getMeasuredHeight();
        }
    }

    private void setAnimator(int start, int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setHeight((Integer) animation.getAnimatedValue());
            }
        });

        valueAnimator.start();
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
        layoutParams.height = height;
        mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
    }
}
