package com.example.studentmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class Student defines a Student and its features such as Name/Roll Number/Class
 *
 */
public class Student implements Parcelable {
    private String name;
    private int rollNumber;
    private int myClass;

    /**
     * Constructor Student that will take Name/Roll Number/Class as input
     *
     * @param myName
     * @param myRollNumber
     * @param myClass
     */
    public Student(String myName,int myRollNumber, int myClass){
        this.name = myName;
        this.rollNumber = myRollNumber;
        this.myClass = myClass;
    }

    /**
     * Parcelable constructor
     *
     * @param in
     */
    private Student(Parcel in) {
        this.name = in.readString();
        this.rollNumber = in.readInt();
        this.myClass = in.readInt();
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

    /**
     * returns the Name of the student
     *
     * @return Name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the Name for the student
     *
     * @param thisName
     */
    public void setName(String thisName) {
        this.name = thisName;
    }

    /**
     * returns the ROll NUmber of the student
     *
     * @return Roll Number
     */
    public int getRollNumber() {
        return this.rollNumber;
    }

    /**
     * sets the RollNumber for the Student
     *
     * @param rollNumber
     */
    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    /**
     * returns the class for the student
     *
     * @return class
     *
     */
    public int getMyClass() {
        return this.myClass;
    }

    /**
     * sets the class for the student
     *
     * @param myClass
     */
    public void setMyClass(int myClass) {
        this.myClass = myClass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.rollNumber);
        dest.writeInt(this.myClass);
    }
}
