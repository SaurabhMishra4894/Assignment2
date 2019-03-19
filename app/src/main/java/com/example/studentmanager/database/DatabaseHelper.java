package com.example.studentmanager.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studentmanager.model.Student;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "student_db";


    /**
     * DatabaseHelper constructor user to initialie the object
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate Method which creates the table
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Student.CREATE_TABLE);
    }

    /**
     * onUpgrade if the database is upgraded
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_NAME);
        onCreate(db);
    }

    /**
     * method insert student into the database and returns the Id
     *
     * @param name
     * @param mCLass
     * @param rollNumber
     * @return
     */
    public long insertStudent(String name, int mCLass, int rollNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Student.COLUMN_NAME, name);
        values.put(Student.COLUMN_CLASS, mCLass);
        values.put(Student.COLUMN_ROLL_NUMBER, rollNumber);

        long id = db.insert(Student.TABLE_NAME, null, values);


        db.close();
        return id;
    }


    /**
     * getAllStudents retreive student list from the database
     *
     * @return List<Student>
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME + " ORDER BY " +
                Student.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndex(Student.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME));
                int mClass = cursor.getInt(cursor.getColumnIndex(Student.COLUMN_CLASS));
                int roll_number = cursor.getInt(cursor.getColumnIndex(Student.COLUMN_ROLL_NUMBER));
                String timestamp = cursor.getString(cursor.getColumnIndex(Student.COLUMN_TIMESTAMP));
                Student student = new Student(id, name, mClass, roll_number, timestamp);


                students.add(student);
            } while (cursor.moveToNext());
        }

        // close db connection
        cursor.close();
        db.close();

        // return notes list
        return students;
    }

    /**
     * Checks if rollnumber already exists in the database
     *
     * @param rollNumber
     * @return
     */
    public boolean rollNumberExists(int rollNumber) {
        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME + " WHERE " +
                Student.COLUMN_ROLL_NUMBER + " = " + rollNumber;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * method checks if the rollNumber already exists in the database other than the
     * rollNumber which is getting edited
     *
     * @param rollNumber
     * @param position
     * @return
     */
    public boolean rollNumberExistsEdit(int rollNumber, int position) {
        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME + " WHERE " +
                Student.COLUMN_ROLL_NUMBER + " = " + rollNumber + " AND Id != " + position;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * getStudent Id based on the rollNumber
     *
     * @param rollNumber
     * @return id of the user
     */
    public int getStudentId(int rollNumber) {
        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME + " WHERE " +
                Student.COLUMN_ROLL_NUMBER + " = " + rollNumber;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int position = cursor.getInt(cursor.getColumnIndex(Student.COLUMN_ID));
        cursor.close();
        db.close();
        return position;
    }

    /**
     * Updates the student details based on the earlier rollnumber if the rollnumber is not edited
     * oldRollNUmber and rollNUmber is same
     *
     * @param name
     * @param mCLass
     * @param rollNumber
     * @param oldRollNumber
     */
    public void updateStudent(String name, int mCLass, int rollNumber, int oldRollNumber) {

        String selectQuery = "UPDATE " + Student.TABLE_NAME + " SET " +
                Student.COLUMN_NAME + " = " + "'" + name + "'" + " , " + Student.COLUMN_CLASS + " = " + mCLass + " , " + Student.COLUMN_ROLL_NUMBER + " = " +
                rollNumber + " WHERE " + Student.COLUMN_ROLL_NUMBER + " = " + oldRollNumber;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        //int mPosition = cursor.getInt(cursor.getColumnIndex(Student.COLUMN_ID));
        cursor.close();
        db.close();
    }

    /**
     * deleteStudent on the basis of the rollNumber
     *
     * @param rollNumber
     * @return
     */
    public boolean deleteStudent(int rollNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(Student.TABLE_NAME, Student.COLUMN_ROLL_NUMBER + "=" + rollNumber, null) > 0;
    }

}
