package com.khadejaclarke.registerlogin;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Users.db";
    public static final String CONTACTS_TABLE_NAME = "users";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_FORENAME = "forename";
    public static final String CONTACTS_COLUMN_SURNAME = "surname";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_PASSWORD = "password";
    public static final String CONTACTS_COLUMN_SALT = "salt";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table users " +
                        "(id integer, forename text not null, surname text not null, email text primary key, password text not null, salt text not null)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean insertUser (String forename, String surname, String email, String password, String salt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("forename", forename);
        contentValues.put("surname", surname);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("salt", salt);
        db.insert("users", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from users where id="+id+"", null );
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
    }

    public boolean updateUser (Integer id, String forename, String surname, String email, String password, String salt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("forename", forename);
        contentValues.put("surname", surname);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("salt", salt);
        db.update("users", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteUser (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("users",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            User user = new User();
            user.setForename(res.getString(res.getColumnIndex(CONTACTS_COLUMN_FORENAME)));
            user.setSurname(res.getString(res.getColumnIndex(CONTACTS_COLUMN_SURNAME)));
            user.setEmail(res.getString(res.getColumnIndex(CONTACTS_COLUMN_EMAIL)));
            user.setPassword(res.getString(res.getColumnIndex(CONTACTS_COLUMN_PASSWORD)));
            user.setSalt(res.getString(res.getColumnIndex(CONTACTS_COLUMN_SALT)));
            users.add(user);
            res.moveToNext();
        }
        return users;
    }


}