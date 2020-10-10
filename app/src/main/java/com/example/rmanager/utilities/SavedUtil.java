package com.example.rmanager.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void copy(File copy, String newfile, Context con) {
        FileInputStream inStream = null;
        OutputStream outStream = null;
        DocumentFile dir= getDocumentFileIfAllowedToWrite(new File(newfile).getParentFile(), con);
        String mime = mime(copy.toURI().toString()) != null? mime(copy.toURI().toString()) : "audio/mpeg";
        DocumentFile copy1= dir.createFile(mime, copy.getName());
        try {
            inStream = new FileInputStream(copy);
            outStream = con.getContentResolver().openOutputStream(copy1.getUri());
            byte[] buffer = new byte[16384];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                inStream.close();

                outStream.close();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        preserveDate(copy,new File(newfile));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static DocumentFile getDocumentFileIfAllowedToWrite(File file, Context con) {

        String[] externalStoragePaths = StorageUtil.getStorageDirectories(con);

        List<UriPermission> permissionUris = con.getContentResolver().getPersistedUriPermissions();

        for (UriPermission permissionUri : permissionUris) {

            Uri treeUri = permissionUri.getUri();
            DocumentFile rootDocFile = DocumentFile.fromTreeUri(con, treeUri);
            String rootDocFilePath = externalStoragePaths[0];

            if (file.getAbsolutePath().startsWith(rootDocFilePath)) {

                ArrayList<String> pathInRootDocParts = new ArrayList<String>();
                while (!rootDocFilePath.equals(file.getAbsolutePath() + "/")) {
                    pathInRootDocParts.add(file.getName());
                    file = file.getParentFile();
                }

                DocumentFile docFile = null;

                if (pathInRootDocParts.size() == 0) {
                    docFile = DocumentFile.fromTreeUri(con, rootDocFile.getUri());
                } else {
                    for (int i = pathInRootDocParts.size() - 1; i >= 0; i--) {
                        if (docFile == null) {
                            docFile = rootDocFile.findFile(pathInRootDocParts.get(i));
                            if (docFile == null){
                                DocumentFile.fromTreeUri(con, rootDocFile.getUri()).createDirectory(pathInRootDocParts.get(i));
                                docFile = rootDocFile.findFile(pathInRootDocParts.get(i));
                            }

                        } else {
                            docFile = docFile.findFile(pathInRootDocParts.get(i));
                        }
                    }
                }
                if (docFile != null && docFile.canWrite()) {
                    return docFile;
                } else {
                    return null;
                }

            }
        }
        return null;
    }

    public static String mime(String URI) {
        String type = null;
        String extention = MimeTypeMap.getFileExtensionFromUrl(URI);
        if (extention != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention);
        }
        return type;
    }


    public static void preserveDate(File src, File dst){
        long modified = src.lastModified();
        dst.setLastModified(modified);
    }


}
