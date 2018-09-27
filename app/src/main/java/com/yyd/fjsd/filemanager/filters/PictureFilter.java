package com.yyd.fjsd.filemanager.filters;

import java.io.File;
import java.io.FileFilter;

public class PictureFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        String name = file.getName();
        if(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif")){
            return true;
        }
        return false;
    }
}
