package org.me.appstore.vm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.me.appstore.module.db.AppEntities;
import org.me.appstore.vm.holder.download.State;

/**
 * Created by user on 2018/4/9.
 * 监听安装与卸载 Receiver
 */

public class AppReceiver extends BroadcastReceiver{
    private static final int PACKAGE_NAME_START_INDEX = 8;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        String dataString = intent.getDataString();
        String packageName = dataString.substring(PACKAGE_NAME_START_INDEX);
        // 安装完成广播
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            AppEntities.updateAppEntities(packageName, State.INSTALL_ALREADY);
        }

        // 卸载完成广播
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            AppEntities.updateAppEntities(packageName, State.DOWNLOAD_NOT);
        }
    }
}
