package com.example.admincollegeapp.faculty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.admincollegeapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class UpdateFacultyInfoActivity extends AppCompatActivity {

    private final int REQ=1;
    private Bitmap bitmap=null;
    private EditText teacherName,teacherEmail,teacherPost;
    private Button teacherUpdateBtn,teacherDeleteBtn;
    private ProgressDialog pd;
    private ImageView teacherImage;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private String name,email,image,post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty_info);


        teacherImage=findViewById(R.id.teacherImage);
        teacherName=findViewById(R.id.teacherName);
        teacherEmail=findViewById(R.id.teacherEmail);
        teacherPost=findViewById(R.id.teacherPost);
        teacherUpdateBtn=findViewById(R.id.teacherUpdateBtn);
        teacherDeleteBtn=findViewById(R.id.teacherDeleteBtn);
        reference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(this);


        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        post=getIntent().getStringExtra("post");
        image=getIntent().getStringExtra("image");


        teacherName.setText(name);
        teacherEmail.setText(email);
        teacherPost.setText(post);

        try {

            if(image.isEmpty())
            {
                Glide.
                        with(this)
                        .load("https://firebasestorage.googleapis.com/v0/b/admin-college-app-d29ea.appspot.com/o/AddedeData%2Funnamed.jpg?alt=media&token=5009e0e1-869a-47f3-826b-ed4a49814025")
                        .into(teacherImage);

            }
            else {
                Glide.
                        with(this)
                        .load(image)
                        .into(teacherImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        teacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });



    }
    private void openGallery() {

        Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK)
        {
            Uri uri =data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            teacherImage.setImageBitmap(bitmap);

        }



    }

}