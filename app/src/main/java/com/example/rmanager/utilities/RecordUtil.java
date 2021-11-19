package com.example.rmanager.utilities;

import android.content.Context;
import android.content.UriPermission;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.example.rmanager.classes.Recording;
import com.example.rmanager.classes.RecordingFileC;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RecordUtil {

    public static ArrayList<Recording> getRecordings(Context context, String folderPath) {
//        String[] externalStoragePaths = StorageUtil.getStorageDirectories(context);
//        File dir;
//        if (loc == -1) {
//            for (String sPath :
//                    externalStoragePaths) {
//                dir = new File(sPath + folderPath);
//                if (dir.exists())
//                    return getRecordings(dir);
//            }
//        }
//        else{
//            dir = new File(externalStoragePaths[0] + folderPath);
//            if (dir.exists())
//                return getRecordings(dir);
//        }
        List<UriPermission> permissionUris = context.getContentResolver().getPersistedUriPermissions();

        for (UriPermission permissionUri : permissionUris) {

            Uri treeUri = permissionUri.getUri();
            File main_dir = new File(getAbsolutePathFromUri(treeUri, context) + "/Call");
            File saved_dir = new File(getAbsolutePathFromUri(treeUri, context) + "/Call/SavedCall");

            if (folderPath.equals("Call")){
                return getRecordings(main_dir, false);
            }
            if (folderPath.equals("SavedCall"))
            {
                if (!saved_dir.exists()){
                    saved_dir.mkdir();
                }
                return getRecordings(saved_dir, true);
            }
        }
        return new ArrayList<Recording>();
    }

    private static ArrayList<Recording> getRecordings(File dir, boolean is_saved) {
        List<File> list = Arrays.asList(dir.listFiles() == null? new File[0]:dir.listFiles());
        Collections.sort(list,new RecordingFileC());
        ArrayList<Recording> recordings = new ArrayList<>();
        for (File file : list) {
            if (file.isFile())
                recordings.add(fileToRecord(file, is_saved));
        }
        return recordings;
    }

    public static Recording fileToRecord(File file, boolean is_saved){
        Recording recording = new Recording();
        recording.set_path(file.getAbsolutePath());
        recording.set_file_name(file.getName());
        recording.set_contactName(file.getName().substring(file.getName().indexOf(' ', file.getName().indexOf(' ') + 1),file.getName().indexOf('_')));
        recording.set_date(millisecondsToDate(file.lastModified()));
        recording.set_saved(is_saved);
        return recording;
    }

    public static String millisecondsToDate(long m){
        DateFormat simple = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date result = new Date(m);
        return simple.format(result);
    }

    public static String getAbsolutePathFromUri(Uri uri, Context context){
        String relativePath = uri.getPath().replaceFirst("/tree/", "").replaceFirst(":Call", "");
        String[] externalStoragePaths = StorageUtil.getStorageDirectories(context);
        if (externalStoragePaths.length == 1)
            return externalStoragePaths[0];
        if (relativePath.equals("primary")){
            for (String path :
                    externalStoragePaths) {
                if (path.contains("emulated")){
                    return path;
                }
            }
        }
        for (String path :
                externalStoragePaths) {
            if (path.contains(relativePath)){
                return path;
            }
        }
        return uri.getPath().replaceFirst("/tree/", "/storage/").replaceFirst(":Call", "/Call");
    }

    public static File getFile(String file_name, String parent, Context con){
        List<UriPermission> permissionUris = con.getContentResolver().getPersistedUriPermissions();

        for (UriPermission permissionUri : permissionUris) {

            Uri treeUri = permissionUri.getUri();
            String abs_path = getAbsolutePathFromUri(treeUri, con);

            return new File(abs_path + "/Call" + (parent.equals("SavedCall")?"/SavedCall":"") + "/" + file_name);
        }
        return null;
    }
}
