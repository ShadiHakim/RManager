package com.example.rmanager.classes;


import java.io.File;
import java.util.Comparator;

public class RecordingFileC implements Comparator<File> {

    @Override
    public int compare(File file, File t1) {
        Long obj1 = (file.lastModified());
        Long obj2 = (t1.lastModified());
        return obj1.compareTo(obj2) * -1;
    }
}