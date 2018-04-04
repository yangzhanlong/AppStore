package org.me.appstore.utils;

import org.me.appstore.MyApplication;

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by user on 2018/4/1.
 * 线程池管理工具类
 */

public class ThreadPoolUtils {
    // 1. 创建可以执行3个下载任务的线程池
    // 增加：新任务执行，没有达到上限就可以增加
    // 减少：执行完任务，进行减少
    // 2. 如果下载任务大于3个，放入等待队列 (先进先出)

    // 队列选择：LinkedList
    // 获取头元素: pollFirst() 获取并移除列表的第一个元素，如果此列表为空，返回null
    // 添加元素：addLast()

    private static final int THREADS = 3; // 线程池的大小
    private static ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADS); // 线程池

    private static int count = 0; // 计数

    // 等待队列
    private static LinkedList<Task> TASKS = new LinkedList<>();

    /**
     * 执行任务
     * @param task
     * @return 返回是否执行任务成功
     */
    public static boolean execute(Task task) {
        if (count < 3) {
            poolExecutor.execute(task);
            count ++;
            return true;
        } else {
            // 放入等待对列
            TASKS.addLast(task);
        }
        return false;
    }

    /**
     * 自定义的任务
     */
    public static abstract class Task implements Runnable {

        @Override
        public void run() {
            // 耗时操作
            work();
            // 执行完后，减少计数，从等待队列中获取新任务,执行新任务
            MyApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    count--;
                    Task task = TASKS.pollFirst();
                    if (task != null) {
                        execute(task);
                    }
                }
            });
        }

        public abstract void work();
    }
}
