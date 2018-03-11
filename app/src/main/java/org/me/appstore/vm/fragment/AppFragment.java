package org.me.appstore.vm.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.me.appstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用
 */
public class AppFragment extends Fragment {

    private boolean isReadData = true;
    private boolean isNullData = false;
    private FrameLayout app_container;
    private View loading;
    private View error;
    private View empty;
    private ListView listView;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isReadData) {
                if (isNullData) {
                    showEmpty();
                } else {
                    showSuccess();
                }
            } else {
                showError();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        app_container = (FrameLayout) inflater.inflate(R.layout.fragment_app, null);
        return app_container;
    }

    @Override
    public void onResume() {
        super.onResume();
        dymania();
    }

    private void dymania() {
        showProgress();
        loadData();
    }

    private void showProgress() {
        loading = View.inflate(getContext(), R.layout.pager_loading, null);
        app_container.removeAllViews();
        app_container.addView(loading);
    }

    private void showError() {
        error = View.inflate(getContext(), R.layout.pager_error, null);
        app_container.removeAllViews();
        app_container.addView(error);

        error.findViewById(R.id.error_btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dymania();
            }
        });
    }

    private void showEmpty() {
        empty = View.inflate(getContext(), R.layout.pager_empty, null);
        app_container.removeAllViews();
        app_container.addView(empty);
    }

    private void showSuccess() {
        listView = (ListView) View.inflate(getContext(), R.layout.app_success, null);

        testData();
        listView.setAdapter(new AppAdapter());

        app_container.removeAllViews();
        app_container.addView(listView);
    }

    private void loadData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);
                handler.sendEmptyMessage(10);
            }
        }.start();
    }

    private List<String> mDatas;

    private void testData() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    private class AppAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = View.inflate(AppFragment.this.getContext(),R.layout.item_home,null);
            TextView tv = (TextView) inflate.findViewById(R.id.id_num);
            tv.setText(mDatas.get(i));
            return inflate;
        }
    }
}
