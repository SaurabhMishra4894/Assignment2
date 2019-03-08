package com.example.studentmanager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanager.activity.AddStudentActivity;
import com.example.studentmanager.adapter.StudentAdapter;
import com.example.studentmanager.util.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    /**
     * VIEW_STUDENT if the user clicks on View option of a student
     * EDIT_STUDENT if the user clicks on Edit option of student
     * Constants VIEW_STUDENT and EDIT_STUDENT act as Token for next Activity
     * ArrayList<Student> act as the object of storing  student details
     *
     */

    public static final int VIEW_STUDENT = 101;
    public static final int EDIT_STUDENT = 102;
    private static int sGridView = 0;
    private TextView mNoStudentMessage;
    protected ArrayList<Student> studentList;
    protected StudentAdapter studentAdapter;
    protected int ADD_STUDENT = 10;
    ImageButton sortDetails;


    /**
     * OnCreate Activity for Main Actrivity
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);

        // Initialising the valriables with the value

        sortDetails = findViewById(R.id.ibSort);
        studentList = new ArrayList<>();
        studentAdapter =
                new StudentAdapter(getApplicationContext(),studentList);
        final RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(MainActivity.this);



        sortDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, sortDetails);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.option_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.MenuOptionSortByName:
                                sortByName();
                                return true;
                            case R.id.MenuOptionSortByRollNumber:
                                sortByRollNo();
                                return true;
                            default:
                                Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });





        final RecyclerView studentRecyclerView = findViewById(R.id.studentRecyclerView);
        studentRecyclerView.setLayoutManager(layoutManager);
        studentRecyclerView.setAdapter(studentAdapter);
        mNoStudentMessage = findViewById(R.id.noStudentMessage);
        final ImageButton imageView = findViewById(R.id.ibChangeView);


        /*
         * OnClickListner for changing the view of the layout
         *
         */
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotateAnimation =
                        (RotateAnimation) AnimationUtils.loadAnimation( getBaseContext() , R.anim.rotate );
                imageView.startAnimation(rotateAnimation);
                if(sGridView == 0){
                    sGridView =1;
                    studentRecyclerView.setLayoutManager( new GridLayoutManager ( getApplicationContext(), 2));
                }
                else{
                    sGridView=0;
                    studentRecyclerView.setLayoutManager(layoutManager);
                }


            }
        });


        /**
         *
         * OnClickListener for the button to add Student
         * Opens a new Activity for result to get student details from User
         *
         */

        final Button button = findViewById(R.id.addStudentButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddStudentActivity.class);
                startActivityForResult(intent,ADD_STUDENT);
            }
        });

        studentAdapter.setOnItemClickListner(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                final Dialog dialog = new Dialog(MainActivity.this);

                // Includes dialog_on_student_click.xml file
                View view =  getLayoutInflater().inflate(R.layout.dilog_on_student_click,null);
                dialog.setContentView(view);
                // Set dialog title
                dialog.setTitle("Custom Dialog");

                // set values for custom dialog components - text, image and button
                Button viewButton = view.findViewById(R.id.viewStudent);
                Button editButton = view.findViewById(R.id.editStudent);
                Button deleteButton = view.findViewById(R.id.deleteStudent);

                final Intent intent =
                        new Intent(MainActivity.this, AddStudentActivity.class);

                final Bundle bundle = new Bundle();
                Student student = studentList.get(position);

                bundle.putParcelable("StudentObject",student);

                /*
                 * OnClickListner for VIEW Button
                 * Opens a new activity and passes bundle in which student object is passed
                 *
                 */
                viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        bundle.putInt("Token",VIEW_STUDENT);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                /*
                 * OnClickListner for EDIT Button
                 * Opens a new activity and passes bundle in which student object is passed
                 * as well as get student which was edited and
                 *
                 */
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        bundle.putInt("Position",position);

                        bundle.putInt("Token",EDIT_STUDENT);
                        intent.putExtras(bundle);
                        startActivityForResult(intent,11);


                    }
                });
                /*
                 * OnClickListner for DELETE Button
                 * Opens a dialog in which confirmation to delete student is confirmed
                 *
                 */
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final AlertDialog deleteConfirmDialog =
                                new AlertDialog.Builder(MainActivity.this).create();

                        deleteConfirmDialog.setTitle("Delete");
                        deleteConfirmDialog.setMessage("Are you sure want to delete ?");
                                // Set the action buttons
                                deleteConfirmDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        studentList.remove(position);
                                        studentAdapter.notifyDataSetChanged();
                                        if(studentList.size() ==0){
                                            mNoStudentMessage.setVisibility(View.VISIBLE);
                                        }

                                    }
                                });

                                deleteConfirmDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        deleteConfirmDialog.dismiss();
                                    }
                                });
                        //
                        deleteConfirmDialog.show();
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });

    }


    /**
     *
     * On Activity Result method in which result is get from AddStudentActivity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if(requestCode ==10){
                Student student = data.getParcelableExtra("studentObject");
                if(uniqueRollNo(student)){
                    studentList.add(student);
                    studentAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Not a unique roll number",Toast.LENGTH_SHORT).show();
                }


                if(studentList.size() >0){
                    mNoStudentMessage.setVisibility(View.GONE);
                }
                Log.i("MyTag", String.valueOf(studentList.get(0).getThisName()));
            }
            else if(requestCode == 11){
                Student student = data.getParcelableExtra("studentObject");
                Bundle bundle = data.getExtras();
                int position = bundle.getInt("Position");
                if(uniqueRollNo(student) || studentList.get(position).getThisrollNumber() == student.getThisrollNumber()){
                    studentList.set(position,student);
                    studentAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Not a unique roll number",Toast.LENGTH_SHORT).show();
                }


                if(studentList.size() >0){
                    mNoStudentMessage.setVisibility(View.GONE);
                }
            }

        }

    }

    /**
     * Function Sort by Name sorts the student of ArrayList based on the Name of the student
     *
     */
    private void sortByName(){
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getThisName().compareToIgnoreCase(o2.getThisName());
            }
        });
        studentAdapter.notifyDataSetChanged();

    }

    /**
     * Function sort by RollNumber sorts the ArrayList based on the RollNumber
     *
     */
    public void sortByRollNo(){
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return (Integer.parseInt(String.valueOf(o1.getThisrollNumber())))-(Integer.parseInt(String.valueOf(o2.getThisrollNumber())));
            }
        });
        studentAdapter.notifyDataSetChanged();


    }
    public boolean uniqueRollNo(Student student){
        for(int i=0;i<studentList.size();i++){
            if(studentList.get(i).getThisrollNumber() == student.getThisrollNumber()){
                return false;
            }
        }
        return true;
    }


}
