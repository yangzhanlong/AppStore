package org.me.appstore.vm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.me.appstore.MyApplication;
import org.me.appstore.R;

/**
 * Created by user on 2018/3/11.
 */

public abstract class CommonPager {
    // 问题：
    // 1、在onResume方法处理流程的开始，但是Fragment的onResume方法是与Activity绑定，对于其他Fragment我们如果使用onResume方法作为流程的开始，就会出现提前加载数据的问题。
    // 网上提供的解决方案：setUserVisibleHint

    // 开发中遇到问题时：处理流程
    // 利用搜索引擎（注意关键字的选择）
    // 搜索到结果后，判断是否能够满足我们的要求，通过读取官方文档判断是否满足要求。
    // 在读官方文档是要注意特殊地方（使用的限制条件），关注：Note

    // 2、不停的切换Fragment，都会重新走界面流程
    // 如果当前fragment已经开始进行数据加载了，多次的开启会倒置线程的重复创建。

    // 3、流程的开启有两种情况：第一次加载，数据读取出错
    // 在切换Fragment时，如果完成了第一次加载，以后只有当数据读取出错时才会走流程

    // 4、如何整理一个通用的流程，不仅仅满足Fragment使用，还要满足Activity使用
    // a、两个Fragment中处理公共代码，放到BaseFragment中
    // b、会将Fragment与Activity通用的部分整理出来

    // 风险规避：确保CommonPager中Handler handler=new Handler()是在主线程中完成的。
    // 在Activity、Fragment、Service中创建Handler，自定义Application类

    public boolean onLoadingData = false;

    // 是否获取到数据
    public boolean isReadData = false;
    // 数据是否为空
    public boolean isNullData = false;
    // 四个界面
    public View loading;
    public View error;
    public View empty;
    public Context context;

    public ViewGroup commonContainer;


//    public final void runOnUiThread(Runnable action) {
//        if (Thread.currentThread() != mUiThread) {
//            mHandler.post(action);
//        } else {
//            action.run();
//        }
//    }

    /**
     * 确保代码在主线程中执行
     */
    public void runOnUiThread(){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                onLoadingData = false;
                if (isReadData) {
                    // 获取到数据，判断数据是否为空
                    if (isNullData) {
                        // 加载空界面
                        showEmpty();
                    } else {
                        // 加载成功界面
                        showSuccess();
                    }
                } else {
                    // 没有获取到数据, 显示错误界面
                    showError();
                }
            }
        };
        MyApplication.getHandler().post(action);
    }

    public CommonPager(Context context) {
        this.context = context;
        commonContainer = new FrameLayout(context);
    }



    // 流程:
    // 1. 数据加载中
    // 2. 耗时操作

    // 动态界面加载流程的起点
    public void dynamic() {
        if (onLoadingData || isReadData) {
            return;
        }
        onLoadingData = true;
        showProgress();
        loadData();
    }

    // 数据加载中
    public void showProgress() {
        loading = View.inflate(context, R.layout.pager_loading, null);
        changeViewTo(loading);
    }

    // 加载错误界面
    public void showError() {
        error = View.inflate(context, R.layout.pager_error, null);
        changeViewTo(error);
        // 重新获取数据
        error.findViewById(R.id.error_btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamic();
            }
        });
    }

    // 加载空界面
    public void showEmpty() {
        empty = View.inflate(context, R.layout.pager_empty, null);
        changeViewTo(empty);
    }

    // 加载成功界面
    public abstract void showSuccess();

    // 耗时操作
    public abstract void loadData();

    public void changeViewTo(View view) {
        commonContainer.removeAllViews();
        commonContainer.addView(view);
    }
}


