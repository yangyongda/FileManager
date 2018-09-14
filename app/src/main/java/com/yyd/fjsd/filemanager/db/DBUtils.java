package com.yyd.fjsd.filemanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/11/7.
 */

public class DBUtils {
    private Context mContext;
    private DBOpenHelper mDBOpenHelper;

    public DBUtils(Context context){
        mContext = context;
        mDBOpenHelper = new DBOpenHelper(context);
    }
    //插入数据
    public long insert(int id, String dot){
        //获取数据库对象
        SQLiteDatabase mSQLiteDatabase = mDBOpenHelper.getWritableDatabase();
        //填充数据字段
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id", id);
        contentValues.put("dot", dot);

        //插入数据
        long row = mSQLiteDatabase.insert(DBOpenHelper.TABLE_NAME, null, contentValues);

        return row;
    }

    public void delete(int id){
        SQLiteDatabase mSQLiteDatabase = mDBOpenHelper.getWritableDatabase();
        mSQLiteDatabase.delete(DBOpenHelper.TABLE_NAME, "Id = ?", new String[]{String.valueOf(id)});
    }

    public int update(int id, String dot){
        SQLiteDatabase mSQLiteDatabase = mDBOpenHelper.getWritableDatabase();
        String where = "Id" + " = ?";
        String[] whereValue = { Integer.toString(id) };

        ContentValues contentValues = new ContentValues();
        contentValues.put("Id", id);
        contentValues.put("dot", dot);

        int row = mSQLiteDatabase.update(DBOpenHelper.TABLE_NAME, contentValues ,where, whereValue);
        return row;
    }

    public Cursor Query(){
        SQLiteDatabase mSQLiteDatabase = mDBOpenHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.query(DBOpenHelper.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }
}
