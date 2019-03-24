package com.example.studentmanager.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_CLICKED_POSITION;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_CLICKED_ROLLNUMBER;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_IS_FROM_ADD;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_IS_FROM_EDIT;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_SERVICE_FILTER;
import static com.example.studentmanager.activity.ViewStudentActivity.INTENT_STUDENT_OBJECT;
import static com.example.studentmanager.activity.ViewStudentActivity.SERVICE_FILTER;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddStudentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddStudentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private EditText etName, etRollNumber, etClass;
    private Context mContext;
    private Button btnAddStudent;
    private Student mStudent;
    private boolean isFromEdit, isFromView, isFromAdd;
    private int mClickedPosition, mCLickedRollNumber;
    private DatabaseHelper mDatabaseHelper;
    MyReceiver myReceiver;
    private static Dialog dialog;


    private OnFragmentInteractionListener mListener;

    public AddStudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddStudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddStudentFragment newInstance() {
        AddStudentFragment fragment = new AddStudentFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_add_student, container, false);
        // Inflate the layout for this fragment

        etName = view.findViewById(R.id.name);
        etRollNumber = view.findViewById(R.id.rollNumber);
        etClass = view.findViewById(R.id.myClass);
        btnAddStudent = view.findViewById(R.id.addStudent);
        mDatabaseHelper = new DatabaseHelper(mContext);

        clearFields();


        /*
         * OnClickListner for addStudent Button
         * on the Basis of Token that is get from the ShowStudentActivity
         * it decides if the value is for EDIT or VIEW
         *
         */
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(mContext);

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
                                    Intent intent = new Intent(mContext, AddStudentSevice.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_EDIT, isFromEdit);
                                    bundle.putInt(INTENT_CLICKED_ROLLNUMBER, mCLickedRollNumber);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    clearFields();
                                    mContext.startService(intent);
                                } else {
                                    dialog.dismiss();
                                    etRollNumber.setError(getString(R.string.notUniqueRollNo));
                                }


                            } else if (isFromAdd) {
                                if (!mDatabaseHelper.rollNumberExists(Integer.parseInt(etRollNumber.getText().toString()))) {
                                    Intent intent = new Intent(mContext, AddStudentSevice.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_ADD, isFromAdd);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    clearFields();
                                    mContext.startService(intent);
                                } else {
                                    dialog.dismiss();
                                    etRollNumber.setError(getString(R.string.notUniqueRollNo));
                                }

                            } else {
                                Toast.makeText(mContext, R.string.popUpError, Toast.LENGTH_SHORT).show();
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
                                    Intent intent = new Intent(mContext, AddStudentIntentService.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_EDIT, isFromEdit);
                                    bundle.putInt(INTENT_CLICKED_ROLLNUMBER, mCLickedRollNumber);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    clearFields();
                                    mContext.startService(intent);

                                } else {
                                    dialog.dismiss();
                                    etRollNumber.setError(getString(R.string.notUniqueRollNo));
                                }

                            } else if (isFromAdd) {
                                if (!mDatabaseHelper.rollNumberExists(Integer.parseInt(etRollNumber.getText().toString()))) {
                                    Intent intent = new Intent(mContext, AddStudentIntentService.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_ADD, isFromAdd);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    clearFields();
                                    mContext.startService(intent);

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
                                    Intent intent = new Intent(mContext, AddStudentAsyncTask.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_EDIT, isFromEdit);
                                    bundle.putInt(INTENT_CLICKED_ROLLNUMBER, mCLickedRollNumber);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    new AddStudentAsyncTask(mContext).execute(intent);
                                    dialog.dismiss();
                                    String tag = "android:switcher:" + R.id.view_Pager_show_Student + ":" + 0;
                                    FragmentManager fm = getFragmentManager();
                                    ShowStudentListFragment showStudentListFragment = (ShowStudentListFragment) fm.findFragmentByTag(tag);
                                    showStudentListFragment.refreshStudentList();
                                    clearFields();
                                    mListener.onChangeTab();

                                } else {
                                    dialog.dismiss();
                                    etRollNumber.setError(getString(R.string.notUniqueRollNo));
                                }

                            } else if (isFromAdd) {
                                if (!mDatabaseHelper.rollNumberExists(Integer.parseInt(etRollNumber.getText().toString()))) {
                                    Intent intent = new Intent(mContext, AddStudentIntentService.class);
                                    Student newStudent = new Student(etName.getText().toString(),
                                            Integer.parseInt(etRollNumber.getText().toString()), Integer.parseInt(etClass.getText().toString()));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(INTENT_IS_FROM_ADD, isFromAdd);
                                    bundle.putParcelable(INTENT_STUDENT_OBJECT, newStudent);
                                    intent.putExtras(bundle);
                                    mContext.startService(intent);
                                    dialog.dismiss();
                                    String tag = "android:switcher:" + R.id.view_Pager_show_Student + ":" + 0;
                                    FragmentManager fm = getFragmentManager();
                                    ShowStudentListFragment showStudentListFragment = (ShowStudentListFragment) fm.findFragmentByTag(tag);
                                    showStudentListFragment.refreshStudentList();
                                    clearFields();
                                    mListener.onChangeTab();

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
        return view;

    }


    @Override
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
        setReceiver();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    /**
     * Method used to clear the texts in the EditText
     */
    public void clearFields() {
        etName.getText().clear();
        etClass.getText().clear();
        etRollNumber.getText().clear();
    }

    @Override
    public void onDetach() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(myReceiver);
        super.onDetach();
        mListener = null;
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(myReceiver);
        super.onStop();
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
        /**
         * onChangeTab move the tabs from one tab to another
         * If the Tab First is Selected in that case Tab 2 will be opened and vice-versa
         */
        void onChangeTab();

    }

    /**
     * Method onStudentAdd takes the intent and
     * checks id the intent is for add or not
     *
     * @param intent
     */
    public void onStudentAdd(Intent intent) {
        Bundle bundle = intent.getExtras();

        isFromAdd = bundle.getBoolean(INTENT_IS_FROM_ADD, false);
        btnAddStudent.setText(getString(R.string.add_student));
        mListener.onChangeTab();
    }

    /**
     * method onStudentEdit calls when user edit students data
     * the variable position is get from ShowStudentActivity in order to make sure when user
     * updates the data the student object on same position gets override
     */
    public void onStudentEdit(Intent intent) {
        Bundle bundle = intent.getExtras();
        mStudent = bundle.getParcelable(INTENT_STUDENT_OBJECT);
        mClickedPosition = bundle.getInt(INTENT_CLICKED_POSITION, -1);
        mCLickedRollNumber = bundle.getInt(INTENT_CLICKED_ROLLNUMBER, -1);
        isFromEdit = bundle.getBoolean(INTENT_IS_FROM_EDIT, false);
        etName.setText(mStudent.getName());
        etRollNumber.setText(String.valueOf(mStudent.getRoll_number()));
        etClass.setText(String.valueOf(mStudent.getmClass()));
        btnAddStudent.setText(getString(R.string.update));
        mListener.onChangeTab();
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
        LocalBroadcastManager.getInstance(mContext).registerReceiver(myReceiver, intentFilter);
    }

    /**
     * Class MyReceiver is the class that overrides onReceive method of the broadcast
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(INTENT_SERVICE_FILTER) || intent.getAction().equals(SERVICE_FILTER)) {
                dialog.dismiss();
                String tag = "android:switcher:" + R.id.view_Pager_show_Student + ":" + 0;
                FragmentManager fm = getFragmentManager();
                ShowStudentListFragment showStudentListFragment = (ShowStudentListFragment) fm.findFragmentByTag(tag);
                showStudentListFragment.refreshStudentList();
                mListener.onChangeTab();

            } else {
                dialog.dismiss();
                Toast.makeText(mContext, R.string.popUpError, Toast.LENGTH_SHORT).show();
            }


        }
    }
}
