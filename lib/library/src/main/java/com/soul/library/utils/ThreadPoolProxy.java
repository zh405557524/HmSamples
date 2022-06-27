package com.soul.library.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 描述：创建线程池，提交任务，执行任务，取消任务
 * 作者：傅健
 * 创建时间：2016/5/25 11:05
 */
public class ThreadPoolProxy {
    private ThreadPoolExecutor mExecutor;            // 只需要一个
    private int mCorePoolSize;
    private int mMaximumPoolSize;
    private long mKeepAliveTime;

    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = maximumPoolSize;
        mKeepAliveTime = keepAliveTime;
    }


    // 初始化ThreadPoolExecutor
    private void initTThreadPoolExecutor() {// 双重间检查加锁

        if (mExecutor == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null) {
                    TimeUnit unit = TimeUnit.MICROSECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
                    ThreadFactory threadFactory = Executors
                            .defaultThreadFactory();
                    RejectedExecutionHandler handler = new DiscardPolicy();

                    mExecutor = new ThreadPoolExecutor(mCorePoolSize,// 核心线程数
                            mMaximumPoolSize,// 最大线程数
                            mKeepAliveTime, // 保持时间
                            unit, // 保持时间的单位
                            workQueue, // 工作队列
                            threadFactory, // 线程工厂
                            handler// 异常捕获器
                    );
                }
            }
        }
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        initTThreadPoolExecutor();
        mExecutor.execute(task);
    }

    /**
     * 提交任务
     */
    public Future<?> submit(Runnable task) {
        initTThreadPoolExecutor();
        return mExecutor.submit(task);
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        initTThreadPoolExecutor();
        mExecutor.getQueue().remove(task);
        /*if (mExecutor != null && (!mExecutor.isShutdown() || mExecutor.isTerminated())) {
            mExecutor.getQueue().remove(task);
        }*/
    }
}
