package com.yyd.fjsd.filemanager.utils;

import java.io.File;
import java.io.FileFilter;

public class NoMediaFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        if(file.getName().equals(".nomedia")){
            return true;
        }
        return false;
    }
}
