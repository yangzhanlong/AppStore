package org.me.appstore.vm;

import org.me.appstore.Constants;
import org.me.appstore.MyApplication;
import org.me.appstore.utils.FileUtils;
import org.me.appstore.utils.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by user on 2018/3/18.
 */

public class DataCache {
    // 从内存中获取数据
    // 从本地文件中获取数据
    // 从网络获取数据（专门的流程进行处理）
    // 数据缓存：内存数据缓存、文件数据缓存


    /**
     * 从本地获取数据
     * @param key
     * @return
     */
    public static String getDataFromLocal(String key) {
        // 先从内存中获取，如果获取不到，从文件中获取
        String json = getDataFromMemory(key);
        if (json == null) {
            json = getDataFromFile(key);
        }
        return json;
    }

    /**
     * 从文件中获取数据
     *
     * @param key
     * @return
     */
    private static String getDataFromFile(String key) {
        String dataPath = FileUtils.getDir("json");
        File file = new File(dataPath, key);
        String json = null;

        if (file.exists()) {
            // 如何判断文件是否过时？
            // 创建文件的时间，当前系统时间，获取两者时间差，与设置好的过时时间进行比对
            // 约定文件的存储格式及内容：
            // 文件的第一行存放系统当前时间
            // 文件的第二行存放Json字符串
            try {
                FileReader reader = new FileReader(file);
                BufferedReader bufferReader = new BufferedReader(reader);
                String timeStr = bufferReader.readLine(); // 系统当前时间
                long cacheTime = Long.valueOf(timeStr);
                long currentTime  = System.currentTimeMillis();

                if (currentTime - cacheTime < Constants.DURATION) {
                    json = bufferReader.readLine();
                    // 需要缓存数据到内存
                    cacheMemory(key, json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    /**
     * 从内存中获取数据
     *
     * @param key
     * @return
     */
    private static String getDataFromMemory(String key) {
        return MyApplication.getDataCache().get(key);
    }

    /**
     * 将数据缓存到内存
     * @param key
     * @param json
     */
    public static void cacheMemory(String key, String json) {
        MyApplication.getDataCache().put(key, json);
    }


    /**
     * 缓存数据在文件中
     * @param key
     * @param json
     */
    public static void cacheFile(String key,String json) {
        String filepath = FileUtils.getDir("json");
        File file = new File(filepath, key);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(System.currentTimeMillis()+"");
            bufferedWriter.newLine();
            bufferedWriter.write(json);
            // 如果没有以下代码，无效
            bufferedWriter.flush();
            IOUtils.close(bufferedWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
