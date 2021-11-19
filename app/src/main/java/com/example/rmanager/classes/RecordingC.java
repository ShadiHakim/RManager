package com.example.rmanager.classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class RecordingC implements Comparator<Recording> {
    @Override
    public int compare(Recording recording, Recording t1) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date strDate = null;
        Date strDate0 = null;
        try {
            strDate = sdf.parse(recording.get_date());
            strDate0 = sdf.parse(t1.get_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate.compareTo(strDate0) * -1;
    }
}
