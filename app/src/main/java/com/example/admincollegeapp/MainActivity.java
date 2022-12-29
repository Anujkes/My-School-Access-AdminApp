package com.example.admincollegeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView uploadNotice,uploadImage,uploadEbook,updateFaculty,deleteNotice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        uploadNotice=findViewById(R.id.uploadNotice);
        uploadImage=findViewById(R.id.uploadImage);
        uploadEbook=findViewById(R.id.uploadEbook);
        updateFaculty=findViewById(R.id.updateFaculty);
        deleteNotice=findViewById(R.id.deleteNotice);



    }
}