package com.example.admincollegeapp.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admincollegeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoticeActivity extends AppCompatActivity {
 private RecyclerView noticeRecycler;
 private ProgressBar pd;
 private ArrayList<NoticeData> list;
 private NoticeAdapter ad;
 private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);

        noticeRecycler=findViewById(R.id.noticeRecycler);
        pd=findViewById(R.id.pd);
        reference= FirebaseDatabase.getInstance().getReference().child("Notice");
       


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list=new ArrayList<>();
                for(DataSnapshot s:snapshot.getChildren())
                {
                    NoticeData data=s.getValue(NoticeData.class);
                    list.add(data);
                }



                ad=new NoticeAdapter(DeleteNoticeActivity.this,list);
                ad.notifyDataSetChanged();

                pd.setVisibility(View.GONE);

                noticeRecycler.setLayoutManager(new LinearLayoutManager(DeleteNoticeActivity.this));
                noticeRecycler.setHasFixedSize(true);
                noticeRecycler.setAdapter(ad);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.setVisibility(View.GONE);
                Toast.makeText(DeleteNoticeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });








    }
}