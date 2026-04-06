package com.example.bladder_frontend.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import java.io.File;
import java.util.Locale;

public class StorageUtils {

    public static long getFolderSize(File file) {
        long size = 0;
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        size += getFolderSize(f);
                    } else {
                        size += f.length();
                    }
                }
            }
        } else if (file.exists()) {
            size = file.length();
        }
        return size;
    }

    public static String formatSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format(Locale.getDefault(), "%.1f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    public static long getCacheSize(Context context) {
        return getFolderSize(context.getCacheDir());
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    public static long getUsedInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return (totalBlocks - availableBlocks) * blockSize;
    }
    
    public static boolean clearCache(Context context) {
        try {
            File dir = context.getCacheDir();
            return deleteDir(dir);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
