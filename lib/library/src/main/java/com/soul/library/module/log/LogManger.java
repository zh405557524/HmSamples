package com.soul.library.module.log;


import com.soul.library.utils.FileHelp;
import com.soul.library.utils.StringUtils;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Description:log日志管理类
 * Author: 祝明
 * CreateDate: 2020/1/9 14:26
 * UpdateUser:
 * UpdateDate: 2020/1/9 14:26
 * UpdateRemark:
 */
public class LogManger implements ILogManger {


    private static final String TAG = LogManger.class.getSimpleName();
    private static LogManger sLogManger;

    private static EventHandler sLogThread;

    /**
     * log文件保存得地址
     */
    private static String sLogSavePath;

    /**
     * log调式日志是否保存
     */
    private static boolean sIsSaveLog;


    /**
     * 更新日志
     */
    private static final int UPLOAD_LOG = 0;


    /**
     * 保存日志
     */
    private static final int SAVE_LOG = 1;

    /**
     * 日志保存间隔时间
     */
    private static final int SAVE_TIME = 10 * 1000;

    /**
     * 最大缓存大小
     */
    private static final int MAX_CACHE_SIZE = 1024 * 100;

    /**
     * log日志内容缓存
     */
    private static StringBuilder sb;

    /**
     * handler
     */
    private final EventHandler mHandler;


    public static ILogManger getInstance() {
        if (sLogManger == null) {
            synchronized (LogManger.class) {
                if (sLogManger == null) {
                    sLogManger = new LogManger();
                }
            }
        }
        return sLogManger;
    }

    private LogManger() {
        EventRunner logEventRunner = EventRunner.create();
        mHandler = new EventHandler(logEventRunner) {
            @Override
            protected void processEvent(InnerEvent event) {
                LogManger.this.handleMessage(event);
            }
        };
    }

    /**
     * 初始化log管理
     *
     * @param logPath log文件保存地址
     */
    @Override
    public void init(String logPath) {
        sLogSavePath = logPath;
    }

    /**
     * 设置配置
     *
     * @param isSaveLog 是否保存日志
     */
    @Override
    public void setConfig(boolean isSaveLog) {
        sIsSaveLog = isSaveLog;
        if (isSaveLog) {//需要保存日志
            mHandler.sendEvent(createMessage(SAVE_LOG), SAVE_TIME);
        } else {
            mHandler.removeEvent(SAVE_LOG);
        }
    }

    /**
     * 保存日志到本地
     *
     * @param msg 日志内容
     */
    @Override
    public void saveLog(String msg) {
        mHandler.sendEvent(createMessage(UPLOAD_LOG, msg));
    }

    private InnerEvent createMessage(int what) {
        return createMessage(what, null);
    }

    private InnerEvent createMessage(int what, Object object) {
        InnerEvent innerEvent = InnerEvent.get();
        innerEvent.eventId = what;
        innerEvent.object = object;
        return innerEvent;
    }


    private void handleMessage(InnerEvent msg) {
        switch (msg.eventId) {
            case UPLOAD_LOG://更新日志信息
                printUploadlog((String) msg.object);
                break;
            case SAVE_LOG://保存日志
                if (sb != null) {
                    writeFileToSD(sLogSavePath, sb.toString());
                    sb.delete(0, sb.length());
                }
                mHandler.removeEvent(SAVE_LOG);
                mHandler.sendTimingEvent(createMessage(SAVE_LOG), SAVE_TIME);
                break;
        }
    }

    /**
     * log日志保存到本地
     *
     * @param msg
     */
    private void printUploadlog(String msg) {
        if (!sIsSaveLog || StringUtils.isEmpty(sLogSavePath)) {
            return;
        }
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append(getCurTime()).append("_ ").append(msg).append("\n");

        if (sb.length() > MAX_CACHE_SIZE) {
            mHandler.sendEvent(createMessage(SAVE_LOG));
        }
    }

    private boolean writeFileToSD(String path, String logstr) {
        deleteFile(path);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] filelist = file.listFiles();
        int size;
        if (filelist == null) {
            size = 0;
        } else {
            size = filelist.length;
        }
        try {
            if (size <= 0) {
                String fileName = CreateSysTimeFileName();
                return FileHelp.saveFile(logstr, path, fileName, false);
            } else {
                for (int i = 0; i < size; i++) {
                    if (filelist[i].length() < 1024 * 1024) {
                        return FileHelp.saveFile(logstr, path, filelist[i].getName(), true);
                    }
                }
                String fileName = CreateSysTimeFileName();
                return FileHelp.saveFile(logstr, path, fileName, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private static String CreateSysTimeFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate) + ".txt";
    }

    /**
     * 获取特定格式的时间
     */
    private static String getCurTime() {
        String curTime = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd/ HH:mm:ss");
            curTime = "[" + formatter.format(System.currentTimeMillis()) + "] ";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curTime;
    }


    private void deleteFile(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null || file.length() <= 0) {
            HiLog.error((new HiLogLabel(HiLog.WARN, 0x00201, "error")), TAG + "空目录");
            return;
        }
        ArrayList<File> fileList = new ArrayList<File>();//将需要的子文件信息存入到FileInfo里面
        fileList.addAll(Arrays.asList(files));
        Collections.sort(fileList, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
        if (fileList.size() > 9) {
            fileList.get(0).delete();
        }
    }


}
