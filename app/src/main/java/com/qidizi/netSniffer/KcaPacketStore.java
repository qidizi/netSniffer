package com.qidizi.netSniffer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.JsonObject;

public class KcaPacketStore extends SQLiteOpenHelper {
    private static final String db_name = "kcasniffer_db";
    private static final String table_name = "packet_store";
    private static int limit = 10;

    public static String getTableName() {
        return table_name;
    }

    public KcaPacketStore(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, db_name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE ".concat(table_name).concat(" ( "));
        sb.append(" KEY INTEGER PRIMARY KEY, ");
        sb.append(" TIMESTAMP INTEGER, ");
        sb.append(" URL TEXT, ");
        sb.append(" REQUEST TEXT, ");
        sb.append(" RESPONSE TEXT ) ");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + table_name);
        onCreate(db);
    }

    public void record(String url, String request, String response) {
        SQLiteDatabase db = this.getWritableDatabase();

        // insert value to db
        ContentValues values = new ContentValues();
        values.put("URL", url);
        values.put("REQUEST", request);
        values.put("RESPONSE", response);
        values.put("TIMESTAMP", System.currentTimeMillis());
        db.insert(table_name, null, values);

        // remove older rows
        db.delete(table_name, "ROWID NOT IN (SELECT ROWID FROM " + table_name +
                " ORDER BY KEY DESC LIMIT " + String.valueOf(limit) + ")", null);
    }

    public JsonObject getRecentData() {
        SQLiteDatabase db = this.getReadableDatabase();
        JsonObject data = new JsonObject();
        Cursor c = db.rawQuery("SELECT URL, REQUEST, RESPONSE, TIMESTAMP ORDER BY KEY DESC LIMIT 1", null);
        while (c.moveToNext()) {
            data.addProperty("url", c.getString(c.getColumnIndex("URL")));
            data.addProperty("request", c.getString(c.getColumnIndex("REQUEST")));
            data.addProperty("response", c.getString(c.getColumnIndex("RESPONSE")));
            data.addProperty("timestamp", c.getLong(c.getColumnIndex("TIMESTAMP")));
        }
        c.close();
        return data;
    }
}

