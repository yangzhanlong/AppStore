package org.me.appstore.vm.holder;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.bumptech.glide.Glide;
import com.orm.SugarDb;

import org.me.appstore.databinding.ItemAppinfoBinding;
import org.me.appstore.module.db.AppEntities;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.utils.AppUtils;
import org.me.appstore.utils.UIUtils;
import org.me.appstore.vm.activity.DetailActivity;
import org.me.appstore.vm.holder.download.State;

import java.util.List;

import static org.me.appstore.utils.UIUtils.getContext;

/**
 * Created by user on 2018/3/21.
 * 应用条目 holder
 */

public class AppInfoHolder extends BaseHolder<AppInfo> {
    private ItemAppinfoBinding binding;

    public AppInfoHolder(View view) {
        super(view);
        binding = DataBindingUtil.bind(view);
    }

    public void setData(final AppInfo data) {
        binding.setApp(data);
        // http://localhost:8080/GooglePlayServer/image?name=
        Glide.with(getContext()).load(getImageUrl(data.iconUrl)).into(binding.itemAppinfoIvIcon);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(UIUtils.getContext(), data.packageName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
                intent.putExtra("packageName", data.packageName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                UIUtils.getContext().startActivity(intent);
            }
        });

        SugarDb db = new SugarDb(UIUtils.getContext());
        db.onCreate(db.getDB());
        //testData(data);

        /**
         * 读取本地应用数据
         */
        readDatabase(data);
    }

    private void testData(AppInfo data) {
        AppEntities entities = new AppEntities();
        entities.appId = data.id;
        entities.packageName = data.packageName;
        entities.size = data.size;
        entities.downloadSize = 0;
        entities.state = 1;
        entities.save();
    }

    private void readDatabase(AppInfo data) {
        AppEntities entities = null;
        int state = State.DOWNLOAD_NOT;
        // 查找应用数据库记录
        List<AppEntities> appEntities = AppEntities.find(AppEntities.class, "app_id=" + data.id);
        if (appEntities != null && appEntities.size() > 0) {
            entities = appEntities.get(0);
            state = entities.state;

        } else {
            // 判断应用是否已经安装
            if (AppUtils.isInstalled(UIUtils.getContext(), data.packageName)) {
                state = State.INSTALL_ALREADY;
            }
        }
        // 设置状态信息
        setTextView(state);
    }

    public void setTextView(int state) {
        switch (state) {
            case State.INSTALL_ALREADY:
                binding.itemAppinfoDownloadTv.setText("打开");
                break;
            case State.DOWNLOAD_NOT:
                binding.itemAppinfoDownloadTv.setText("下载");
                break;
            case State.DOWNLOAD_COMPLETED:
                binding.itemAppinfoDownloadTv.setText("安装");
                break;
            case State.DOWNLOAD_WAIT:
                binding.itemAppinfoDownloadTv.setText("等待");
                break;
            case State.DOWNLOAD_STOP:
                binding.itemAppinfoDownloadTv.setText("继续");
                break;
            case State.DOWNLOADING:
                binding.itemAppinfoDownloadTv.setText("进度展示");
                break;
            case State.DOWNLOAD_ERROR:
                binding.itemAppinfoDownloadTv.setText("重试");
                break;
        }
    }
}
