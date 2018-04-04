package org.me.appstore.vm.holder.download;

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
}
