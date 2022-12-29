package com.example.admincollegeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

        CardView uploadNotice,uploadImage,uploadEbook,updateFaculty,deleteNotice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        uploadNotice=findViewById(R.id.uploadNotice);
        uploadImage=findViewById(R.id.uploadImage);
        uploadEbook=findViewById(R.id.uploadEbook);
        updateFaculty=findViewById(R.id.updateFaculty);
        deleteNotice=findViewById(R.id.deleteNotice);


        uploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,UploadNotice.class));
            }
        });



    }
}