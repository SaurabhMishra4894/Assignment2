package com.example.studentmanager.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanager.R;
import com.example.studentmanager.activity.ViewStudentActivity;
import com.example.studentmanager.adapter.StudentAdapter;
import com.example.studentmanager.callback.OnStudentItemClick;
import com.example.studentmanager.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_CLICKED_POSITION;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_CLICKED_ROLLNUMBER;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_IS_FROM_ADD;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_IS_FROM_EDIT;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_IS_FROM_VIEW;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_STUDENT_OBJECT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowStudentListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowStudentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowStudentListFragment extends Fragment implements OnStudentItemClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int sGridView = 0;
    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private TextView mNoStudentMessage;
    protected StudentAdapter studentAdapter;
    protected ImageButton ibSortDetails;
    protected ArrayList<Student> studentList;


    public ShowStudentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ShowStudentListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowStudentListFragment newInstance() {
        ShowStudentListFragment fragment = new ShowStudentListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        studentList = new ArrayList<>();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_student_list, container, false);

        final RecyclerView studentRecyclerView = view.findViewById(R.id.rv_students_fragment_show_student);
        final ImageButton changeView = view.findViewById(R.id.ib_ChangeView_fragment_show_student);
        ibSortDetails = view.findViewById(R.id.ib_sort_fragment_show_student);

        mNoStudentMessage = view.findViewById(R.id.noStudentMessage);
        studentAdapter = new StudentAdapter(mContext, studentList, this);
        final RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(mContext);
        studentRecyclerView.setLayoutManager(layoutManager);
        studentRecyclerView.setAdapter(studentAdapter);
        refreshStudentList();




        /*
         * Icon sortDetails onClickListner pops up a popupmenu which will have options to
         * sortByName or SortByRollNumber
         */
        ibSortDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext, ibSortDetails);
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
                                Toast.makeText(mContext, getString(R.string.popUpError), Toast.LENGTH_SHORT).show();
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
        changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotateAnimation =
                        (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.rotate);
                changeView.startAnimation(rotateAnimation);
                if (sGridView == 0) {
                    sGridView = 1;
                    studentRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
                } else {
                    sGridView = 0;
                    studentRecyclerView.setLayoutManager(layoutManager);
                }


            }
        });


        /*
         * OnClickListener for the button to add Student
         *
         */

        final Button button = view.findViewById(R.id.btn_addStudent_fragment_show_Student);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();

                final Bundle bundle = new Bundle();
                bundle.putBoolean(INTENT_IS_FROM_ADD, true);
                intent.putExtras(bundle);


                mListener.onAddData(intent);
            }
        });
        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        /**
         * Method delete student from the database
         *
         * @param student
         * @return true is successfully deleted
         */
        boolean onStudentDelete(Student student);

        /**
         * Method fetches data from the database and pass them in the adapter
         *
         * @return List of students from database
         */
        List<Student> onRefreshStudentList();

        /**
         * Method change the fragment to another fragment and call method in that fragment
         * used for cummunication in fragments to pass editData
         *
         * @param intent
         */
        void onEditData(Intent intent);

        /**
         * Method change the fragment to another fragment and call method in that fragment
         * used for cummunication in fragments to pass addData
         *
         * @param intent
         */
        void onAddData(Intent intent);
    }

    @Override
    public void onItemClickListener(final int position) {
        final Dialog dialog = new Dialog(mContext);

        // Includes dialog_on_student_click.xml file
        View view = getLayoutInflater().inflate(R.layout.dilog_on_student_click, null);
        dialog.setContentView(view);


        // set values for custom dialog components - text, image and button
        Button viewButton = view.findViewById(R.id.btn_viewStudent_dilog);
        Button editButton = view.findViewById(R.id.btn_editStudent_dilog);
        Button deleteButton = view.findViewById(R.id.btn_deleteStudent_dilog);

        final Intent intent = new Intent(mContext, ViewStudentActivity.class);

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

                mListener.onEditData(intent);

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
                        new AlertDialog.Builder(mContext).create();

                deleteConfirmDialog.setTitle(getString(R.string.delete));
                deleteConfirmDialog.setMessage(getString(R.string.deleteConfirmation));
                // Set the action buttons
                deleteConfirmDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (!mListener.onStudentDelete(student)) {
                            Toast.makeText(mContext, getString(R.string.popUpError), Toast.LENGTH_SHORT).show();
                        }

                        refreshStudentList();
                        studentAdapter.notifyDataSetChanged();

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
     * Method refreshStudentList reloads students data from the database and restore them in
     * adapter and local ArrayList
     */
    public void refreshStudentList() {
        List<Student> students = mListener.onRefreshStudentList();
        studentList.clear();

        for (int i = 0; i < students.size(); i++) {
            Student student = new Student(students.get(i).getName(), students.get(i).getRoll_number(), students.get(i).getmClass());
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
