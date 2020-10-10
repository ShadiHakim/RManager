package com.example.rmanager.utilities;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;

import com.example.rmanager.classes.Recording;
import com.example.rmanager.classes.RecordingFileC;
import com.example.rmanager.save.DBManager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class RecordUtil {

    public static ArrayList<Recording> initializeRecordings(Context context, String folderPath, int loc, ArrayList<Recording> savedRecordings) {
        String[] externalStoragePaths = StorageUtil.getStorageDirectories(context);
        File dir;
        if (loc == -1) {
            for (String sPath :
                    externalStoragePaths) {
                dir = new File(sPath + folderPath);
                if (dir.exists())
                    return getRecordings(dir, savedRecordings);
            }
        }
        else{
            dir = new File(externalStoragePaths[loc] + folderPath);
            if (dir.exists())
                return getRecordings(dir, savedRecordings);
        }
        return new ArrayList<Recording>();
    }

    private static ArrayList<Recording> getRecordings(File dir, ArrayList<Recording> savedRecordings) {
        List<File> list = Arrays.asList(dir.listFiles());
        Collections.sort(list,new RecordingFileC());
        ArrayList<Recording> recordings = new ArrayList<>();
        for (File file : list) {
            recordings.add(fileToRecord(file, savedRecordings));
        }
        return recordings;
    }

    public static Recording fileToRecord(File file, ArrayList<Recording> savedRecordings){
        Recording recording = new Recording();
        recording.set_location(file.getAbsolutePath());
        recording.set_contactName(file.getName().substring(file.getName().indexOf(' ', file.getName().indexOf(' ') + 1),file.getName().indexOf('_')));
        recording.set_date(millisecondsToDate(file.lastModified()));
        recording.set_saved(recording.checkIfSaved(savedRecordings));
        return recording;
    }

    public static String millisecondsToDate(long m){
        DateFormat simple = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date result = new Date(m);
        return simple.format(result);
    }

    public static ArrayList<Recording> getSavedRecordings(Context context){
        ArrayList<Recording> recordings = new ArrayList<>();
        DBManager dbManager = new DBManager(context);
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Recording recording = new Recording();
                    recording.set_location(cursor.getString(cursor.getColumnIndex("_location")));
                    recording.set_contactName(cursor.getString(cursor.getColumnIndex("_contactName")));
                    recording.set_date(cursor.getString(cursor.getColumnIndex("_date")));
                    recording.set_saved(cursor.getString(cursor.getColumnIndex("_saved")).equals("1"));
                    recordings.add(recording);
                } while (cursor.moveToNext());
            }
        }
        Collections.sort(recordings, new Comparator<Recording>() {

            @Override
            public int compare(Recording r1, Recording r2) {
                return r1.get_date().compareTo(r2.get_date()) * -1;
            }
        });
        return recordings;
    }

    public static String getNewPathDirectory(Recording recording, String from, String to){
        return recording.get_location().replaceFirst("/"+ from +"/","/"+ to +"/");
    }
}
