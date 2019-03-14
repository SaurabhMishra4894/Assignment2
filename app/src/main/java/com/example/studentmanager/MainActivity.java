package com.example.studentmanager;

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

import com.example.studentmanager.activity.AddStudentActivity;
import com.example.studentmanager.adapter.StudentAdapter;
import com.example.studentmanager.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.studentmanager.activity.AddStudentActivity.INTENT_CLICKED_POSITION;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_IS_FROM_ADD;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_IS_FROM_EDIT;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_IS_FROM_VIEW;
import static com.example.studentmanager.activity.AddStudentActivity.INTENT_STUDENT_OBJECT;
import static com.example.studentmanager.util.Constants.REQ_CODE_ADD_STUDENT;
import static com.example.studentmanager.util.Constants.REQ_CODE_EDIT_STUDENT;

public class MainActivity extends AppCompatActivity {

    /**
     * VIEW_STUDENT if the user clicks on View option of a student
     * EDIT_STUDENT if the user clicks on Edit option of student
     * Constants VIEW_STUDENT and EDIT_STUDENT act as Token for next Activity
     * ArrayList<Student> act as the object of storing  student details
     */
    private static int sGridView = 0;
    private TextView mNoStudentMessage;
    protected ArrayList<Student> studentList;
    protected StudentAdapter studentAdapter;
    ImageButton ibSortDetails;


    /**
     * OnCreate Activity for Main Actrivity
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);
        final RecyclerView studentRecyclerView = findViewById(R.id.studentRecyclerView);
        final ImageButton imageView = findViewById(R.id.ibChangeView);

        // Initialising the valriables with the value

        ibSortDetails = findViewById(R.id.ibSort);
        studentList = new ArrayList<>();
        studentAdapter = new StudentAdapter(getApplicationContext(), studentList);
        final RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(MainActivity.this);
        studentRecyclerView.setLayoutManager(layoutManager);
        studentRecyclerView.setAdapter(studentAdapter);
        mNoStudentMessage = findViewById(R.id.noStudentMessage);


        ibSortDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, ibSortDetails);
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

        studentAdapter.setOnItemClickListner(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                final Dialog dialog = new Dialog(MainActivity.this);

                // Includes dialog_on_student_click.xml file
                View view = getLayoutInflater().inflate(R.layout.dilog_on_student_click, null);
                dialog.setContentView(view);


                // set values for custom dialog components - text, image and button
                Button viewButton = view.findViewById(R.id.viewStudent);
                Button editButton = view.findViewById(R.id.editStudent);
                Button deleteButton = view.findViewById(R.id.deleteStudent);

                final Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);

                final Bundle bundle = new Bundle();
                Student student = studentList.get(position);
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
                        bundle.putInt(INTENT_CLICKED_POSITION, position);
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
                                new AlertDialog.Builder(MainActivity.this).create();

                        deleteConfirmDialog.setTitle(getString(R.string.delete));
                        deleteConfirmDialog.setMessage(getString(R.string.deleteConfirmation));
                        // Set the action buttons
                        deleteConfirmDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                studentList.remove(position);
                                studentAdapter.notifyDataSetChanged();
                                if (studentList.size() == 0) {
                                    mNoStudentMessage.setVisibility(View.VISIBLE);
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
        if (data != null && resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_ADD_STUDENT) {
                Student student = data.getParcelableExtra(getString(R.string.studentObject));
                if (uniqueRollNo(student)) {
                    studentList.add(student);
                    studentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.notUniqueRollNo), Toast.LENGTH_SHORT).show();
                }


                if (studentList.size() > 0) {
                    mNoStudentMessage.setVisibility(View.GONE);
                }
            } else if (requestCode == REQ_CODE_EDIT_STUDENT) {
                Student student = data.getParcelableExtra(getString(R.string.studentObject));
                Bundle bundle = data.getExtras();
                int position = bundle.getInt(INTENT_CLICKED_POSITION, -1);
                if (uniqueRollNo(student) || studentList.get(position).getRollNumber() == student.getRollNumber()) {
                    studentList.set(position, student);
                    studentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.notUniqueRollNo), Toast.LENGTH_SHORT).show();
                }
                if (studentList.size() > 0) {
                    mNoStudentMessage.setVisibility(View.GONE);
                }
            }
        }
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
                return (Integer.parseInt(String.valueOf(o1.getRollNumber()))) - (Integer.parseInt(String.valueOf(o2.getRollNumber())));
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
            if (studentList.get(i).getRollNumber() == student.getRollNumber()) {
                return false;
            }
        }
        return true;
    }


}
