package com.example.admincollegeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincollegeapp.faculty.UpdateFacultyActivity_And_Faculty_Database;
import com.example.admincollegeapp.faculty.UpdateFacultyInfoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Student> list;

    public PendingRequestAdapter(Context context, ArrayList<Student> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public PendingRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.pending_request_item_layout,parent,false);
        return new PendingRequestAdapter.ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull PendingRequestAdapter.ViewHolder holder, int position) {

       Student student=list.get(position);

       holder.name.setText(student.getName());
       holder.email.setText(student.getEmail());
       holder.mobile_no.setText(student.getMobile_no());
       holder.student_id.setText(student.getStudent_id());
       holder.uid.setText(student.getUid());

       holder.verify.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               FirebaseDatabase db=FirebaseDatabase.getInstance();
               DatabaseReference ref=db.getReference();


             ref.child("users").child(student.getStudent_id()).child("status").setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {



                DatabaseReference rf=FirebaseDatabase.getInstance().getReference();
                rf.child("STATUS").child(student.getUid()).setValue("yes")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, student.getName() + " : verified successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });



           }
       });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public  class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,email,student_id,mobile_no,uid;
        private Button verify;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.name);
            email =itemView.findViewById(R.id.email);
            student_id =itemView.findViewById(R.id.student_id);
            mobile_no =itemView.findViewById(R.id.mobile_no);
            verify=itemView.findViewById(R.id.verify);
            uid=itemView.findViewById(R.id.uid);


        }
    }

}
