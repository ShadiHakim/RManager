package com.example.rmanager.classes;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class Recording {
    private String _path;
    private String _file_name;
    private String _contactName;
    private String _date;
    private boolean _saved;

    public Recording(){}

    public String get_path() {
        return _path;
    }

    public void set_path(String _path) {
        this._path = _path;
    }

    public String get_file_name() {
        return _file_name;
    }

    public void set_file_name(String _file_name) {
        this._file_name = _file_name;
    }

    public String get_contactName() {
        return _contactName;
    }

    public void set_contactName(String _contactName) {
        this._contactName = _contactName;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public boolean is_saved() {
        return _saved;
    }

    public void set_saved(boolean _saved) {
        this._saved = _saved;
    }

    public boolean isSelected(ArrayList<Recording> selectedRecordings){
        for (Recording saved :
                selectedRecordings) {
            if (this._file_name.equals(saved.get_file_name())) {
                return true;
            }
        }
        return false;
    }

    public int getIndexOfRecording(ArrayList<Recording> recordings){
        for (int i = 0; i < recordings.size(); i++) {
            String recording0 = recordings.get(i).get_file_name();
            String recording1 = this.get_file_name();
            if (recording0.equals(recording1)){
                return i;
            }
        }
        return -1;
    }
}

