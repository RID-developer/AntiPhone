package com.RIDdev.antiphone.Database;
import static java.lang.reflect.Array.get;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.*;
import android.content.Context;
import android.util.Log;

import com.RIDdev.antiphone.PackSet;
import com.RIDdev.antiphone.background.Constant;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
public class DBOperation extends SQLiteOpenHelper {
    SQLiteDatabase db = null;
    Field[] fields = com.RIDdev.antiphone.PackSet.class.getDeclaredFields();
    Type[] types = new Type[fields.length];
    String[] names = new String[fields.length];
    public DBOperation(Context context) {
        super(context, context.getDatabasePath("Package.db").getPath(), null, 1);
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Package (PackName TEXT PRIMARY KEY);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void fill()
    {
        int i = 0;
        for(Field field: fields)
        {
            types[i] = field.getType();
            names[i] = field.getName();
            ++i;
        }
    }
    public void Remake() {

        List<String> dbfields = refresh();
        StringBuilder query;
        for (int i=0;i< fields.length;++i) {
            if (!dbfields.contains(names[i])) {
                if (types[i] == String.class)
                    query = new StringBuilder("ALTER TABLE Package ADD COLUMN " + names[i] + " TEXT;");
                else
                    query = new StringBuilder("ALTER TABLE Package ADD COLUMN " + names[i] + " INTEGER;");
                db.execSQL(String.valueOf(query));
            }
        }

    }

    public List<String> refresh() {
        String column = "";
        List<String> OldAtr = new ArrayList<>();
        Cursor cursor = db.rawQuery("PRAGMA table_info(Package);", null);
        while (cursor.moveToNext()) {
            int Index = cursor.getColumnIndex("name");
            if (Index >= 0) {
                column = cursor.getString(Index);
            }
            OldAtr.add(column);
        }
        cursor.close();
        return OldAtr;
    }
    public void Insert(PackSet pack) {
        try {
            ContentValues values = new ContentValues();
            Object x;
            for (int i = 0; i < types.length; ++i) {
                x = fields[i].get(pack);
                if (x != null) {
                    if (types[i] == String.class)
                        values.put(names[i], (String) x);
                    if (types[i] == boolean.class)
                        values.put(names[i], (Boolean) x);
                    if (types[i] == int.class)
                        values.put(names[i], (Integer) x);
                }
            }

            db.insertWithOnConflict("Package", null, values, SQLiteDatabase.CONFLICT_REPLACE);

        } catch(Exception e) {
            System.out.println("AAAAAAAAAAAAA WHYYYYYY" + e);
            Remake();
        }
    }

    public void Retrieve(PackSet pack) {
        Cursor cursor;
        String query;
        PackSet Pack = new PackSet();
        Pack.Reset();
        for (int i = 0; i < names.length; ++i) {
            if (!names[i].equals("PackName")) {
                query = "SELECT " + names[i] + " FROM Package WHERE PackName = ?";
                cursor = db.rawQuery(query, new String[]{pack.PackName});
                Log.d("AppDetails", "PackName before Retrieve: " + pack.PackName);
                if ((cursor != null) && cursor.moveToFirst()) {
                    try {

                        if (cursor.isNull(0) || cursor.getInt(0) == 0) {
                            fields[i].set(pack, fields[i].get(Pack));
                        } else if (types[i] == String.class) {
                            fields[i].set(pack, cursor.getString(0));
                        } else if (types[i] == int.class || types[i] == Integer.class) {
                            fields[i].set(pack, cursor.getInt(0));
                        } else if (types[i] == boolean.class || types[i] == Boolean.class) {
                            fields[i].set(pack, cursor.getInt(0) == 1);
                        }

                    } catch (IllegalAccessException e) {
                        System.out.println("AAAAAAAAAAAAA" + e);
                        Remake();
                    } finally {
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                    }
                }
            }
        }
    }
    }


