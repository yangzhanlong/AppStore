package org.me.appstore.vm.holder.download;

/**
 * Created by user on 2018/4/1.
 * 应用状态
 */

public interface State {
    int INSTALL_ALREADY = 0;
    int DOWNLOAD_NOT = 1;
    int DOWNLOAD_COMPLETED = 2;
    int DOWNLOAD_WAIT = 3;
    int DOWNLOAD_STOP = 4;
    int DOWNLOADING = 5;
    int DOWNLOAD_ERROR = -1;
}
