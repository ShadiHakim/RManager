package com.example.rmanager.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.rmanager.classes.Recording;

import java.io.File;
import java.util.ArrayList;

public class DeleteUtil {

    public static void deleteAllSelectedRecordings(Context context, ArrayList<Recording> recordings){
        for (Recording recording :
                recordings) {
            File fdelete = new File(recording.get_location());
            if (!delete(context,fdelete)){
                recordings.remove(recording);
            }
        }
    }

    public static boolean delete(final Context context, final File file) {
        final String pathone = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectedArgs = new String[] {
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri fileUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(fileUri, pathone, selectedArgs );

        if (file.exists()) {
            contentResolver.delete(fileUri, pathone, selectedArgs );
        }
        if (file.exists()){
            file.delete();
        }
        return !file.exists();
    }
}
