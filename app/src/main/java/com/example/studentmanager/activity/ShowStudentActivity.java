package com.example.studentmanager.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanager.R;
import com.example.studentmanager.adapter.StudentAdapter;
import com.example.studentmanager.callback.OnStudentItemClick;
import com.example.studentmanager.database.DatabaseHelper;
import com.example.studentmanager.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.studentmanager.activity.AddStudentActivity.INTENT_CLICKED_POSITION;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_CLICKED_ROLLNUMBER;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_IS_FROM_ADD;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_IS_FROM_EDIT;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_IS_FROM_VIEW;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_SEND_POSTION;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_STUDENT_OBJECT;
import static com.example.studentmanager.util.Constants.REQ_CODE_ADD_STUDENT;
import static com.example.studentmanager.util.Constants.REQ_CODE_EDIT_STUDENT;


public class ShowStudentActivity extends AppCompatActivity implements OnStudentItemClick {


    private static int sGridView = 0;
    private TextView mNoStudentMessage;
    protected ArrayList<Student> studentList;
    protected StudentAdapter studentAdapter;
    protected ImageButton ibSortDetails;
    protected DatabaseHelper databaseHelper;


    /**
     * OnCreate Activity for Main Activity
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);
        databaseHelper = new DatabaseHelper(this);


        final RecyclerView studentRecyclerView = findViewById(R.id.studentRecyclerView);
        final ImageButton imageView = findViewById(R.id.ibChangeView);

        // Initialising the valriables with the value

        ibSortDetails = findViewById(R.id.ibSort);
        studentList = new ArrayList<>();
        studentAdapter = new StudentAdapter(getApplicationContext(), studentList, this);
        final RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(ShowStudentActivity.this);
        studentRecyclerView.setLayoutManager(layoutManager);
        studentRecyclerView.setAdapter(studentAdapter);
        mNoStudentMessage = findViewById(R.id.noStudentMessage);


        refreshStudentList();

        ibSortDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ShowStudentActivity.this, ibSortDetails);
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
                                Toast.makeText(getApplicationContext(), getString(R.string.popUpError), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });










        /*
         * OnClickListner for changing the view of the layout
         *
         */
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotateAnimation =
                        (RotateAnimation) AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
                imageView.startAnimation(rotateAnimation);
                if (sGridView == 0) {
                    sGridView = 1;
                    studentRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                } else {
                    sGridView = 0;
                    studentRecyclerView.setLayoutManager(layoutManager);
                }


            }
        });


        /*

          OnClickListener for the button to add Student
          Opens a new Activity for result to get student details from User

         */

        final Button button = findViewById(R.id.addStudentButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddStudentActivity.class);
                intent.putExtra(INTENT_IS_FROM_ADD, true);
                startActivityForResult(intent, REQ_CODE_ADD_STUDENT);
            }
        });


    }


    /**
     * On Activity Result method in which result is get from AddStudentActivity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        refreshStudentList();
    }

    /**
     * Function Sort by Name sorts the student of ArrayList based on the Name of the student
     */
    private void sortByName() {
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        studentAdapter.notifyDataSetChanged();

    }

    /**
     * Function sort by RollNumber sorts the ArrayList based on the RollNumber
     */
    public void sortByRollNo() {
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return (Integer.parseInt(String.valueOf(o1.getRoll_number()))) - (Integer.parseInt(String.valueOf(o2.getRoll_number())));
            }
        });
        studentAdapter.notifyDataSetChanged();


    }

    /**
     * Function to check if the rollNumber is Unique or not
     *
     * @param student
     * @return
     */
    public boolean uniqueRollNo(Student student) {
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getRoll_number() == student.getRoll_number()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onItemClickListener(final int position) {
        final Dialog dialog = new Dialog(ShowStudentActivity.this);

        // Includes dialog_on_student_click.xml file
        View view = getLayoutInflater().inflate(R.layout.dilog_on_student_click, null);
        dialog.setContentView(view);


        // set values for custom dialog components - text, image and button
        Button viewButton = view.findViewById(R.id.viewStudent);
        Button editButton = view.findViewById(R.id.editStudent);
        Button deleteButton = view.findViewById(R.id.deleteStudent);

        final Intent intent = new Intent(ShowStudentActivity.this, AddStudentActivity.class);

        final Bundle bundle = new Bundle();
        final Student student = studentList.get(position);
        bundle.putParcelable(INTENT_STUDENT_OBJECT, student);

        /*
         * OnClickListner for VIEW Button
         * Opens a new activity and passes bundle in which student object is passed
         *
         */
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                bundle.putBoolean(INTENT_IS_FROM_VIEW, true);
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
                int rollNumber = studentList.get(position).getRoll_number();
                bundle.putInt(INTENT_CLICKED_POSITION, position);
                bundle.putInt(INTENT_CLICKED_ROLLNUMBER, rollNumber);
                bundle.putBoolean(INTENT_IS_FROM_EDIT, true);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQ_CODE_EDIT_STUDENT);


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
                        new AlertDialog.Builder(ShowStudentActivity.this).create();

                deleteConfirmDialog.setTitle(getString(R.string.delete));
                deleteConfirmDialog.setMessage(getString(R.string.deleteConfirmation));
                // Set the action buttons
                deleteConfirmDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (databaseHelper.deleteStudent(student.getRoll_number())) {
                            refreshStudentList();
                            studentAdapter.notifyDataSetChanged();
                            if (studentList.size() == 0) {
                                mNoStudentMessage.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.popUpError), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                deleteConfirmDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteConfirmDialog.dismiss();
                    }
                });

                deleteConfirmDialog.show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void refreshStudentList() {
        List<Student> Students = databaseHelper.getAllStudents();
        studentList.clear();

        for (int i = 0; i < Students.size(); i++) {
            Student student = new Student(Students.get(i).getName(), Students.get(i).getRoll_number(), Students.get(i).getmClass());
            studentList.add(student);
        }
        if (studentList.size() == 0) {
            mNoStudentMessage.setVisibility(View.VISIBLE);
        } else {
            mNoStudentMessage.setVisibility(View.GONE);
        }
        studentAdapter.notifyDataSetChanged();
    }
}
