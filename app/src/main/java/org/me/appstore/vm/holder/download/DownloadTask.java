package org.me.appstore.vm.holder.download;

import org.me.appstore.Constants;
import org.me.appstore.module.db.AppEntities;
import org.me.appstore.utils.FileUtils;
import org.me.appstore.utils.HttpUtils;
import org.me.appstore.utils.IOUtils;
import org.me.appstore.utils.LogUtil;
import org.me.appstore.utils.ThreadPoolUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 2018/4/4.
 * 下载任务
 */

public class DownloadTask extends ThreadPoolUtils.Task {
    private DownloadInfo downloadInfo;
    public DownloadTask(DownloadInfo info) {
        super(info.appId);
        this.downloadInfo = info;
    }

    @Override
    public void work() {
        DownloadApk();
    }

    private void DownloadApk() {
        // http://localhost:8080/GooglePlayServer/download?name=&range=0
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", downloadInfo.downloadUrl);
        params.put("range", downloadInfo.downloadSize > 0 ? downloadInfo.downloadSize : 0);
        Request request = HttpUtils.getRequest(Constants.DOWNLOAD, params);
        Call call = HttpUtils.getClient().newCall(request);

        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                // 输入流
                inputStream = response.body().byteStream();

                // 下载到Sdcard的/data/android/包名/apk/packageName.apk
                File apk = FileUtils.getApk(downloadInfo.packageName);

                // 以追加的方式写文件
                outputStream = new FileOutputStream(apk, true);

                // inputStream 读取缓存空间
                byte[] buffer = new byte[1024];

                // inputStream 返回读取字节长度
                int len = 0;

                // 更新状态
                setState(State.DOWNLOADING);

                // 读取输入流写到文件中
                while ((len = inputStream.read(buffer)) != -1) { // 判断是否读到结束
                    // 如果是暂停状态，暂停下载
                    if (downloadInfo.state == State.DOWNLOAD_STOP) {
                        AppEntities.insertEntities(downloadInfo);
                        break;
                    }
                    outputStream.write(buffer, 0, len);
                    // 累积下载大小
                    downloadInfo.downloadSize += len;

                    LogUtil.s("downsize:" + downloadInfo.downloadSize);
                    LogUtil.s("size:" + downloadInfo.size);
                    if (downloadInfo.downloadSize == downloadInfo.size) {
                        setState(State.DOWNLOAD_COMPLETED);
                        break;
                    }
                }
            } else {
                // 重试
                setState(State.DOWNLOAD_ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setState(State.DOWNLOAD_ERROR);
        } finally {
            IOUtils.close(inputStream);
            IOUtils.close(outputStream);
        }
    }

    public void setState(int state) {
        downloadInfo.state = state;
        // 更新界面
        DownloadManager.getInstance().notifyChangeUi(downloadInfo);
        // 更新数据库
        AppEntities.insertEntities(downloadInfo);
    }
}