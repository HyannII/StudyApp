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

import com.example.myapplication.Models.ResultModel;
import com.example.myapplication.Models.UserModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "Signup.db";
    private static final String TABLE_NAME = "UserProfile";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String TABLE_RESULT = "Result";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EXAMINEE = "examinee";
    private static final String COLUMN_START_TIME = "startTime";
    private static final String COLUMN_CORRECT_NUM = "correctNum";
    private static final String COLUMN_WRONG_NUM = "wrongNum";
    private static final String COLUMN_TIME_LEFT = "timeLeft";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng UserProfile
        String createUserProfileTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                COLUMN_PASSWORD + " TEXT," +
                "fullname TEXT," +
                "birthday TEXT," +
                "gender TEXT," +
                "email TEXT," +
                "phonenumber TEXT," +
                "avatar BLOB)";
        db.execSQL(createUserProfileTableQuery);

        // Tạo bảng Result
        String createResultTableQuery = "CREATE TABLE " + TABLE_RESULT + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_EXAMINEE + " TEXT," +
                COLUMN_START_TIME + " TEXT," +
                COLUMN_CORRECT_NUM + " INTEGER," +
                COLUMN_WRONG_NUM + " INTEGER," +
                COLUMN_TIME_LEFT + " TEXT)";
        db.execSQL(createResultTableQuery);
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
    public long addResult(String examinee, String startTime, int correctNum, int wrongNum, String timeLeft) {
        // Lấy cơ sở dữ liệu trong chế độ ghi
        SQLiteDatabase db = this.getWritableDatabase();

        // Tạo đối tượng ContentValues để lưu các giá trị của các trường
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXAMINEE, examinee);
        values.put(COLUMN_START_TIME, startTime);
        values.put(COLUMN_CORRECT_NUM, correctNum);
        values.put(COLUMN_WRONG_NUM, wrongNum);
        values.put(COLUMN_TIME_LEFT, timeLeft);

        // Chèn hàng vào bảng Result
        // Hàm insert() trả về ID của hàng mới được chèn, hoặc -1 nếu chèn thất bại
        long resultId = db.insert(TABLE_RESULT, null, values);

        // Đóng cơ sở dữ liệu
        db.close();

        // Trả về ID của hàng mới được chèn
        return resultId;
    }
    public ArrayList<ResultModel> getAllResult() {
        // Tạo danh sách để lưu trữ các đối tượng Result
        ArrayList<ResultModel> resultList = new ArrayList<>();

        // Lấy cơ sở dữ liệu trong chế độ đọc
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn tất cả các hàng trong bảng Result
        String query = "SELECT * FROM " + TABLE_RESULT;
        Cursor cursor = db.rawQuery(query, null);

        // Lặp qua các kết quả truy vấn
        if (cursor.moveToFirst()) {
            do {
                // Lấy dữ liệu của một hàng từ cursor
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String examinee = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXAMINEE));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME));
                int correctNum = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_NUM));
                int wrongNum = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WRONG_NUM));
                String timeLeft = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME_LEFT));

                // Tạo đối tượng Result mới từ dữ liệu của hàng
                ResultModel result = new ResultModel(id, examinee, startTime, correctNum, wrongNum, timeLeft);

                // Thêm đối tượng Result vào danh sách
                resultList.add(result);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return resultList;
    }

}
