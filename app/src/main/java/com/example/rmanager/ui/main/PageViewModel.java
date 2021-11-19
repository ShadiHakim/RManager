package com.example.rmanager.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rmanager.classes.Recording;
import com.example.rmanager.classes.RecordingC;

import java.util.ArrayList;
import java.util.Collections;

public class PageViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Recording>> mAllRecords = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Recording>> mSavedRecords = new MutableLiveData<>();
    public LiveData<ArrayList<Recording>> getRecordings(int x){
        switch (x){
            case 1:
                return mAllRecords;
            case 2:
                return mSavedRecords;
            default:
                return null;
        }
    }
    public void init(ArrayList<Recording> allRecordings, ArrayList<Recording> savedRecordings){
        mAllRecords.setValue(allRecordings);
        mSavedRecords.setValue(savedRecordings);
        mSAllRecords.setValue(new ArrayList<Recording>());
        mSSavedRecords.setValue(new ArrayList<Recording>());
    }

    public void updateAllRecords(Recording recording) {
        if (recording.is_saved()){
            recording.set_saved(!recording.is_saved());
            ArrayList<Recording> updatedAllRecordings = mAllRecords.getValue();
            int index = recording.getIndexOfRecording(updatedAllRecordings);
            recording.set_saved(!recording.is_saved());
            updatedAllRecordings.remove(index);
            mAllRecords.postValue(updatedAllRecordings);
        }
        else {
            ArrayList<Recording> updatedAllRecordings = mAllRecords.getValue();
            updatedAllRecordings.add(recording);
            Collections.sort(updatedAllRecordings, new RecordingC());
            mAllRecords.postValue(updatedAllRecordings);
        }
    }

    public void updateSavedRecords(Recording recording) {
        if (recording.is_saved()){
            ArrayList<Recording> updatedSavedRecordings = mSavedRecords.getValue();
            updatedSavedRecordings.add(recording);
            Collections.sort(updatedSavedRecordings,new RecordingC());
            mSavedRecords.postValue(updatedSavedRecordings);
        }
        else {
            recording.set_saved(!recording.is_saved());
            ArrayList<Recording> updatedSavedRecordings = mSavedRecords.getValue();
            int index = recording.getIndexOfRecording(updatedSavedRecordings);
            recording.set_saved(!recording.is_saved());
            updatedSavedRecordings.remove(index);
            mSavedRecords.postValue(updatedSavedRecordings);
        }
    }

    public void removeRecordings(Recording dRecording){
        if (dRecording.is_saved()){
            ArrayList<Recording> updatedSavedRecordings = mSavedRecords.getValue();
            int index = dRecording.getIndexOfRecording(updatedSavedRecordings);
            updatedSavedRecordings.remove(index);
            mSavedRecords.postValue(updatedSavedRecordings);
        }
        ArrayList<Recording> updatedAllRecordings = mAllRecords.getValue();
        int index = dRecording.getIndexOfRecording(updatedAllRecordings);
        if (index != -1){
            updatedAllRecordings.remove(index);
            mAllRecords.postValue(updatedAllRecordings);
        }
    }

    private MutableLiveData<ArrayList<Recording>> mSAllRecords = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Recording>> mSSavedRecords = new MutableLiveData<>();
    public LiveData<ArrayList<Recording>> getSelectedRecordings(int x){
        switch (x){
            case 1:
                return mSAllRecords;
            case 2:
                return mSSavedRecords;
            default:
                return null;
        }
    }

    public void addSAllRecords(Recording dAllRecording) {
        ArrayList<Recording> updated_mDAllRecords = mSAllRecords.getValue();
        updated_mDAllRecords.add(dAllRecording);
        mSAllRecords.postValue(updated_mDAllRecords);
    }

    public void addSSavedRecords(Recording dSavedRecording) {
        ArrayList<Recording> updated_mDSavedRecords = mSSavedRecords.getValue();
        updated_mDSavedRecords.add(dSavedRecording);
        mSSavedRecords.postValue(updated_mDSavedRecords);
    }

    public void removeSAllRecords(Recording dAllRecording) {
        ArrayList<Recording> updated_mDAllRecords = mSAllRecords.getValue();
        updated_mDAllRecords.remove(dAllRecording);
        mSAllRecords.postValue(updated_mDAllRecords);
    }

    public void removeSSavedRecords(Recording dSavedRecording) {
        ArrayList<Recording> updated_mDSavedRecords = mSSavedRecords.getValue();
        updated_mDSavedRecords.remove(dSavedRecording);
        mSSavedRecords.postValue(updated_mDSavedRecords);
    }

    public void clearSAllRecords(){
        ArrayList<Recording> clear_mDAllRecords = mSAllRecords.getValue();
        clear_mDAllRecords.clear();
        mSAllRecords.postValue(clear_mDAllRecords);
    }

    public void clearSSavedRecords(){
        ArrayList<Recording> clear_mDSavedRecords = mSSavedRecords.getValue();
        clear_mDSavedRecords.clear();
        mSSavedRecords.postValue(clear_mDSavedRecords);
    }

    public boolean IsEmptySAllRecords(){
        return mSAllRecords.getValue().isEmpty();
    }

    public boolean IsEmptySSavedRecords(){
        return mSSavedRecords.getValue().isEmpty();
    }
}