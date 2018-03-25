package org.me.appstore.vm.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by user on 2018/3/6.
 * 创建 Fragment 工厂   ------简单工厂模式
 * 依据传递的条件，产生一系列同类产品
 */

public class FragmentFactory {

    //创建Fragment
    public static Fragment createFragment(int position, Context context) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment(context);
                break;
            case 1:
                fragment = new AppFragment(context);
                break;
            case 2:
                fragment = new GameFragment(context);
                break;
            case 3:
                fragment = new SubjectFragment(context);
                break;
            case 4:
                fragment = new CategoryFragment(context);
                break;
        }
        return fragment;
    }
}
