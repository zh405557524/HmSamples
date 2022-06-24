package com.soul.library;

import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

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
    private static EventHandler bgHandler;

    /**
     * 任务线程-可做耗时操作，文件数据库读取
     */
    private static EventHandler heavilyHandler;

    public static void init(Context context, EventHandler mainHandler) {
        sContext = context;
        sManinHandler = mainHandler;
        EventRunner bgEventRunner = EventRunner.create();
        bgHandler = new EventHandler(bgEventRunner);

        EventRunner heavilyEventRunner = EventRunner.create();
        heavilyHandler = new EventHandler(heavilyEventRunner);
    }

    public static Context getContext() {
        return sContext;
    }
}
