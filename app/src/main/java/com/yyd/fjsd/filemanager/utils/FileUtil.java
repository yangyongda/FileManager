package com.yyd.fjsd.filemanager.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.bean.MyFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtil {

    public static String getInterPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    public static List<File> getFileList(String path) {
        List<File> list = new ArrayList<>();
        File file = new File(path);
        File[] allFile = file.listFiles();
        for (int i = 0; i < allFile.length; i++) {
            list.add(allFile[i]);
        }

        return list;
    }

    public static ArrayList<MyFile> FileToMyFile(List<File> files) {
        ArrayList<MyFile> myFiles = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String fileName = file.getName();
            String path = file.getAbsolutePath();

            MyFile myFile;
            if (file.isDirectory()) {
                myFile = new MyFile(R.drawable.folder, fileName, path, 0, 1);
            } else {
                long size = file.length();
                myFile = new MyFile(R.drawable.files, fileName, path, size, 0);
            }
            myFiles.add(myFile);
            Collections.sort(myFiles, new MyFileComparator());
        }

        return myFiles;
    }

    public static String sizeTransform(long size) {
        long point = 0; //小数点后的值
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            point = size % 1024; //小数点后的值
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            return String.valueOf(size) + "."
                    + String.valueOf((point * 10) / 1024) + "MB";
        } else {
            //否则如果要以GB为单位的，要保留最后两个小数
            point = size % 1024; //小数点后的值
            size = size / 1024;
            return String.valueOf(size) + "."
                    + String.valueOf(((point * 100) / 1024)) + "GB";
        }
    }

    public static void openFile(Context context, String file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(file)), MapTable.getMIMEType(file));
            context.startActivity(intent);
            Intent.createChooser(intent, "请选择对应的软件打开该附件！");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 拷贝成功返回true
     * */
    public static boolean copy(String fromFile, String toFile){
        //Log.v("yang", "fromPath = " + fromFile + " ,toPath = " + toFile);
        //要复制的文件目录
        File[] currentFiles = null;
        File root = new File(fromFile);
        //如果不存在则 return出去
        if (!root.exists()) {
            return false;
        }
        if(root.isDirectory()){
            currentFiles = root.listFiles();
        }

        //目标目录
        File targetDir = new File(toFile);
        //目录不存在则创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        //遍历要复制该目录下的全部文件
        if(currentFiles != null){
            for (int i = 0; i < currentFiles.length; i++) {
                //Log.v("yang", "length = " + currentFiles.length + "FileName = " + currentFiles[i].getName() + "i = " + i);
                if (currentFiles[i].isDirectory()) { //如果当前项为子目录 进行递归
                    copy(currentFiles[i].getPath() + "/", toFile + File.separator + currentFiles[i].getName());

                } else {//如果当前项为文件则进行文件拷贝
                    if(!copyFile(currentFiles[i].getPath(), toFile + File.separator + currentFiles[i].getName())){
                        return false;
                    }

                }
            }
        }else {
            if(!copyFile(fromFile, toFile + File.separator + new File(fromFile).getName())){
                return false;
            }
        }
        return true;
    }

    private static boolean copyFile(String fromFile, String toFile){
        //Log.v("yang", "fromPath = " + fromFile + "toPath = " + toFile);
        try {
            InputStream fisfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte data[] = new byte[1024];
            int num;
            while ((num = fisfrom.read(data)) > 0) {
                fosto.write(data, 0, num);
            }
            fosto.flush();
            fisfrom.close();
            fosto.close();
            return true;
        }catch (IOException e){
            //e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFiles(String file){
        File deleteFile = new File(file);

        //如果不存在则 return出去
        if (!deleteFile.exists()) {
            return false;
        }

        if(deleteFile.isDirectory()){
            String[] children = deleteFile.list();
            //递归删除
            for (int i=0; i<children.length; i++) {
                boolean success = deleteFiles(file + File.separator + children[i]);
                if (!success) {
                    return false;
                }
            }
        }
        if(deleteFile.delete()){
            return true;
        }else{
            return false;
        }
    }

    public static boolean renameFile(String oldName, String newName){
        File oldFile = new File(oldName);
        File newFile = new File(newName);
        //检查是否当前目录下有相同名字的文件
        List<File> checkFiles = getFileList(MyApplication.getInstance().currPath);
        for(int i = 0; i < checkFiles.size(); i++){
            if(checkFiles.get(i).getName().equals(newName)){
                return false;
            }
        }
        if(!oldFile.renameTo(newFile)){
            return false;
        }
        return true;
    }

    public static boolean newFolder(String name){
        File folder = new File(name);
        //检查是否当前目录下有相同名字的文件
        List<File> checkFiles = getFileList(MyApplication.getInstance().currPath);
        for(int i = 0; i < checkFiles.size(); i++){
            if(checkFiles.get(i).getName().equals(name)){
                return false;
            }
        }
        if(!folder.mkdir()){
            return false;
        }
        return true;

    }

    public static long getTotalStorageSpace(){
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        //long blockSize = stat.getBlockSizeLong();
        //long totalBlocks = stat.getBlockCountLong();
        return stat.getTotalBytes();
    }

    public static long getAvailableSpace(){
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        return stat.getAvailableBytes();
    }

}

