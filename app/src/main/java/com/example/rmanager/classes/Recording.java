package com.example.rmanager.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class Recording {
    private String _location;
    private String _contactName;
    private String _date;
    private boolean _saved;

    public Recording(){}

    public String get_location() {
        return _location;
    }

    public void set_location(String _location) {
        this._location = _location;
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


    public boolean checkIfSaved(ArrayList<Recording> savedRecordings) {
        for (Recording saved :
                savedRecordings) {
            if (this._location.equals(saved.get_location())) {
                return true;
            }
        }
        return false;
    }

    public int getIndexOfRecording(ArrayList<Recording> recordings){
        for (int i = 0; i < recordings.size(); i++) {
            String recording0 = recordings.get(i).get_location().substring(recordings.get(i).get_location().lastIndexOf("/") + 1);
            String recording1 = this.get_location().substring(this.get_location().lastIndexOf("/") + 1);
            if (recording0.equals(recording1)){
                return i;
            }
        }
        return -1;
    }
}

