package com.soul.library.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述：创建普通线程池，创建下载线程池
 * 作者：傅健
 * 创建时间：2016/5/25 11:06
 */
public class ThreadFactory {
    private static ThreadPoolProxy mNormalPool;    // 只需要初始化一次
    private static ThreadPoolProxy mDownLoadPool;    // 只需要初始化一次
    private static ExecutorService mCachedThreadPool;    // 只需要初始化一次

    /**
     * 普通的线程池
     */
    public static ThreadPoolProxy getNormaPool() {

        if (mNormalPool == null) {
            synchronized (ThreadFactory.class) {
                if (mNormalPool == null) {
                    mNormalPool = new ThreadPoolProxy(5, 5, 3000);
                }
            }
        }
        return mNormalPool;
    }

    /**
     * 下载的线程池
     */
    public static ThreadPoolProxy getDownLoadPool() {

        if (mDownLoadPool == null) {
            synchronized (ThreadFactory.class) {
                if (mDownLoadPool == null) {
                    mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
                }
            }
        }
        return mDownLoadPool;
    }

    /**
     * 可缓存线程池
     */
    public static ExecutorService getCachedThreadPool() {

        if (mCachedThreadPool == null) {
            synchronized (ThreadFactory.class) {
                if (mCachedThreadPool == null) {
                    mCachedThreadPool = Executors.newCachedThreadPool();
                }
            }
        }
        return mCachedThreadPool;
    }
}