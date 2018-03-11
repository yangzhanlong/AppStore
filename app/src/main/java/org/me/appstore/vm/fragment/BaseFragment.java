package org.me.appstore.vm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.me.appstore.R;

/**
 * Created by user on 2018/3/11.
 */

public abstract class BaseFragment extends Fragment {
    // 问题：
    // 1、在onResume方法处理流程的开始，但是Fragment的onResume方法是与Activity绑定，对于其他Fragment我们如果使用onResume方法作为流程的开始，就会出现提前加载数据的问题。
    // 网上提供的解决方案：setUserVisibleHint

    // 开发中遇到问题时：处理流程
    // 利用搜索引擎（注意关键字的选择）
    // 搜索到结果后，判断是否能够满足我们的要求，通过读取官方文档判断是否满足要求。
    // 在读官方文档是要注意特殊地方（使用的限制条件），关注：Note

    // 是否获取到数据
    protected boolean isReadData = true;
    // 数据是否为空
    protected boolean isNullData = false;
    // 四个界面
    private View loading;
    private View error;
    private View empty;
    private Context context;

    protected ViewGroup fragment_container;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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

    public BaseFragment(Context context) {
        this.context = context;
        fragment_container = (FrameLayout) View.inflate(context, R.layout.fragment_common, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return fragment_container;
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        // 动态界面加载流程的起点
//        dynamic();
//    }

    // 流程:
    // 1. 数据加载中
    // 2. 耗时操作

    // 动态界面加载流程的起点
    private void dynamic() {
        showProgress();
        loadData();
    }

    // 数据加载中
    private void showProgress() {
        loading = View.inflate(context, R.layout.pager_loading, null);
        fragment_container.removeAllViews();
        fragment_container.addView(loading);
    }

    // 加载错误界面
    private void showError() {
        error = View.inflate(context, R.layout.pager_error, null);
        fragment_container.removeAllViews();
        fragment_container.addView(error);
        // 重新获取数据
        error.findViewById(R.id.error_btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamic();
            }
        });
    }

    // 加载空界面
    private void showEmpty() {
        empty = View.inflate(context, R.layout.pager_empty, null);
        fragment_container.removeAllViews();
        fragment_container.addView(empty);
    }

    // 加载成功界面
    protected abstract void showSuccess();

    // 耗时操作
    protected abstract void loadData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            dynamic();
        }
    }
}


