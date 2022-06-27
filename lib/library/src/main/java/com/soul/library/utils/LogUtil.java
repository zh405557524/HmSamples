package com.soul.library.utils;

import com.soul.library.module.log.LogManger;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class LogUtil {

    public static boolean sIsSaveLog;
    public static boolean sIsDebug;
    public static final HiLogLabel LABEL_debug = new HiLogLabel(HiLog.DEBUG, 0x00201, "MY_TAG");
    public static final HiLogLabel LABEL_info = new HiLogLabel(HiLog.INFO, 0x00201, "MY_TAG");
    public static final HiLogLabel LABEL_warn = new HiLogLabel(HiLog.ERROR, 0x00201, "MY_TAG");
    public static final HiLogLabel LABEL_error = new HiLogLabel(HiLog.WARN, 0x00201, "MY_TAG");


    public static void d(String msg) {
        if (sIsDebug) {
            HiLog.debug(LABEL_info, msg);
            saveLogin(msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sIsDebug) {
            HiLog.debug(new HiLogLabel(HiLog.DEBUG, 0x00201, tag), msg);
            saveLogin(tag + " " + msg);
        }
    }

    public static void i(String msg) {
        if (sIsDebug) {
            HiLog.info(LABEL_info, msg);
            saveLogin(msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sIsDebug) {
            HiLog.info(new HiLogLabel(HiLog.INFO, 0x00201, tag), msg);
            saveLogin(tag + " " + msg);
        }
    }

    public static void w(String msg) {
        HiLog.warn(LABEL_info, msg);
        saveLogin(msg);
    }

    public static void w(String tag, String msg) {
        if (sIsDebug) {
            HiLog.info(new HiLogLabel(HiLog.WARN, 0x00201, tag), msg);
            saveLogin(tag + " " + msg);
        }
    }

    public static void e(String msg) {
        if (sIsDebug) {
            HiLog.error(LABEL_info, msg);
            saveLogin(msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sIsDebug) {
            HiLog.info(new HiLogLabel(HiLog.ERROR, 0x00201, tag), msg);
            saveLogin(tag + " " + msg);
        }
    }

    private static void saveLogin(String msg) {
        if (sIsSaveLog) {
            //todo 保存日志
            LogManger.getInstance().saveLog(msg);
        }
    }

}
