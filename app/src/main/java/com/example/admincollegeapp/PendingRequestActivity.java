package com.example.admincollegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.admincollegeapp.faculty.TeacherAdapter;
import com.example.admincollegeapp.faculty.TeacherData;
import com.example.admincollegeapp.faculty.UpdateFacultyActivity_And_Faculty_Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingRequestActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private RecyclerView pending_req_Recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request);


        db=FirebaseDatabase.getInstance();
        ref=db.getReference();

        pending_req_Recycler=findViewById(R.id.pending_req_Recycler);




        DatabaseReference myRef=ref.child("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Student> list=new ArrayList<>();

                for(DataSnapshot snaps:snapshot.getChildren())
                {
                    Student student=snaps.getValue(Student.class);

                    if(student.getStatus().equals("no"))
                         list.add(student);
                }




                   pending_req_Recycler.setHasFixedSize(true);
                   pending_req_Recycler.setLayoutManager(new LinearLayoutManager(PendingRequestActivity.this));

                PendingRequestAdapter ad=new PendingRequestAdapter(PendingRequestActivity.this,list);

                        pending_req_Recycler.setAdapter(ad);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PendingRequestActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });






    }
}