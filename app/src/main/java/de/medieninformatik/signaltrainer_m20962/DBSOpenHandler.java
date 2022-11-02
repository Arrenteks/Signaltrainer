package de.medieninformatik.signaltrainer_m20962;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBSOpenHandler extends SQLiteOpenHelper {


    private static DBSOpenHandler instance = null;

    // name and version of the database

    public static void createInstance(Context context, String databasename, int databaseVersion) {
        if (instance==null) {
            instance = new DBSOpenHandler(context, databasename, databaseVersion);
        }
    }
    // Problem: SingleTon Pattern, ABER der Konstruktor braucht den CONTEXT !!
    // sinnvoll wäre ein layzy-Singleton
    public static DBSOpenHandler getInstance() {
        return instance;
    }

    private DBSOpenHandler(Context context, String databasename, int databaseVersion) {
        super(context, databasename, null, databaseVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(MainActivity.TAG, "********** String  getCreateTables");
        try {
            // sqlite kann immer nur EINEN SQL-String abarbeiten !!!
            String sql= DBSQueries.getCreateTables();
            String[] sqlArray = sql.split(";");
            for (String s : sqlArray){
                db.execSQL(s);
                Log.i(MainActivity.TAG, "********** database created, sql: "+s);
            }
        } catch (SQLiteException e) {
            Log.i(MainActivity.TAG, "**********                        no database are created", e);
        } finally {
            Log.i(MainActivity.TAG, "********** in finally onCreate");
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.queryWithFactory()
        Log.i(MainActivity.TAG, "Upgrade of the database from version "
                + oldVersion + " to "
                + newVersion + "; all datas are deleted");
        //String sql= DBSQueries.getCreateTables();
        //db.execSQL(sql);
        //onCreate(db);
    }


    public DBError updateSQL(ContentValues values, String dbtable, String str_id, long id) {
        long count = -1;
        String error="";
        boolean isError=false;
        SQLiteDatabase db=null;
        try {
            db=getWritableDatabase();
            Log.i(MainActivity.TAG, "********************* DB-Path: "+db.getPath()+"   id: "+id);
            count = db.update(dbtable,values,str_id+"= ?",new String[] {Long.toString(id)});
        }
        catch (SQLException e) {
            Log.i(MainActivity.TAG, "********************* DB-Exception: "+e.toString());
            error="DB-Exception: "+e.toString();
            isError=true;
        }
        finally {
            Log.i(MainActivity.TAG, "********************* Finally");
            if (db!=null) db.close();
        }
        return new DBError(count, isError, error);
    }


    public DBError deleteSQL(String dbtable, String str_id, long id) {
        long rowId = -1;
        String error="";
        boolean isError=false;
        SQLiteDatabase db=null;
        try {
            db=getWritableDatabase();
            Log.i(MainActivity.TAG, "********************* DB-Path: "+db.getPath());
            if (id>0) {
                rowId = db.delete(dbtable,str_id+"= ?",new String[] {Long.toString(id)});
            }
            else {
                rowId = db.delete(dbtable,null, null);
            }

            // rowId = db.delete(dbtable,str_id+"= ?",new String[] {String.valueOf(id)});
        }
        catch (SQLException e) {
            Log.i(MainActivity.TAG, "********************* DB-Exception: "+e.toString());
            error="DB-Exception: "+e.toString();
            isError=true;
        }
        finally {
            Log.i(MainActivity.TAG, "********************* Finally");
            if (db!=null) db.close();
        }
        return new DBError(rowId,isError, error);
    }

    public DBError insertSQL(ContentValues values, String dbtable) {
        long rowId = -1;
        String error="";
        boolean isError=false;
        SQLiteDatabase db=null;
        try {
            db=getWritableDatabase();
            Log.i(MainActivity.TAG, "********************* DB-Path: "+db.getPath());
            rowId = db.insert(dbtable,null, values);
        }
        catch (SQLException e) {
            Log.i(MainActivity.TAG, "********************* DB-Exception: "+e.toString());
            error="DB-Exception: "+e.toString();
            isError=true;
        }
        finally {
            Log.i(MainActivity.TAG, "********************* Finally");
            if (db!=null) db.close();
        }
        return new DBError(rowId,isError, error);
    }


    // select * from  =>  tableColumns=null   // 2222222222
    public Cursor getQueryPur(String sql)  {
        try{
            SQLiteDatabase db = getWritableDatabase();
            Cursor resultSet = db.rawQuery(sql,null); // ErgebnisMatrix
            return resultSet;
        }
        catch(SQLiteException e)    {
            Log.e("Database", "getQueryAutomarke", e);
            return null;
        }
        //return db.query(TABLE_NAME, null, "Automarke", new String[]{Automarke}, null, null, null);
    }

    // select * from  =>  tableColumns=null
    public Cursor getQuery(String table, String[] tableColumns,
                           String whereClause, String orderBy)  {
        try{
            SQLiteDatabase db = getWritableDatabase();
            // db.execSQL(sql);
            // db.beginTransaction();
            // db.endTransaction();
            //  String table, String[] columns, String whereClause, String[] selectionArgs, String groupBy, String having, String orderBy
            //  String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy
            return db.query(table, tableColumns, whereClause, null, null, null, orderBy);
        }
        catch(SQLiteException e)    {
            Log.e("Database", "getQueryAutomarke", e);
            return null;
        }
        //return db.query(TABLE_NAME, null, "Automarke", new String[]{Automarke}, null, null, null);
    }


    }  // DBSOpenHandler

