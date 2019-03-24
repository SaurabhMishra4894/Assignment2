package com.example.studentmanager.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.studentmanager.activity.ViewStudentActivity;
import com.example.studentmanager.database.DatabaseHelper;
import com.example.studentmanager.model.Student;

import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_CLICKED_ROLLNUMBER;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_IS_FROM_ADD;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_IS_FROM_EDIT;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_STUDENT_OBJECT;


/**
 * AddStudentIntent Service saves the Student data as IntentService and sends a broadcast to the
 * ViewStudentActivity that the student has been successfully saved
 */
public class AddStudentIntentService extends IntentService {
    DatabaseHelper mDatabaseHelper;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public AddStudentIntentService() {
        super("Add Student Intent Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());

        if (intent.getBooleanExtra(INTENT_IS_FROM_ADD, false)) {
            Student student = intent.getParcelableExtra(INTENT_STUDENT_OBJECT);
            mDatabaseHelper.insertStudent(student.getName(), student.getmClass(), student.getRoll_number());
        } else if (intent.getBooleanExtra(INTENT_IS_FROM_EDIT, false)) {
            Student student = intent.getParcelableExtra(INTENT_STUDENT_OBJECT);

            mDatabaseHelper.updateStudent(student.getName(), student.getmClass(), student.getRoll_number(), intent.getIntExtra(INTENT_CLICKED_ROLLNUMBER, 0));

        }
        intent.setAction(ViewStudentActivity.INTENT_SERVICE_FILTER);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
