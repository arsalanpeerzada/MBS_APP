package com.mbs.mbsapp.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Dell 5521 on 9/26/2016.
 */
public class DbScript {
    private SQLiteDatabase database;
    private static DbScript instance;

    public static DbScript getInstance() {
        if (instance == null) instance = new DbScript();
        return instance;
    }

    private void getDatabase(Context context) {
        DbHandler dbHandler = DbHandler.getInstance(context);
        if (!dbHandler.openDatabase()) {
            dbHandler = DbHandler.newInstance();
        }
        database = dbHandler.getDatabase();
    }

    public void releaseDatabase() {
        database = null;
    }
}
