package com.example.studentmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class Student defines a Student and its features such as Name/Roll Number/Class
 */

public class Student implements Parcelable {

    public static final String TABLE_NAME = "students";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CLASS = "class";
    public static final String COLUMN_ROLL_NUMBER = "roll_number";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String name;
    private int mClass;
    private int roll_number;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT NOT NULL,"
                    + COLUMN_CLASS + " INTEGER NOT NULL,"
                    + COLUMN_ROLL_NUMBER + " INTEGER NOT NULL,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";


    /**
     * constructor student used to set values in the database
     *
     * @param id
     * @param name
     * @param mClass
     * @param rollNumber
     * @param timestamp
     */
    public Student(int id, String name, int mClass, int rollNumber, String timestamp) {
        this.id = id;
        this.name = name;
        this.mClass = mClass;
        this.roll_number = rollNumber;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getmClass() {
        return mClass;
    }

    public int getRoll_number() {
        return roll_number;
    }


    /**
     * Constructor Student that will take Name/Roll Number/Class as input
     *
     * @param myName
     * @param myRollNumber
     * @param myClass
     */
    public Student(String myName, int myRollNumber, int myClass) {
        this.name = myName;
        this.roll_number = myRollNumber;
        this.mClass = myClass;
    }

    /**
     * Parcelable constructor
     *
     * @param in
     */
    private Student(Parcel in) {
        this.name = in.readString();
        this.roll_number = in.readInt();
        this.mClass = in.readInt();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.roll_number);
        dest.writeInt(this.mClass);
    }
}

