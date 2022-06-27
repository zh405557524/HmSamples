package com.soul.library.utils;


import ohos.app.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FileHelp {


    public static boolean copyFile(String fromFile, String toFile) {
        try {
            InputStream is = new FileInputStream(fromFile);
            OutputStream os = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = is.read(bt)) > 0) {
                os.write(bt, 0, c);
            }
            is.close();
            os.close();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 从指定的文件中读取字符串
     *
     * @param file
     * @return
     */
    public static String readDataFromFile(File file) {

        BufferedReader br = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            InputStreamReader inputreader = new InputStreamReader(fis);
            br = new BufferedReader(inputreader);
            String line;
            StringBuffer content = new StringBuffer();
            //分行读取
            while ((line = br.readLine()) != null) {
                content.append(line + "\n");
            }

            return content.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (br != null) {
                    br.close();
                }

                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }


    /**
     * 获取指定文件夹、文件大小
     */
    public static long getFileSize(File f) {
        long size = 0L;
        File[] list = f.listFiles();
        if (list == null || list.length == 0) {
            return 0L;
        }
        for (File file : list) {
            if (file.isDirectory()) {
                size = size + getFileSize(file);
            } else {
                // 排除播放文件
                if (!file.getName().equalsIgnoreCase("cache.db") && !file.getName().equalsIgnoreCase("private_data.db")) {
                    size = size + file.length();
                }
            }
        }

        return size;
    }

    public static void DeleteAPKFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return;
            }
            for (File f : childFile) {
                if (f.getName().contains(".apk") || f.getName().contains(".patch")) {
                    DeleteAPKFile(f);
                }

            }
        }
    }


    /**
     * create parent dir by file path
     *
     * @param filePath file path
     * @return true mean create parent dir succeed
     */
    public static final boolean createFileParentDir(String filePath) {
        File file = new File(filePath);
        if (file != null) {
            if (file.exists()) {
                return true;// parent dir exist
            } else {
                File parentFile = file.getParentFile();
                if (parentFile != null) {
                    if (parentFile.exists()) {
                        return true;// parent dir exist
                    } else {
                        return parentFile.mkdirs();// create parent dir
                    }
                }
            }
        }
        return false;
    }

    /**
     * get file suffix by file path
     *
     * @param filePath file path
     * @return file suffix,return null means failed
     */
    public static String getFileSuffix(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            int start = filePath.lastIndexOf(".");
            if (start != -1) {
                return filePath.substring(start + 1);
            }
        }
        return null;
    }

    /**
     * whether the path is file path
     *
     * @param path file path
     * @return true means the path is file path
     */
    public static boolean isFilePath(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        return path.startsWith(File.separator);
    }


    /**
     * copy a file to another one
     *
     * @param fromFile
     * @param toFile
     * @param forceOverwrite
     * @return
     * @throws IOException
     */
    public static boolean copyFile(File fromFile, File toFile, boolean forceOverwrite) {

        if (fromFile == null || !fromFile.exists() || toFile == null) {
            return false;
        }

        if (toFile.exists() && !forceOverwrite) {
            return false;
        }

        try {
            InputStream fosFrom = new FileInputStream(fromFile);
            OutputStream fosTo = new FileOutputStream(toFile);
            byte bytes[] = new byte[1024];

            int writeSize = 0;
            int startIndex = 0;
            int readCount = 0;

            while ((readCount = fosFrom.read(bytes)) != -1) {
                fosTo.write(bytes, startIndex, readCount);
                writeSize += (readCount - startIndex);
            }
            fosFrom.close();
            fosTo.close();

            return writeSize > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * whether the file exist
     *
     * @param filePath the check file path
     * @return true means exist
     */
    public static boolean isFileExist(String filePath) {

        if (!isFilePath(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return file != null && file.exists() && file.isFile();

    }


    /**
     * @param msg      写入的消息
     * @param parent   父目录
     * @param filename 文件名
     * @param isAppend 是否是追加
     * @return 是否保存成功
     */
    public static boolean saveFile(String msg, String parent, String filename, boolean isAppend) {
        if (StringUtils.isEmpty(msg)) {
            return false;
        }
        File dir = new File(parent);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        PrintWriter pw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(dir + File.separator + filename, isAppend);
            pw = new PrintWriter(fw, true);
            pw.println(msg);
            return true;
        } catch (IOException e) {
            // Log.e(TAG, "saveFile: 发生异常");
            e.printStackTrace();
            return false;
        } finally {
            CloseUtils.closeIO(pw, fw);
        }
    }


    // 转换文件大小
    public static String formatSize(long size) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String fileSizeString = "";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "GB";
        }
        return fileSizeString;
    }

}
