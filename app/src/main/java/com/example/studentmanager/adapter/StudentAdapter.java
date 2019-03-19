package com.example.studentmanager.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentmanager.R;
import com.example.studentmanager.callback.OnStudentItemClick;
import com.example.studentmanager.model.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Student> studentArrayList;
    private Context mContext;
    Dialog myDialog;
    private OnStudentItemClick mListener;


    /**
     * StudentAdapter constructor sets the values in the ArrayList and Context
     *
     * @param context
     * @param studentList
     */

    public StudentAdapter(Context context, ArrayList<Student> studentList, final OnStudentItemClick listner) {
        studentArrayList = studentList;
        mContext = context;
        mListener = listner;
    }

    /**
     * Function CreateViewHolder in order to create ViewHolder
     *
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View layoutInflater = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_student_view, viewGroup, false);
        return new StudentViewHolder(layoutInflater);

    }


    /**
     * function BindViewHolder in order to bind data to the ViewHolder
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        StudentViewHolder studentViewHolder = (StudentViewHolder) viewHolder;
        studentViewHolder.textViewName.setText(studentArrayList.get(i).getName());
        studentViewHolder.textViewClass.setText("Class :" + " " + String.valueOf(studentArrayList.get(i).getmClass()));
        studentViewHolder.textViewRollno.setText("Roll No :" + " " + String.valueOf(studentArrayList.get(i).getRoll_number()));

        studentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClickListener(viewHolder.getAdapterPosition());
            }
        });

    }


    /**
     * method to return size of list
     *
     * @return studentArrayList size
     */
    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewClass;
        TextView textViewRollno;

        /**
         * Student View Holder for StudentAdapter
         *
         * @param itemView
         */
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.studentObjectName);
            textViewClass = itemView.findViewById(R.id.studentObjectClass);
            textViewRollno = itemView.findViewById(R.id.studentObjectRollNumber);

        }
    }

}
