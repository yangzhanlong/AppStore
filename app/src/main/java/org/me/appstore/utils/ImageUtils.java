package org.me.appstore.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by itheima.
 * 图片处理工具类
 */
public class ImageUtils {

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     * Note: ImageView android:layout_width="match_parent"
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                setLayoutParams(resource, imageView);
                                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                        });
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }

    private static void setLayoutParams(GlideDrawable resource, ImageView imageView) {
        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        // 获取容器实际存放图片的宽度
        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
        // 计算出容器实际存放图片宽度与图片资源宽度的比例
        float scale = (float) vw / (float) resource.getIntrinsicWidth();
        // 依据比例算出容器实际存放图片高度值
        int vh = Math.round(resource.getIntrinsicHeight() * scale);
        // 计算容器的高度
        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
        imageView.setLayoutParams(params);
    }

}
