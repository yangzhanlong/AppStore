package org.me.appstore.vm.holder.download;

import org.me.appstore.MyApplication;
import org.me.appstore.utils.ThreadPoolUtils;
import org.me.appstore.vm.holder.AppInfoHolder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2018/4/4.
 * 下载管理类
 */

public class DownloadManager {
    public static HashMap<Long, DownloadInfo> DOWNLOAD_CACHES = new HashMap<>();
    public static ArrayList<AppInfoHolder> APP_INFO_HOLDERS = new ArrayList<>();

    private static DownloadManager instance = new DownloadManager();
    public static DownloadManager getInstance() {
        return instance;
    }

    /**
     * 修改状态
     * @param downloadInfo
     */
    public static void notifyChangeUi(final DownloadInfo downloadInfo) {
        MyApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (AppInfoHolder holder : APP_INFO_HOLDERS) {
                    holder.update(downloadInfo);
                }
            }
        });
    }

    /**
     * 取消下载任务
     * @param appId
     */
    public static void removeQueueTask(long appId) {
        boolean cancelWaitTask = ThreadPoolUtils.cancelWaitTask(appId);
        if (cancelWaitTask) {
            DownloadInfo info = DOWNLOAD_CACHES.get(appId);
            info.state = State.DOWNLOAD_NOT;
            DownloadManager.getInstance().notifyChangeUi(info);
        }
    }
}
