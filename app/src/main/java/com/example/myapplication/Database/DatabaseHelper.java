package com.example.myapplication.Database;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.Models.UserModel;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Signup.db";
    private static final String TABLE_NAME = "UserProfile";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                COLUMN_PASSWORD + " TEXT," +
                "fullname TEXT," +
                "birthday TEXT," +
                "gender TEXT," +
                "email TEXT," +
                "phonenumber TEXT," +
                "avatar BLOB)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(String username, String password, String fullname, String birthday, String gender, String email, String phonenumber, byte[] avatar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put("fullname", fullname);
        values.put("birthday", birthday);
        values.put("gender", gender);
        values.put("email", email);
        values.put("phonenumber", phonenumber);
        values.put("avatar", avatar);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public UserModel getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        if (cursor != null) {
            cursor.moveToFirst();
            // Đọc giá trị của cột `avatar` dưới dạng byte array
            byte[] avatarData = cursor.getBlob(cursor.getColumnIndexOrThrow("avatar"));

            // Tạo đối tượng UserModel với dữ liệu đã đọc được
            UserModel user = new UserModel(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    avatarData  // Truyền byte array của avatar
            );
            cursor.close();
            return user;
        }
        return null;
    }
    public void updateUser(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put("fullname", user.getFullname());
        values.put("birthday", user.getBirthday());
        values.put("gender", user.getGender());
        values.put("email", user.getEmail());
        values.put("phonenumber", user.getPhonenumber());
        // Cập nhật giá trị của cột `avatar` dưới dạng byte array
        values.put("avatar", user.getAvatar());

        db.update(TABLE_NAME, values, COLUMN_USERNAME + " = ?", new String[]{user.getUsername()});
    }

    public boolean checkUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public boolean checkAccount(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, password});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
}
