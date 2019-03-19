package com.example.studentmanager.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentmanager.R;
import com.example.studentmanager.database.DatabaseHelper;
import com.example.studentmanager.model.Student;
import com.example.studentmanager.service.AddStudentAsyncTask;
import com.example.studentmanager.service.AddStudentIntentService;
import com.example.studentmanager.service.AddStudentSevice;
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
    public static final String INTENT_CLICKED_ROLLNUMBER = "clicked_roll_number";
    public static final String INTENT_SEND_POSTION = "new_student";
    public static final String INTENT_SERVICE_FILTER = "intentservice";
    public static final String SERVICE_FILTER = "service";


    private EditText etName, etRollNumber, etClass;
    private Button btnAddStudent;
    private Student mStudent;
    private boolean isFromEdit, isFromView, isFromAdd;
    private int mClickedPosition, mCLickedRollNumber;
    private DatabaseHelper mDatabaseHelper;
    MyReceiver myReceiver;
    private static Dialog dialog;


    @Override
    protected void onStart() {
        super.onStart();
        setReceiver();
    }


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
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());


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
                mCLickedRollNumber = bundle.getInt(INTENT_CLICKED_ROLLNUMBER, -1);
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
         * on the Basis of Token that is get from the ShowStudentActivity
         * it decides if the value is for EDIT or VIEW
         *
         */
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AddStudentActivity.this);

                // Includes dialog_on_student_click.xml file
                View view = getLayoutInflater().inflate(R.layout.dilog_saving_method, null);
                dialog.setContentView(view);


                // set values for custom dialog components - text, image and button
                Button addService = view.findViewById(R.id.btn_add_service);
                Button addIntentService = view.findViewById(R.id.btn_add_intent_service);
                Button addAsyncService = view.findViewById(R.id.btn_add_async);

                dialog.show();

                //onCLickListner for Add Student As service

                addService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validateFields()) {

                            if (isFromEdit) {
                                if (!mDatabaseHelper.rollNumberExistsEdit(Integer.parseInt(etRollNumber.getText().toString()), mDatabaseHelper.getStudentId(mStudent.getRoll_number()))) {
                                    Intent intent = new Intent(getApplicationContext(), AddStudentSevice.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_EDIT, isFromEdit);
                                    bundle.putInt(INTENT_CLICKED_ROLLNUMBER, mCLickedRollNumber);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    startService(intent);
                                } else {
                                    dialog.dismiss();
                                    etRollNumber.setError(getString(R.string.notUniqueRollNo));
                                }


                            } else if (isFromAdd) {
                                if (!mDatabaseHelper.rollNumberExists(Integer.parseInt(etRollNumber.getText().toString()))) {
                                    Intent intent = new Intent(getApplicationContext(), AddStudentSevice.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_ADD, isFromAdd);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    startService(intent);
                                } else {
                                    dialog.dismiss();
                                    etRollNumber.setError(getString(R.string.notUniqueRollNo));
                                }

                            }

                        }

                    }
                });
                //onClickListner to add Student as IntentService
                addIntentService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validateFields()) {

                            if (isFromEdit) {
                                if (!mDatabaseHelper.rollNumberExistsEdit(Integer.parseInt(etRollNumber.getText().toString()), mDatabaseHelper.getStudentId(mStudent.getRoll_number()))) {
                                    Intent intent = new Intent(getApplicationContext(), AddStudentIntentService.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_EDIT, isFromEdit);
                                    bundle.putInt(INTENT_CLICKED_ROLLNUMBER, mCLickedRollNumber);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    startService(intent);

                                } else {
                                    dialog.dismiss();
                                    etRollNumber.setError(getString(R.string.notUniqueRollNo));
                                }

                            } else if (isFromAdd) {
                                if (!mDatabaseHelper.rollNumberExists(Integer.parseInt(etRollNumber.getText().toString()))) {
                                    Intent intent = new Intent(getApplicationContext(), AddStudentIntentService.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_ADD, isFromAdd);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    startService(intent);

                                } else {
                                    dialog.dismiss();
                                    etRollNumber.setError(getString(R.string.notUniqueRollNo));
                                }

                            }

                        }
                    }
                });
                //onClickListner to save student as Async task

                addAsyncService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (validateFields()) {

                            if (isFromEdit) {
                                if (!mDatabaseHelper.rollNumberExistsEdit(Integer.parseInt(etRollNumber.getText().toString()), mDatabaseHelper.getStudentId(mStudent.getRoll_number()))) {
                                    Intent intent = new Intent(getApplicationContext(), AddStudentAsyncTask.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_EDIT, isFromEdit);
                                    bundle.putInt(INTENT_CLICKED_ROLLNUMBER, mCLickedRollNumber);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    new AddStudentAsyncTask(AddStudentActivity.this).execute(intent);
                                    dialog.dismiss();
                                    finish();

                                } else {
                                    dialog.dismiss();
                                    etRollNumber.setError(getString(R.string.notUniqueRollNo));
                                }

                            } else if (isFromAdd) {
                                if (!mDatabaseHelper.rollNumberExists(Integer.parseInt(etRollNumber.getText().toString()))) {
                                    Intent intent = new Intent(getApplicationContext(), AddStudentIntentService.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_ADD, isFromAdd);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    startService(intent);
                                    dialog.dismiss();
                                    finish();

                                } else {
                                    dialog.dismiss();
                                    etRollNumber.setError(getString(R.string.notUniqueRollNo));
                                }

                            }

                        }
                    }
                });


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
        etRollNumber.setText(String.valueOf(mStudent.getRoll_number()));
        etClass.setText(String.valueOf(mStudent.getmClass()));
    }

    /**
     * method onStudentEdit calls when user edit students data
     * the variable position is get from ShowStudentActivity in order to make sure when user
     * updates the data the student object on same position gets override
     */
    public void onStudentEdit() {
        etName.setText(mStudent.getName());
        etRollNumber.setText(String.valueOf(mStudent.getRoll_number()));
        etClass.setText(String.valueOf(mStudent.getmClass()));
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


    /**
     * setReceiver Function Starts the Broadcast Manager in order to finish the activity
     * when operation is successfully performed
     */
    private void setReceiver() {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_SERVICE_FILTER);
        intentFilter.addAction(SERVICE_FILTER);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onStop() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onStop();
    }

    /**
     * Class MyReceiver is the class that overrides onReceive method of the broadcast
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(INTENT_SERVICE_FILTER) || intent.getAction().equals(SERVICE_FILTER)) {
                dialog.dismiss();
                finish();
            } else {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went Wrong in broadcast", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
