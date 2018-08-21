package park.epam.com.parkit.park.epam.com.dao;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Map;

import park.epam.com.parkit.Employee;

/**
 * Created by saurabh on 4/8/18.
 */

public class EmplyeeProvider  extends ContentProvider{
    static final String PROVIDER_NAME="park.epam.com.parkit.park.epam.com.dao.EmplyeeProvider";
    static final String URL="content://"+PROVIDER_NAME+"/employees";
    public static final Uri CONTENT_URI= Uri.parse(URL);

    public static final String _ID="_id";
    public static final String NAME="name";
    public static final String EMPID="employeeId";

    private static Map<String, String> EMPLOYEE_PROJ_MAP;
    static final int EMPLOYEES=1;
    static final int EMPLOYEE_ID=2;
    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "employees", EMPLOYEES);
        uriMatcher.addURI(PROVIDER_NAME, "employees/#", EMPLOYEE_ID);
    }


    private SQLiteDatabase db;
    static final String DATABASE_NAME = "EPAM_PARKIT";
    static final String EMPLOYEES_TABLE_NAME = "employees";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + EMPLOYEES_TABLE_NAME +
                    " (_id TEXT PRIMARY KEY, " +
                    " name TEXT NOT NULL, " +
                    " employeeId TEXT NOT NULL);";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  EMPLOYEES_TABLE_NAME);
            onCreate(db);
        }
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        long rowID = db.insert(	EMPLOYEES_TABLE_NAME, "", values);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(EMPLOYEES_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case EMPLOYEES:
                qb.setProjectionMap(EMPLOYEE_PROJ_MAP);
                break;

            case EMPLOYEE_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
            sortOrder = NAME;
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case EMPLOYEES:
                count = db.delete(EMPLOYEES_TABLE_NAME, selection, selectionArgs);
                break;

            case EMPLOYEE_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( EMPLOYEES_TABLE_NAME, _ID +  " = " + id +
                                (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case EMPLOYEES:
                count = db.update(EMPLOYEES_TABLE_NAME, values, selection, selectionArgs);
                break;

            case EMPLOYEE_ID:
                count = db.update(EMPLOYEES_TABLE_NAME, values,
                        _ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? "  AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case EMPLOYEES:
                return "vnd.android.cursor.dir/parkit.epam.employees";
            /**
             * Get a particular student
             */
            case EMPLOYEE_ID:
                return "vnd.android.cursor.item/parkit.epam.employees";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}

