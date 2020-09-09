package com.cloudwalk.livenesscameraproject.db;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    /**
     * The name of database.
     */
    private static final String DB_NAME = "cloudwalk_midware_demo_terminal.db";

    private static DaoSession daoSession;

    private DBManager() {
    }

    /**
     * Initialize database
     */
    public static void initDataBase(final Application app) {
        DaoMaster.DevOpenHelper devOpenHelper = new CwOpenHelper(app, DB_NAME, null);
        SQLiteDatabase readableDatabase = devOpenHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(readableDatabase);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getInstance() {
        return daoSession;
    }
}
