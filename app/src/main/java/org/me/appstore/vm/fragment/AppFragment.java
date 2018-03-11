package org.me.appstore.vm.fragment;


import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.me.appstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用
 */
public class AppFragment extends BaseFragment {
    protected ListView listView;

    public AppFragment(Context context) {
        super(context);
    }

    protected void showSuccess() {
        listView = (ListView) View.inflate(getContext(), R.layout.app_success, null);

        testData();
        listView.setAdapter(new AppAdapter());

        pager.commonContainer.removeAllViews();
        pager.commonContainer.addView(listView);
    }

    protected void loadData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);
                pager.runOnUiThread();
                //pager.handler.sendEmptyMessage(10);
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
