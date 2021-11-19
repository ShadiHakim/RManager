package com.example.rmanager.utilities;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class SavedUtil {

    public static void copyFile(File sourceFile, File destFile) throws IOException {

        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
        preserveDate(sourceFile,destFile);
    }

    public static void preserveDate(File src, File dst){
        long modified = src.lastModified();
        dst.setLastModified(modified);
    }

    public static Boolean move(String file_name, String cur_loc, String new_loc, Context con) {
        File src = RecordUtil.getFile(file_name,cur_loc,con);
        File dst = RecordUtil.getFile(file_name,new_loc,con);
        try {
            copyFile(src,dst);
            if (!DeleteUtil.delete(con, src)){ return false; }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}