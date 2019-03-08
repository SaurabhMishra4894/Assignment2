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
import com.example.studentmanager.util.Student;
import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Student> studentArrayList;
    private Context mContext;
    Dialog myDialog;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListener listner){
        mListener = listner;
    }

    /**
     * StudentAdapter constructor sets the values in the ArrayList and Context
     * @param context
     * @param studentList
     */

    public StudentAdapter(Context context,ArrayList<Student> studentList){
        studentArrayList = studentList;
        mContext = context;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,final int i) {
        View layoutInflater = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_student_view,viewGroup,false);
        return new StudentViewHolder(layoutInflater,mListener);

    }


    /**
     * function BindViewHolder in order to bind data to the ViewHolder
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        StudentViewHolder studentViewHolder = (StudentViewHolder) viewHolder;
        studentViewHolder.textViewName.setText(studentArrayList.get(i).getThisName());
        studentViewHolder.textViewClass.setText("Class :"+" "+String.valueOf(studentArrayList.get(i).getThisclass()));
        studentViewHolder.textViewRollno.setText("Roll No :"+" "+ String.valueOf(studentArrayList.get(i).getThisrollNumber()));


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

    public static class StudentViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewName;
        TextView textViewClass;
        TextView textViewRollno;

        /**
         * Student View Holder for StudentAdapter
         *
         * @param itemView
         * @param listener
         */
        public StudentViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.studentObjectName);
            textViewClass = itemView.findViewById(R.id.studentObjectClass);
            textViewRollno = itemView.findViewById(R.id.studentObjectRollNumber);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
