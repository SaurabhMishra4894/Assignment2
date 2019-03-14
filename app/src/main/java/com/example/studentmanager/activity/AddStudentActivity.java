package com.example.studentmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.studentmanager.R;
import com.example.studentmanager.model.Student;
import com.example.studentmanager.util.Validate;

/**
 * AddStudentActivity where user add details about the student and saves it
 * The same activity is shown to the user when user view/Edit student data
 */
public class AddStudentActivity extends AppCompatActivity {

    public static final String INTENT_IS_FROM_VIEW = "is_from_view";
    public static final String INTENT_IS_FROM_EDIT = "is_from_edit";
    public static final String INTENT_IS_FROM_ADD = "is_from_add";
    public static final String INTENT_STUDENT_OBJECT = "student_object";
    public static final String INTENT_CLICKED_POSITION = "clicked_position";

    private EditText etName, etRollNumber, etClass;
    private Button btnAddStudent;
    private Student mStudent;
    private boolean isFromEdit, isFromView, isFromAdd;
    private int mClickedPosition;

    /**
     * onCreate method for Add Student Activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        etName = findViewById(R.id.name);
        etRollNumber = findViewById(R.id.rollNumber);
        etClass = findViewById(R.id.myClass);
        btnAddStudent = findViewById(R.id.addStudent);


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
                mClickedPosition = bundle.getInt(INTENT_CLICKED_POSITION, -1);
                isFromEdit = bundle.getBoolean(INTENT_IS_FROM_EDIT, false);
                isFromView = bundle.getBoolean(INTENT_IS_FROM_VIEW, false);
                isFromAdd = bundle.getBoolean(INTENT_IS_FROM_ADD, false);

                if (isFromView) {
                    onStudentView();
                } else if (isFromEdit) {
                    onStudentEdit();
                }
            }
        }


        /*
         * OnClickListner for addStudent Button
         * on the Basis of Token that is get from the MainActivity
         * it decides if the value is for EDIT or VIEW
         *
         */
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    Student updatedStudent = new Student(etName.getText().toString(),
                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt(INTENT_CLICKED_POSITION, mClickedPosition);
                    bundle.putParcelable(getString(R.string.studentObject), updatedStudent);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
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
        etRollNumber.setText(String.valueOf(mStudent.getRollNumber()));
        etClass.setText(String.valueOf(mStudent.getMyClass()));
    }

    /**
     * method onStudentEdit calls when user edit students data
     * the variable position is get from MainActivity in order to make sure when user
     * updates the data the student object on same position gets override
     */
    public void onStudentEdit() {
        etName.setText(mStudent.getName());
        etRollNumber.setText(String.valueOf(mStudent.getRollNumber()));
        etClass.setText(String.valueOf(mStudent.getMyClass()));
        btnAddStudent.setText(getString(R.string.update));
    }

    /**
     * Validate Fiels validate Name/Roll Number/Class of the student with a check that
     * Name should not have special characters or spaces
     * Roll Number should lie between 1 and 9999
     * class should be between 1 to 12
     *
     * @return true if valid
     */
    public boolean validateFields() {
        Validate validate = new Validate();
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError(getString(R.string.notEmpty));
        } else if (!validate.stringValidate(etName.getText().toString())) {
            etName.setError(getString(R.string.notValidName));
        } else if (etRollNumber.getText().toString().trim().isEmpty()) {
            etRollNumber.setError(getString(R.string.notEmpty));
        } else if (!validate.rollNumberValidate(Integer.parseInt(etRollNumber.getText().toString()))) {
            etRollNumber.setError(getString(R.string.notValidRollNo));
        } else if (etClass.getText().toString().trim().isEmpty()) {
            etClass.setError(getString(R.string.notEmpty));
        } else if (!validate.classValidate(Integer.parseInt(etClass.getText().toString()))) {
            etClass.setError(getString(R.string.notValidClass));
        } else {
            return true;
        }
        return false;
    }
}
