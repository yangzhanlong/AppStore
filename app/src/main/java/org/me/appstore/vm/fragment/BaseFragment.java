package org.me.appstore.vm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.me.appstore.vm.CommonPager;

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

    // 2、不停的切换Fragment，都会重新走界面流程
    // 如果当前fragment已经开始进行数据加载了，多次的开启会倒置线程的重复创建。

    // 3、流程的开启有两种情况：第一次加载，数据读取出错
    // 在切换Fragment时，如果完成了第一次加载，以后只有当数据读取出错时才会走流程

    // 4、如何整理一个通用的流程，不仅仅满足Fragment使用，还要满足Activity使用
    // a、两个Fragment中处理公共代码，放到BaseFragment中
    // b、会将Fragment与Activity通用的部分整理出来

    // 风险规避：确保CommonPager中Handler handler=new Handler()是在主线程中完成的。
    // 在Activity、Fragment、Service中创建Handler，自定义Application类

    protected CommonPager pager;
    private Context context;

    public BaseFragment(Context context) {
        this.context = context;
        pager = new CommonPager(context) {
            @Override
            public void showSuccess() {
                BaseFragment.this.showSuccess();
            }

            @Override
            public void loadData() {
                BaseFragment.this.loadData();
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return pager.commonContainer;
    }


    // 加载成功界面
    protected abstract void showSuccess();

    // 耗时操作
    protected abstract void loadData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            pager.dynamic();
        }
    }
}


