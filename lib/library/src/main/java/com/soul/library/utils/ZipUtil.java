package com.soul.library.utils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.global.resource.Entry;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ZipUtil {

    /**
     * 解压内部资源文件
     *
     * @param abilitySlice   资源对象
     * @param zipFilePath    资源文件地址
     * @param targetFilesDir 指定复制的目录
     */
    public static void unZip(AbilitySlice abilitySlice, String zipFilePath, File targetFilesDir) {
        try {
            RawFileEntry rawFileEntry = abilitySlice.getResourceManager().getRawFileEntry(zipFilePath);
            Entry.Type type = rawFileEntry.getType();
            if (type == Entry.Type.FOLDER) {
                if (!targetFilesDir.exists()) {
                    //不存在则创建目录
                    boolean mkdirs = targetFilesDir.mkdirs();
                    LogUtil.i("mkdirs:" + mkdirs);
                }
                if (targetFilesDir.exists()) {
                    LogUtil.i("文件夹已存在:" + targetFilesDir);
                }
                //文件夹
                Entry[] entries = rawFileEntry.getEntries();
                for (Entry entry : entries) {
                    String path = zipFilePath + "/" + entry.getPath();
                    unZip(abilitySlice, path, new File(targetFilesDir, entry.getPath()));
                }
            } else {
                LogUtil.i("zipFilePath:" + zipFilePath);
                LogUtil.i("targetFilesDir:" + targetFilesDir);
                //复制到目的地
                Resource resource = rawFileEntry.openRawFile();
                cpoyResourceFile(resource, targetFilesDir);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 复制资源文件到本地
     *
     * @param resource       资源
     * @param targetFilesDir 本地地址
     */
    private static void cpoyResourceFile(Resource resource, File targetFilesDir) {
        if (resource == null) {
            return;
        }
        FileOutputStream fileOutputStream = null;
        try {
            byte[] bytes = new byte[resource.available()];
            fileOutputStream = new FileOutputStream(targetFilesDir);
            while ((resource.read(bytes)) != -1) {
                fileOutputStream.write(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
