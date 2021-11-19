package com.example.rmanager.utilities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import com.example.rmanager.classes.Recording;

import java.io.File;
import java.util.ArrayList;

public class DeleteUtil {

    private ProgressDialog mProgressDialog;
    private AsyncTask asyncTaskDelete;
    private ArrayList<Recording> deletedRecordings;

    public void deleteAllSelectedRecordings(Context context, ArrayList<Recording> recordings){
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(recordings.size());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("Deleting");
        mProgressDialog.setMessage("Please wait...");
        deletedRecordings = recordings;
        asyncTaskDelete = new AsyncTaskDelete(context);
        asyncTaskDelete.execute(recordings.toArray(new Recording[recordings.size()]));
    }

    public static boolean delete(final Context context, final File file_delete) {
        final String pathone = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectedArgs = new String[] {
                file_delete.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri fileUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(fileUri, pathone, selectedArgs );

        if (file_delete.exists()) {
            contentResolver.delete(fileUri, pathone, selectedArgs );
        }
        if (file_delete.exists()){
            file_delete.delete();
        }
        return !file_delete.exists();
    }

    public class AsyncTaskDelete extends AsyncTask<Recording,Integer,ArrayList<Recording>> {

        Context context;

        public AsyncTaskDelete(Context context){
            this.context = context;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected ArrayList<Recording> doInBackground(Recording... recordings) {
            ArrayList<Recording> recordingList = new ArrayList<>();
            for (int i = 0; i < recordings.length; i++) {
                String file_delete = recordings[i].get_path();
                if (!delete(context, new File(file_delete))){
                    recordingList.add(recordings[i]);
                }
                publishProgress(i);
            }
            return recordingList;
        }

        @Override
        protected void onPostExecute(ArrayList<Recording> recordings) {
            super.onPostExecute(recordings);
            mProgressDialog.dismiss();

            for (Recording recording :
                    recordings) {
                deletedRecordings.remove(recording);
            }
        }

        protected void onPreExecute(){
            // Display the progress dialog on async task start
            mProgressDialog.show();
        }
    }
}
