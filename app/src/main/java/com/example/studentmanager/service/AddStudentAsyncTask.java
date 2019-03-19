package com.example.studentmanager.service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.example.studentmanager.database.DatabaseHelper;
import com.example.studentmanager.model.Student;

import static com.example.studentmanager.activity.AddStudentActivity.INTENT_CLICKED_ROLLNUMBER;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_IS_FROM_ADD;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_IS_FROM_EDIT;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_STUDENT_OBJECT;

public class AddStudentAsyncTask extends AsyncTask<Intent, Integer, String> {

    private DatabaseHelper mDatabaseHelper;

    private Context mContext;

    /**
     * used to pass the context of the calling activity
     *
     * @param context
     */
    public AddStudentAsyncTask(Context context) {
        this.mContext = context;
    }

    /**
     * method doInBackground takes intents as the input and saves user data
     *
     * @param intents
     * @return
     */

    @Override
    protected String doInBackground(Intent... intents) {

        mDatabaseHelper = new DatabaseHelper(mContext);

        if (intents[0].getBooleanExtra(INTENT_IS_FROM_ADD, false)) {
            Student student = intents[0].getParcelableExtra(INTENT_STUDENT_OBJECT);
            mDatabaseHelper.insertStudent(student.getName(), student.getmClass(), student.getRoll_number());
        } else if (intents[0].getBooleanExtra(INTENT_IS_FROM_EDIT, false)) {
            Student student = intents[0].getParcelableExtra(INTENT_STUDENT_OBJECT);

            mDatabaseHelper.updateStudent(student.getName(), student.getmClass(), student.getRoll_number(), intents[0].getIntExtra(INTENT_CLICKED_ROLLNUMBER, 0));

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
