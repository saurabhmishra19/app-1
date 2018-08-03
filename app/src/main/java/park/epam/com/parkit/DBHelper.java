package park.epam.com.parkit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    //information of database

    //https://dzone.com/articles/create-a-database-android-application-in-android-s
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "parkitDB.db";
    public static final String TABLE_NAME = "parking";
    public static final String COLUMN_ID = "empUID";
    public static final String COLUMN_NAME = "uuid";
    //initialize the database
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {}
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}
    //public String loadHandler() {}
  //  public void addHandler(Student student) {}
   // public Student findHandler(String studentname) {}
    //public boolean deleteHandler(int ID) {}
  //  public boolean updateHandler(int ID, String name) {}
}