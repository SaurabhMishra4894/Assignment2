package com.example.studentmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.studentmanager.R;
import com.example.studentmanager.model.Student;


/**
 * ViewStudentActivity where user add details about the student and saves it
 * The same activity is shown to the user when user view/Edit student data
 */
public class ViewStudentActivity extends AppCompatActivity {

    public static final String INTENT_IS_FROM_VIEW = "is_from_view";
    public static final String INTENT_IS_FROM_EDIT = "is_from_edit";
    public static final String INTENT_IS_FROM_ADD = "is_from_add";
    public static final String INTENT_STUDENT_OBJECT = "student_object";
    public static final String INTENT_CLICKED_POSITION = "clicked_position";
    public static final String INTENT_CLICKED_ROLLNUMBER = "clicked_roll_number";
    public static final String INTENT_SERVICE_FILTER = "intentservice";
    public static final String SERVICE_FILTER = "service";


    private EditText etName, etRollNumber, etClass;
    private Button btnAddStudent;
    private Student mStudent;
    private boolean isFromView;


    /**
     * onCreate method for Add Student Activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        etName = findViewById(R.id.et_name_activity_add_student);
        etRollNumber = findViewById(R.id.et_rollNumber_activity_add_student);
        etClass = findViewById(R.id.et_Class_activity_add_student);
        btnAddStudent = findViewById(R.id.btn_addStudent_activity_add_student);


        /*
         * If the activity is opening for Edit/View Intent != null is true
         * A token is passed by the main activity will checks of the activity is calling for edit/View
         *
         */
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mStudent = bundle.getParcelable(INTENT_STUDENT_OBJECT);
                isFromView = bundle.getBoolean(INTENT_IS_FROM_VIEW, false);

                if (isFromView) {
                    onStudentView();
                }
            }
        }


    }


    /**
     * method onStudentView which calls when user View students data
     */
    public void onStudentView() {

        etName.setEnabled(false);
        etRollNumber.setEnabled(false);
        etClass.setEnabled(false);
        btnAddStudent.setVisibility(View.GONE);
        etName.setText(mStudent.getName());
        etRollNumber.setText(String.valueOf(mStudent.getRoll_number()));
        etClass.setText(String.valueOf(mStudent.getmClass()));

    }


}
