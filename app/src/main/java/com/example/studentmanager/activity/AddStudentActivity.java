package com.example.studentmanager.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.studentmanager.R;
import com.example.studentmanager.util.Student;
import com.example.studentmanager.util.Validate;

import static com.example.studentmanager.MainActivity.EDIT_STUDENT;
import static com.example.studentmanager.MainActivity.VIEW_STUDENT;

/**
 * AddStudentActivity where user add details about the student and saves it
 * The same activity is shown to the user when user view/Edit student data
 *
 */
public class AddStudentActivity extends AppCompatActivity {

    EditText getName;
    EditText getRollNumber;
    EditText getClass;
    Button addStudent;
    Bundle mbundle;
    Student mStudent;
    int POSITION;

    /**
     * onCreate method for Add Student Activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        getName = findViewById(R.id.name);
        getRollNumber = findViewById(R.id.rollNumber);
        getClass = findViewById(R.id.myClass);
        addStudent = findViewById(R.id.addStudent);


        /*
         * If the activity is opening for Edit/View Intent != null is true
         * A token is passed by the main activity will checks of the activity is calling for edit/View
         *
         */



        if(getIntent().getExtras() != null){
            Intent intent = getIntent();
            mbundle = intent.getExtras();
            if(mbundle != null){
                Student student = mbundle.getParcelable("StudentObject");
                if(mbundle.getInt("Token") == VIEW_STUDENT){
                    onStudentView(student);
                }
                else if(mbundle.getInt("Token") == EDIT_STUDENT){
                    onStudentEdit(student,mbundle.getInt("Position"));
                }
                
            }


        }


        /*
         * OnClickListner for addStudent Button
         * on the Basis of Token that is get from the MainActivity
         * it decides if the value is for EDIT or VIEW
         *
         */
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(mbundle.getInt("Token") == EDIT_STUDENT){
                        if(validateFields()){
                            Student updatedStudent = new Student(getName.getText().toString(),Integer.parseInt(getRollNumber.getText().toString()),Integer.parseInt(getClass.getText().toString()));
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Position",POSITION);
                            bundle.putParcelable("studentObject",updatedStudent);
                            intent.putExtras(bundle);
                            setResult(RESULT_OK,intent);
                            finish();
                        }


                    }
                }
                catch (Exception e){
                    if(validateFields()){
                        Student student = new Student(getName.getText().toString(),Integer.parseInt(getRollNumber.getText().toString()),Integer.parseInt(getClass.getText().toString()));
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("studentObject",student);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                }




            }
        });

    }

    /**
     * method onStudentView which calls when user View students data
     *
     * @param student
     *
     */
    public void onStudentView(Student student){
        getName.setEnabled(false);
        getRollNumber.setEnabled(false);
        getClass.setEnabled(false);
        addStudent.setVisibility(View.GONE);
        getName.setText(student.getThisName());
        getRollNumber.setText(String.valueOf(student.getThisrollNumber()));
        getClass.setText(String.valueOf(student.getThisclass()));
    }

    /**
     * method onStudentEdit calls when user edit students data
     * the variable position is get from MainActivity in order to make sure when user
     * updates the data the student object on same position gets override
     *
     *
     * @param student
     * @param position
     */
    public void onStudentEdit(final Student student,int position){
        POSITION = position;
        getName.setText(student.getThisName());
        getRollNumber.setText(String.valueOf(student.getThisrollNumber()));
        getClass.setText(String.valueOf(student.getThisclass()));
        addStudent.setText("Update");

    }

    /**
     * Validate Fiels validate Name/Roll Number/Class of the student with a check that
     * Name should not have special characters or spaces
     * Roll Number should lie between 1 and 9999
     * class should be between 1 to 12
     *
     * @return true if valid
     *
     */
    public boolean validateFields(){
        Validate validate = new Validate();
        if(!validate.stringValidate(getName.getText().toString())){
            getName.setError("Thats not a valid Name");
        }
        else{
            if(!validate.rollNumberValidate(Integer.parseInt(getRollNumber.getText().toString()))){
                getRollNumber.setError("Enter a valid Roll Number between 1 and 9999");
            }
            else{
                if(!validate.classValidate(Integer.parseInt(getClass.getText().toString()))){
                    getClass.setError("Enter a valid Class between 1 and 12");
                }
                else{
                    return true;
                }
            }
        }

        return false;
    }



}
