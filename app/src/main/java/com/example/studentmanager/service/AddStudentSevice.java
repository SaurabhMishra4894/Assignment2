package com.example.studentmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.studentmanager.activity.ViewStudentActivity;
import com.example.studentmanager.database.DatabaseHelper;
import com.example.studentmanager.model.Student;

import org.jetbrains.annotations.Nullable;

import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_CLICKED_ROLLNUMBER;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_IS_FROM_ADD;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_IS_FROM_EDIT;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_STUDENT_OBJECT;


/**
 * AddStudentService class is a service which saves student data and sends a broadcast
 * to the ViewStudentActivity if the student is successfully saved or not
 */
public class AddStudentSevice extends Service {


    protected DatabaseHelper mDatabaseHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mDatabaseHelper = new DatabaseHelper(getApplicationContext());

        if (intent.getBooleanExtra(INTENT_IS_FROM_ADD, false)) {
            Student student = intent.getParcelableExtra(INTENT_STUDENT_OBJECT);
            mDatabaseHelper.insertStudent(student.getName(), student.getmClass(), student.getRoll_number());
        } else if (intent.getBooleanExtra(INTENT_IS_FROM_EDIT, false)) {
            Student student = intent.getParcelableExtra(INTENT_STUDENT_OBJECT);

            mDatabaseHelper.updateStudent(student.getName(), student.getmClass(), student.getRoll_number(), intent.getIntExtra(INTENT_CLICKED_ROLLNUMBER, 0));

        }

        intent.setAction(ViewStudentActivity.SERVICE_FILTER);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        return START_STICKY;
    }

}
