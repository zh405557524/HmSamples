package com.soul.library;

import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.global.resource.Resource;

public class Global {

    /**
     * 全局的Context
     */
    private static Context sContext;
    /**
     * 主线程
     */
    private static EventHandler sManinHandler;

    /**
     * 子线程 -工作线程，可以做不需要在主线程工作的任务，任务耗时不能超过5s
     */
    private static EventHandler sBgHandler;

    /**
     * 任务线程-可做耗时操作，文件数据库读取
     */
    private static EventHandler sHeavilyHandler;

    public static void init(Context context, EventHandler mainHandler) {
        sContext = context;
        sManinHandler = mainHandler;
        EventRunner bgEventRunner = EventRunner.create();
        sBgHandler = new EventHandler(bgEventRunner);

        EventRunner heavilyEventRunner = EventRunner.create();
        sHeavilyHandler = new EventHandler(heavilyEventRunner);
    }

    public static Context getContext() {
        return sContext;
    }

    /**
     * 得到一个主线程的handler
     */
    public static EventHandler getMainThreadHandler() {
        if (sManinHandler == null) {
            throw new RuntimeException("The Global must init first");
        }
        return sManinHandler;
    }

    /**
     * 得到一个子线程的handler
     */
    public static EventHandler getBackgroundHandler() {
        if (sBgHandler == null) {
            throw new RuntimeException("The Global must init first");
        }
        return sBgHandler;
    }

    /**
     * 得到一个子线程的handler
     */
    public static EventHandler getHeavilyHandler() {
        if (sHeavilyHandler == null) {
            throw new RuntimeException("The Global must init first");
        }
        return sHeavilyHandler;
    }

    public static String getString(int resourceId) {
        return sContext.getString(resourceId);
    }


    /**
     * 获取存储缓存位置
     *
     * @return 存储缓存位置
     */
    public static String getExternalCacheDir() {
        return sContext.getExternalFilesDir("cache").getPath();
    }

    /**
     * 获取资源文件的目录
     *
     * @return 资源文件的目录
     */
    public static String getRawFilePath() {
        return "entry/resources/rawfile/";
    }


}
