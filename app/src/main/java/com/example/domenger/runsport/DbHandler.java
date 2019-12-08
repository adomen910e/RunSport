package com.example.domenger.runsport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;


public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "usersdb";

    private static final String TABLE_Users = "userdetails";
    private static final String KEY_ID = "id";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_MDP = "mdp";
    private static final String KEY_DATE = "date";

    private static final String TABLE_Courses = "coursesdetails";
    private static final String KEY_ID_COURSES = "idcourses";
    private static final String KEY_ID_USER = "iduser";
    private static final String KEY_SPEED_MAX = "speedmax";
    private static final String KEY_SPEED_MOY = "speedmoy";
    private static final String KEY_DIST = "distance";
    private static final String KEY_TIME = "time";
    private static final String KEY_POINTS = "points";
    private static final String KEY_TIME_COURSE = "date";


    DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_Courses + "("
                + KEY_ID_COURSES + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ID_USER + " INTEGER,"
                + KEY_SPEED_MAX + " TEXT,"
                + KEY_SPEED_MOY + " TEXT,"
                + KEY_DIST + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_POINTS + " TEXT,"
                + KEY_TIME_COURSE + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE_COURSES);


        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_Users + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_LOGIN + " TEXT,"
                + KEY_NOM + " TEXT,"
                + KEY_PRENOM + " TEXT,"
                + KEY_MDP + " TEXT,"
                + KEY_DATE + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE_USER);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Users);
        onCreate(db);
    }

    void insertCoursesDetails(int id_user, String max_speed, String moy_speed, String dist, String time, String date, String points){
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ID_USER, id_user);
        cValues.put(KEY_SPEED_MAX, max_speed);
        cValues.put(KEY_SPEED_MOY, moy_speed);
        cValues.put(KEY_DIST, dist);
        cValues.put(KEY_TIME, time);
        cValues.put(KEY_POINTS, points);
        cValues.put(KEY_TIME_COURSE, date);

        // Insert the new row, returning the primary key value of the new row
        db.insert(TABLE_Courses,null, cValues);
        db.close();
    }

    long insertUserDetails(String login, String nom, String prenom, String mdp, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_LOGIN, login);
        cValues.put(KEY_NOM, nom);
        cValues.put(KEY_PRENOM, prenom);
        cValues.put(KEY_MDP, mdp);
        cValues.put(KEY_DATE, date);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Users,null, cValues);
        db.close();

        return newRowId;
    }


    ArrayList<HashMap<String, String>> GetUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        String query = "SELECT id, login, nom, prenom, mdp, date FROM "+ TABLE_Users;
        Cursor cursor = db.rawQuery(query,null);

        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("id",cursor.getString(cursor.getColumnIndex(KEY_ID)));
            user.put("login",cursor.getString(cursor.getColumnIndex(KEY_LOGIN)));
            user.put("nom",cursor.getString(cursor.getColumnIndex(KEY_NOM)));
            user.put("prenom",cursor.getString(cursor.getColumnIndex(KEY_PRENOM)));
            user.put("mdp",cursor.getString(cursor.getColumnIndex(KEY_MDP)));
            user.put("date",cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            userList.add(user);
        }
        return  userList;
    }

    ArrayList<HashMap<String, String>> GetCoursesFromUser(int id_user){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        String query = "SELECT iduser, speedmax, speedmoy, distance, time, points, date FROM "+ TABLE_Courses;
        Cursor cursor = db.rawQuery(query,null);

        String str_id_user = Integer.toString(id_user);

        while (cursor.moveToNext()){
            if (str_id_user.equals(cursor.getString(cursor.getColumnIndex(KEY_ID_USER)))){
                HashMap<String,String> user = new HashMap<>();
                user.put("iduser",cursor.getString(cursor.getColumnIndex(KEY_ID_USER)));
                user.put("speedmax",cursor.getString(cursor.getColumnIndex(KEY_SPEED_MAX)));
                user.put("speedmoy",cursor.getString(cursor.getColumnIndex(KEY_SPEED_MOY)));
                user.put("distance",cursor.getString(cursor.getColumnIndex(KEY_DIST)));
                user.put("time",cursor.getString(cursor.getColumnIndex(KEY_TIME)));
                user.put("points",cursor.getString(cursor.getColumnIndex(KEY_POINTS)));
                user.put("date",cursor.getString(cursor.getColumnIndex(KEY_TIME_COURSE)));
                userList.add(user);
            }
        }
        return  userList;
    }


    public ArrayList<HashMap<String, String>> GetUserByUserId(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        String query = "SELECT login, nom, prenom, mdp, date FROM "+ TABLE_Users;
        Cursor cursor = db.query(TABLE_Users, new String[]{KEY_LOGIN, KEY_NOM, KEY_PRENOM, KEY_MDP, KEY_DATE},
                KEY_ID+ "=?",new String[]{String.valueOf(userid)},null,
                null, null, null);

        if (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("login",cursor.getString(cursor.getColumnIndex(KEY_LOGIN)));
            user.put("nom",cursor.getString(cursor.getColumnIndex(KEY_NOM)));
            user.put("prenom",cursor.getString(cursor.getColumnIndex(KEY_PRENOM)));
            user.put("mdp",cursor.getString(cursor.getColumnIndex(KEY_MDP)));
            user.put("date",cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            userList.add(user);
        }
        return  userList;
    }


    public void DeleteUser(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Users, KEY_ID+" = ?",new String[]{String.valueOf(userid)});
        db.close();
    }


    public int UpdateUserDetails(String mdp, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_MDP, mdp);
        return db.update(TABLE_Users, cVals, KEY_ID+" = ?",new String[]{String.valueOf(id)});
    }
}
