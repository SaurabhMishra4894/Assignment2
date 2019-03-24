package com.example.studentmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.studentmanager.R;
import com.example.studentmanager.adapter.ViewPagerAdapter;
import com.example.studentmanager.database.DatabaseHelper;
import com.example.studentmanager.fragment.AddStudentFragment;
import com.example.studentmanager.fragment.ShowStudentListFragment;
import com.example.studentmanager.model.Student;
import com.example.studentmanager.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class ShowStudentActivity extends AppCompatActivity implements AddStudentFragment.OnFragmentInteractionListener, ShowStudentListFragment.OnFragmentInteractionListener, Constants {


    private TextView mNoStudentMessage;
    private ViewPager mViewPager;
    protected ArrayList<Student> studentList;
    protected DatabaseHelper databaseHelper;


    /**
     * OnCreate Activity for ShowStudentActivity
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);

        // Initialising the variables with the value

        databaseHelper = new DatabaseHelper(this);

        mNoStudentMessage = findViewById(R.id.noStudentMessage);
        TabLayout tabLayout = findViewById(R.id.tab_layout_show_Student);

        mViewPager = findViewById(R.id.view_Pager_show_Student);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(mViewPager);

        studentList = new ArrayList<>();
    }


    @Override
    public boolean onStudentDelete(Student student) {
        mNoStudentMessage = findViewById(R.id.noStudentMessage);
        if (databaseHelper.deleteStudent(student.getRoll_number())) {

            if (studentList.size() == NULL_SIZE) {
                mNoStudentMessage.setVisibility(View.VISIBLE);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Student> onRefreshStudentList() {
        List<Student> students = databaseHelper.getAllStudents();
        return students;

    }

    @Override
    public void onChangeTab() {

        if (mViewPager.getCurrentItem() == SHOW_STUDENT_CODE) {
            mViewPager.setCurrentItem(ADD_STUDENT_CODE);
        } else {
            mViewPager.setCurrentItem(SHOW_STUDENT_CODE);
        }
    }


    @Override
    public void onEditData(Intent intent) {

        String tag = "android:switcher:" + R.id.view_Pager_show_Student + ":" + ADD_STUDENT_CODE;
        AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (addStudentFragment != null) {
            addStudentFragment.onStudentEdit(intent);
        }

    }

    @Override
    public void onAddData(Intent intent) {
        String tag = "android:switcher:" + R.id.view_Pager_show_Student + ":" + ADD_STUDENT_CODE;
        AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (addStudentFragment != null) {
            addStudentFragment.onStudentAdd(intent);
        }
    }


}
