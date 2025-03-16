package com.RIDdev.antiphone.Database;
import android.database.Cursor;
import android.database.sqlite.*;
import android.content.Context;
import java.lang.reflect.Field;
import java.util.*;
public class DBOperation extends SQLiteOpenHelper{
    SQLiteDatabase db = null;
    public DBOperation(Context context){
        super(context, context.getDatabasePath("Package.db").getPath(), null, 1);
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Package (PackName TEXT PRIMARY KEY);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return;
    }
    public void Remake()
    {

        List<String> dbfields = refresh();
        String query = "";
        Field[] fields = com.RIDdev.antiphone.PackSet.class.getDeclaredFields();
        for(Field field : fields) {
            if (!dbfields.contains(field.getName())){
                if (field.getType() == String.class)
                    query = "ALTER TABLE Package ADD COLUMN " + field.getName() + " TEXT;";
                else
                    query = "ALTER TABLE Package ADD COLUMN " + field.getName() + " INTEGER;";
                db.execSQL(query);
        }
        }

    }
    public List<String> refresh()
    {
        String column = "";
        List<String> OldAtr = new ArrayList<>();
        Cursor cursor = db.rawQuery("PRAGMA table_info(Package);", null);
        while (cursor.moveToNext()) {
            int Index = cursor.getColumnIndex("name");
            if(Index>=0)
            {column = cursor.getString(Index);}
            OldAtr.add(column);
        }
        cursor.close();
        return OldAtr;
    }
}
