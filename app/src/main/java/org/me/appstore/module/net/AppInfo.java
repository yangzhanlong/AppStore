package org.me.appstore.module.net;

import java.util.List;

/**
 * Created by user on 2018/3/12.
 * 应用信息封装
 */

public class AppInfo {

    /**
     * id : 1
     * name : 黑马程序员
     * packageName : com.itheima.www
     * iconUrl : app/com.itheima.www/icon.jpg
     * stars : 5
     * size : 91767
     * downloadUrl : app/com.itheima.www/com.itheima.www.apk
     * des : 产品介绍：google市场app测试。
     */

    public long id;
    public String name;
    public String packageName;
    public String iconUrl;
    public float stars;
    public long size;
    public String downloadUrl;
    public String des;
    // 详情信息
    public String downloadNum;
    public String version;
    public String date;
    public String author;

    public List<String> screen;
    public List<SafeInfo> safe;

    @Override
    public String toString() {
        return "AppInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
