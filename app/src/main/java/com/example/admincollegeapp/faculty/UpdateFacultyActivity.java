package com.example.admincollegeapp.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.admincollegeapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UpdateFacultyActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView cseDept,eceDept,eeDept,meDept,btDept,othersDept;
    private LinearLayout cseNoData,eceNoData,eeNoData,meNoData,btNoData,othersNoData;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);



        fab=findViewById(R.id.fab);

        cseDept=findViewById(R.id.cseDept);
        eceDept=findViewById(R.id.eceDept);
        eeDept=findViewById(R.id.eeDept);
        meDept=findViewById(R.id.meDept);
        btDept=findViewById(R.id.btDept);
        othersDept=findViewById(R.id.othersDept);

        cseNoData = findViewById(R.id.cseNoData);
        eceNoData = findViewById(R.id.eceNoData);
        eeNoData = findViewById(R.id.eeNoData);
        meNoData = findViewById(R.id.meNoData);
        btNoData = findViewById(R.id.btNoData);
        othersNoData = findViewById(R.id.othersNoData);
        reference=FirebaseDatabase.getInstance().getReference().child("teachers");










      cseDepartment();
      eceDepartment();
      eeDepartment();
      meDepartment();
      btDepartment();
      othersDepartment();







        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateFacultyActivity.this,AddTeacherActivity.class));
            }
        });
    }


    //"Select Category","CSE","ECE","EE","ME","BioTechnology","Others"
    private void othersDepartment() {
        DatabaseReference myRef=reference.child("Others");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    othersNoData.setVisibility(View.VISIBLE);
                    othersDept.setVisibility(View.GONE);
                }
                else
                {

                    othersNoData.setVisibility(View.GONE);
                    othersDept.setVisibility(View.VISIBLE);

                    List<TeacherData> list=new ArrayList();
                    for(DataSnapshot snaps: snapshot.getChildren())
                    {
                        TeacherData data=snaps.getValue(TeacherData.class);
                        list.add(data);
                    }


                    othersDept.setHasFixedSize(true);
                    othersDept.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));

                    TeacherAdapter ad=new TeacherAdapter(list,UpdateFacultyActivity.this);

                    othersDept.setAdapter(ad);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });







    }

    private void btDepartment() {

        DatabaseReference myRef=reference.child("BioTechnology");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    btNoData.setVisibility(View.VISIBLE);
                    btDept.setVisibility(View.GONE);
                }
                else
                {

                   btNoData.setVisibility(View.GONE);
                  btDept.setVisibility(View.VISIBLE);

                    List<TeacherData> list=new ArrayList();
                    for(DataSnapshot snaps: snapshot.getChildren())
                    {
                        TeacherData data=snaps.getValue(TeacherData.class);
                        list.add(data);
                    }


                    btDept.setHasFixedSize(true);
                    btDept.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));

                    TeacherAdapter ad=new TeacherAdapter(list,UpdateFacultyActivity.this);

                    btDept.setAdapter(ad);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }
    private void meDepartment() {
        DatabaseReference myRef=reference.child("ME");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    meNoData.setVisibility(View.VISIBLE);
                    meDept.setVisibility(View.GONE);
                }
                else
                {

                    meNoData.setVisibility(View.GONE);
                    meDept.setVisibility(View.VISIBLE);

                    List<TeacherData> list=new ArrayList();
                    for(DataSnapshot snaps: snapshot.getChildren())
                    {
                        TeacherData data=snaps.getValue(TeacherData.class);
                        list.add(data);
                    }


                    meDept.setHasFixedSize(true);
                    meDept.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));

                    TeacherAdapter ad=new TeacherAdapter(list,UpdateFacultyActivity.this);

                    meDept.setAdapter(ad);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void eeDepartment() {
        DatabaseReference myRef=reference.child("EE");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    eeNoData.setVisibility(View.VISIBLE);
                    eeDept.setVisibility(View.GONE);
                }
                else
                {

                    eeNoData.setVisibility(View.GONE);
                    eeDept.setVisibility(View.VISIBLE);

                    List<TeacherData> list=new ArrayList();
                    for(DataSnapshot snaps: snapshot.getChildren())
                    {
                        TeacherData data=snaps.getValue(TeacherData.class);
                        list.add(data);
                    }


                    eeDept.setHasFixedSize(true);
                    eeDept.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));

                    TeacherAdapter ad=new TeacherAdapter(list,UpdateFacultyActivity.this);

                    eeDept.setAdapter(ad);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });















    }

    private void eceDepartment() {

        DatabaseReference myRef=reference.child("ECE");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    eceNoData.setVisibility(View.VISIBLE);
                    eceDept.setVisibility(View.GONE);
                }
                else
                {

                    eceNoData.setVisibility(View.GONE);
                    eceDept.setVisibility(View.VISIBLE);

                    List<TeacherData> list=new ArrayList();
                    for(DataSnapshot snaps: snapshot.getChildren())
                    {
                        TeacherData data=snaps.getValue(TeacherData.class);
                        list.add(data);
                    }


                    eceDept.setHasFixedSize(true);
                    eceDept.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));

                    TeacherAdapter ad=new TeacherAdapter(list,UpdateFacultyActivity.this);

                    eceDept.setAdapter(ad);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });










    }

    private void cseDepartment() {

         DatabaseReference myRef=reference.child("CSE");
         myRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 if(!snapshot.exists())
                 {
                     cseNoData.setVisibility(View.VISIBLE);
                     cseDept.setVisibility(View.GONE);
                 }
                 else
                 {

                     cseNoData.setVisibility(View.GONE);
                     cseDept.setVisibility(View.VISIBLE);

                     List<TeacherData> list=new ArrayList();
                    for(DataSnapshot snaps: snapshot.getChildren())
                    {
                        TeacherData data=snaps.getValue(TeacherData.class);
                        list.add(data);
                    }


                    cseDept.setHasFixedSize(true);
                    cseDept.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));

                    TeacherAdapter ad=new TeacherAdapter(list,UpdateFacultyActivity.this);

                     cseDept.setAdapter(ad);


                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });





    }
}