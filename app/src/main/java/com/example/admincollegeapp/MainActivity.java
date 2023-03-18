package com.example.admincollegeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.admincollegeapp.faculty.UpdateFacultyActivity_And_Faculty_Database;
import com.example.admincollegeapp.notice.DeleteNoticeActivity;
import com.example.admincollegeapp.notice.UploadNoticeActivity;

public class MainActivity extends AppCompatActivity {

    private CardView uploadNotice,uploadImage,uploadEbook,updateFaculty,deleteNotice,logout;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        uploadNotice=findViewById(R.id.uploadNotice);
        uploadImage=findViewById(R.id.uploadImage);
        uploadEbook=findViewById(R.id.uploadEbook);
        updateFaculty=findViewById(R.id.updateFaculty);
        deleteNotice=findViewById(R.id.deleteNotice);
        logout=findViewById(R.id.logout);


        sharedPreference=this.getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreference.edit();




        uploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, UploadNoticeActivity.class));
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, UploadImageActivity.class));
            }
        });

        uploadEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, UploadPdfActivity.class));
            }
        });

        updateFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, UpdateFacultyActivity_And_Faculty_Database.class));
            }
        });

        deleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DeleteNoticeActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("isLogin","false");
                editor.commit();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

    }
}