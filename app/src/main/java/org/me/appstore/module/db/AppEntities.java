package org.me.appstore.module.db;

import com.orm.SugarRecord;

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
}
