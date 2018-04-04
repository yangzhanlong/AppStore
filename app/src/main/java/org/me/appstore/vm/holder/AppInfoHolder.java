package org.me.appstore.vm.holder;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.bumptech.glide.Glide;

import org.me.appstore.databinding.ItemAppinfoBinding;
import org.me.appstore.module.db.AppEntities;
import org.me.appstore.module.net.AppInfo;
import org.me.appstore.utils.AppUtils;
import org.me.appstore.utils.ThreadPoolUtils;
import org.me.appstore.utils.UIUtils;
import org.me.appstore.vm.activity.DetailActivity;
import org.me.appstore.vm.holder.download.DownloadInfo;
import org.me.appstore.vm.holder.download.DownloadManager;
import org.me.appstore.vm.holder.download.DownloadTask;
import org.me.appstore.vm.holder.download.State;

import java.util.List;

import static org.me.appstore.utils.UIUtils.getContext;

/**
 * Created by user on 2018/3/21.
 * 应用条目 holder
 */

public class AppInfoHolder extends BaseHolder<AppInfo> implements View.OnClickListener {
    private ItemAppinfoBinding binding;

    public AppInfoHolder(View view) {
        super(view);
        binding = DataBindingUtil.bind(view);
        DownloadManager.APP_INFO_HOLDERS.add(this);
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

        //SugarDb db = new SugarDb(UIUtils.getContext());
        //db.onCreate(db.getDB());


        // 从集合里获取下载信息
        DownloadInfo downloadInfo = DownloadManager.DOWNLOAD_CACHES.get(data.id);
        if (downloadInfo == null) {
            downloadInfo = new DownloadInfo();
            downloadInfo.appId = data.id;
            downloadInfo.packageName = data.packageName;
            downloadInfo.downloadUrl = data.downloadUrl;
            downloadInfo.size = data.size;

            // 读取本地应用数据库数据
            readDatabase(downloadInfo);

            // 保存信息到集合
            DownloadManager.DOWNLOAD_CACHES.put(data.id, downloadInfo);
        }

        // 设置下载状态信息
        setTextView(downloadInfo.state);

        // 下载点击监听
        binding.itemAppinfoDownloadIv.setOnClickListener(this);
    }

    private void readDatabase(DownloadInfo info) {
        info.state = State.DOWNLOAD_NOT;
        // 查找应用数据库记录
        List<AppEntities> appEntities = AppEntities.find(AppEntities.class, "app_id=" + info.appId);
        if (appEntities != null && appEntities.size() > 0) {
            AppEntities entities = appEntities.get(0);
            info.state = entities.state;
            info.downloadSize = entities.downloadSize;
        } else {
            // 判断应用是否已经安装
            if (AppUtils.isInstalled(UIUtils.getContext(), info.packageName)) {
                info.state = State.INSTALL_ALREADY;
            }
        }
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

    @Override
    public void onClick(View v) {
        DownloadTask task = new DownloadTask(DownloadManager.DOWNLOAD_CACHES.get(binding.getApp().id));
        boolean execute = ThreadPoolUtils.execute(task);
        if (!execute) {
            setTextView(State.DOWNLOAD_WAIT); // 等待状态
        }
    }

    public void update(DownloadInfo downloadInfo) {
        if (binding.getApp().id == downloadInfo.appId) {
            setTextView(downloadInfo.state);
        }
    }
}