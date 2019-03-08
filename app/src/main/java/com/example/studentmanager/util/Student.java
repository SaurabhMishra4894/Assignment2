package com.example.studentmanager.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class Student defines a Student and its features such as Name/Roll Number/Class
 *
 */
public class Student implements Parcelable {
    private String thisName;
    private int thisrollNumber;
    private int thisclass;

    /**
     * Constructor Student that will take Name/Roll Number/Class as input
     *
     * @param myName
     * @param myRollNumber
     * @param myClass
     */
    public Student(String myName,int myRollNumber, int myClass){
        thisName = myName;
        thisrollNumber = myRollNumber;
        thisclass = myClass;
    }

    /**
     * Parcelable constructor
     *
     * @param in
     */
    private Student(Parcel in) {
        thisName = in.readString();
        thisrollNumber = in.readInt();
        thisclass = in.readInt();
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
    public String getThisName() {
        return thisName;
    }

    /**
     * Set the Name for the student
     *
     * @param thisName
     */
    public void setThisName(String thisName) {
        this.thisName = thisName;
    }

    /**
     * returns the ROll NUmber of the student
     *
     * @return Roll Number
     */
    public int getThisrollNumber() {
        return thisrollNumber;
    }

    /**
     * sets the RollNumber for the Student
     *
     * @param thisrollNumber
     */
    public void setThisrollNumber(int thisrollNumber) {
        this.thisrollNumber = thisrollNumber;
    }

    /**
     * returns the class for the student
     *
     * @return class
     *
     */
    public int getThisclass() {
        return thisclass;
    }

    /**
     * sets the class for the student
     *
     * @param thisclass
     */
    public void setThisclass(int thisclass) {
        this.thisclass = thisclass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thisName);
        dest.writeInt(thisrollNumber);
        dest.writeInt(thisclass);
    }
}
