package org.me.appstore.module.db;

import com.orm.SugarRecord;

import org.me.appstore.utils.FileUtils;
import org.me.appstore.vm.holder.download.DownloadInfo;
import org.me.appstore.vm.holder.download.DownloadManager;
import org.me.appstore.vm.holder.download.State;

import java.util.List;

/**
 * Created by user on 2018/4/1.
 * 应用数据库信息
 */

public class AppEntities extends SugarRecord {
    public long appId; //应用标识
    public String packageName; //应用包名
    public long size; // 文件大小
    public long downloadSize; // 已经下载大小
    public int state; // 应用状态

    /**
     * 插入数据库
     * @param downloadInfo
     */
    public static void insertEntities(DownloadInfo downloadInfo) {
        List<AppEntities> appEntities = AppEntities.find(AppEntities.class, "APP_ID = " + downloadInfo.appId);
        AppEntities entities = null;
        // 如果数据库有记录，更新字段,如果没有记录，增加记录
        if (appEntities !=null && appEntities.size() > 0) {
            entities = appEntities.get(0);
        } else {
            entities = new AppEntities();
            entities.packageName = downloadInfo.packageName;
            entities.appId = downloadInfo.appId;
            entities.size = downloadInfo.size;
        }
        entities.state = downloadInfo.state;
        entities.downloadSize = downloadInfo.downloadSize;
        entities.save();
    }

    /**
     * 数据库处理安装和卸载
     * @param packageName
     * @param state
     */
    public static void updateAppEntities(String packageName, int state) {
        List<AppEntities> entities = AppEntities.find(AppEntities.class, "package_name = '" + packageName + "'");
        if (entities !=null && entities.size() > 0) {
            AppEntities entity = entities.get(0);
            if (state == State.DOWNLOAD_NOT) {
                entity.delete();
                FileUtils.deleteApk(packageName);
            } else {
                entity.state = state;
                entity.save();
            }
            updateDownloadInfo(entity.appId, state);
        }
    }

    /**
     * 界面处理安装和卸载
     * @param appId
     * @param state
     */
    private static void updateDownloadInfo(long appId, int state) {
        DownloadInfo downloadInfo = DownloadManager.DOWNLOAD_CACHES.get(appId);
        downloadInfo.state = state;
        DownloadManager.notifyChangeUi(downloadInfo);
    }
}
