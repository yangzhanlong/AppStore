package org.me.appstore.vm.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by user on 2018/3/6.
 * 创建 Fragment 工厂   ------简单工厂模式
 * 依据传递的条件，产生一系列同类产品
 */

public class FragmentFactory {

    //创建Fragment
    public static Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new AppFragment();
                break;
        }
        return fragment;
    }
}
