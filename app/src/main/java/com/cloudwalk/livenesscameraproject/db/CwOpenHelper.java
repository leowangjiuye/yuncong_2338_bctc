package com.cloudwalk.livenesscameraproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;

public class CwOpenHelper extends DaoMaster.DevOpenHelper {

    /**
     * Log Tag
     */
    private static final String TAG = "PropertyHelper";


    private Context context;

    public CwOpenHelper(Context context, String name) {
        super(context, name);
        this.context = context;
    }

    public CwOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
        this.context = context;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d(TAG, "The old version:" + oldVersion + ",the new version:" + newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Database sqliteDb = new StandardDatabase(db);
        DaoMaster.dropAllTables(sqliteDb, true);
        onCreate(sqliteDb);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
        Log.d(TAG, "The database created:");
    }


}
