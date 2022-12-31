package com.example.admincollegeapp.faculty;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admincollegeapp.R;
import com.example.admincollegeapp.UploadImageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateFacultyInfoActivity extends AppCompatActivity {

    private final int REQ=1;
    private Bitmap bitmap=null;
    private EditText teacherName,teacherEmail,teacherPost;
    private Button teacherUpdateBtn,teacherDeleteBtn;
    private ProgressDialog pd;
    private ImageView teacherImage;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private String name,email,image,post,uniqueKey;
    private String category;
    private String downloadUrl;
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


         //----------------------- get data from intent-----------------//
        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        post=getIntent().getStringExtra("post");
        image=getIntent().getStringExtra("image");
        uniqueKey=getIntent().getStringExtra("key");
        category=getIntent().getStringExtra("category");
        //--------------------------------------------------------------//


       //------------setting data after getting from intent--------------------------------------//
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

//---------------------------------------------------------------------------------------------------------------------------------//


        teacherUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName,newEmail,newPost;

                newName=teacherName.getText().toString();
                newEmail=teacherEmail.getText().toString();
                newPost=teacherPost.getText().toString();

                extracted(newName, newEmail, newPost);
            }

            private void extracted(String newName, String newEmail, String newPost) {
                checkValidation(newName, newEmail, newPost);
            }


        });

        teacherDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }




        });














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


    private void checkValidation(String newName, String newEmail, String newPost) {

        if(newName.isEmpty())
        {
            teacherName.setError("Empty");
            teacherName.requestFocus();
        }
        else if(newEmail.isEmpty())
        {
            teacherEmail.setError("Empty");
            teacherEmail.requestFocus();
        }
        else if(newPost.isEmpty())
        {
            teacherPost.setError("Empty");
            teacherPost.requestFocus();
        }
        else if(bitmap==null)
        {
            updateData(newName,newEmail,newPost,image);

        }
        else
        {
           uploadImage(newName,newEmail,newPost);
        }





    }



    private void uploadImage(String newName, String newEmail, String newPost) {


        pd.setMessage("Uploading...");
        pd.show();


        ByteArrayOutputStream baos =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();

        final StorageReference filepath;
        filepath=storageReference.child("Teacher_Profile_img").child(category).child(finalimg+"jpg");

        final UploadTask uploadTask=filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UpdateFacultyInfoActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful())
                {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    downloadUrl=String.valueOf(uri);
                                    pd.dismiss();
                                    updateData(newName, newEmail, newPost,downloadUrl);
                                }
                            });
                        }
                    });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(UpdateFacultyInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });






    }

    private void updateData(String newName, String newEmail, String newPost, String image) {

        HashMap hp=new HashMap();

        hp.put("name",newName);
        hp.put("email",newEmail);
        hp.put("post",newPost);
        hp.put("image",image);

        reference.child("teachers").child(category).child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

                Toast.makeText(UpdateFacultyInfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(UpdateFacultyInfoActivity.this,UpdateFacultyActivity_And_Faculty_Database.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void deleteData() {

        reference.child("teachers").child(category).child(uniqueKey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateFacultyInfoActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                        Intent intent =new Intent(UpdateFacultyInfoActivity.this,UpdateFacultyActivity_And_Faculty_Database.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateFacultyInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}