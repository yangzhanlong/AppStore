package org.me.appstore.vm.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.me.appstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
public class HomeFragment extends Fragment {

    // 是否获取到数据
    private boolean isReadData = true;
    // 数据是否为空
    private boolean isNullData = false;
    private FrameLayout home_container;

    // 四个界面
    private View loading;
    private View error;
    private View empty;
    // 展示 成功界面
    private RecyclerView recyclerView;

    private Handler handler = new Handler() {
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

    // 流程:
    // 1. 数据加载中
    // 2. 耗时操作

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        home_container = (FrameLayout) inflater.inflate(R.layout.fragment_home, null);
        return home_container;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 动态界面加载流程的起点
        dynamic();
    }

    // 动态界面加载流程的起点
    private void dynamic() {
        showProgress();
        loadData();
    }

    // 数据加载中
    private void showProgress() {
        loading = View.inflate(getContext(), R.layout.pager_loading, null);
        home_container.removeAllViews();
        home_container.addView(loading);
    }

    // 加载错误界面
    private void showError() {
        error = View.inflate(getContext(), R.layout.pager_error, null);
        home_container.removeAllViews();
        home_container.addView(error);
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
        empty = View.inflate(getContext(), R.layout.pager_empty, null);
        home_container.removeAllViews();
        home_container.addView(empty);
    }

    // 加载成功界面
    private void showSuccess() {
        recyclerView = (RecyclerView) View.inflate(getContext(), R.layout.home_success, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        testData();
        recyclerView.setAdapter(new HomeAdapter());

        home_container.removeAllViews();
        home_container.addView(recyclerView);
    }

    // 耗时操作
    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);

                handler.sendEmptyMessage(10);
            }
        }).start();
    }

    private List<String> mDatas;

    private void testData() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.item_home, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
            }
        }
    }
}
