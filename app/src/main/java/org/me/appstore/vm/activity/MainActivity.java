package org.me.appstore.vm.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.me.appstore.R;
import org.me.appstore.vm.fragment.FragmentFactory;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        // 注意：需要在setSupportActionBar 调用之前设置好ToolBar的配置
        // 使用toolbar 代替 actionbar
        setSupportActionBar(toolBar);

        // 初始化 DrawerLayout
        // 将Toolbar与DrawerLayout整合
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);
        ActionBarDrawerToggle barDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolBar, R.string.open, R.string.close);
        barDrawerToggle.syncState();

        // 如果想看到菜单滑动过程中，Toolbar的一些细节变化（菜单按钮）
        drawerLayout.addDrawerListener(barDrawerToggle);

        // 处理菜单项点击
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 处理 TableLayout
        // 1. 手动添加tab

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));

        // 2. 与ViewPager 绑定 通过 PagerAdapter的getPageTitle(int position) 可以获取到选项卡
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        return true;
    }

    private class ViewPageAdapter extends FragmentStatePagerAdapter {
        private final String tab_names[];

        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
            //tab_names = UIUtils.getStrings(R.array.tab_names);
            tab_names = getApplicationContext().getResources().getStringArray(R.array.tab_names);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.createFragment(position, MainActivity.this);
        }

        @Override
        public int getCount() {
            return tab_names.length;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tab_names[position];
        }
    }
}
