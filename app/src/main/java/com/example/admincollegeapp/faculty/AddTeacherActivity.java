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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admincollegeapp.R;
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

public class AddTeacherActivity extends AppCompatActivity {

    private ProgressDialog pd;
    private final int REQ=1;
    private Bitmap bitmap=null;
    private ImageView teacherImage;
    private EditText teacherName,teacherEmail,teacherPost;
    private Spinner techerCategorySpinner;
    private Button teacherAddBtn;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private String name,email,post,catergory,downloadUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        teacherImage=findViewById(R.id.teacherImage);
        teacherName=findViewById(R.id.teacherName);
        teacherEmail=findViewById(R.id.teacherEmail);
        teacherPost=findViewById(R.id.teacherPost);
        techerCategorySpinner=findViewById(R.id.techerCategorySpinner);
        teacherAddBtn=findViewById(R.id.teacherAddBtn);

        reference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(this);

        teacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });



          // category array//
        String [] items=new String[]{"Select Category","CSE","ECE","EE","ME","BioTechnology","Others"};
            //----------------------------//



        // spinner  and all----------------------------------------------------------------------//

        ArrayAdapter<String> spinnerAdp=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,items);
        techerCategorySpinner.setAdapter(spinnerAdp);

        techerCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catergory=techerCategorySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //--------------------------------------------------------------------------------------//






        teacherAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
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

    private void checkValidation() {
        name=teacherName.getText().toString();
        email=teacherEmail.getText().toString();
        post=teacherPost.getText().toString();

        if(name.isEmpty())
        {
            teacherName.setError("Empty");
            teacherName.requestFocus();
        }
        else if(email.isEmpty())
        {
            teacherEmail.setError("Empty");
            teacherEmail.requestFocus();
        }
        else if(post.isEmpty())
        {
            teacherPost.setError("Empty");
            teacherPost.requestFocus();
        }
        else if(catergory=="Select Category")
        {
            Toast.makeText(this, "Select category", Toast.LENGTH_SHORT).show();
        }
        else if(bitmap==null)
        {

            uploadData();// ye database me dalega
        }
        else
        {
            uploadImage();

            // ye phle storage me fir waha se database me dalega : uploadDate() function ko call kr ke
        }



    }

    //-----   ye hamari galery ki image ko firebase storage me store kr ke ek url dega-----------------//
    private void uploadImage() {

        pd.setMessage("Uploading...");
        pd.show();


        ByteArrayOutputStream baos =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();

        final StorageReference filepath;
        filepath=storageReference.child("Teacher_Profile_img").child(catergory).child(finalimg+"jpg");

        final UploadTask uploadTask=filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddTeacherActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadData();
                                }
                            });
                        }
                    });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(AddTeacherActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
//------------------------------------------------------------------------------------------//



    //------------jo url mila usko title ,date , ke sath RealTime database me store krenge-------------------//
    private void uploadData() {
        pd.setMessage("Uploading...");
        pd.show();
        DatabaseReference myRef;
        myRef=reference.child("teachers").child(catergory);
        final String uniqeKey = myRef.push().getKey();

        TeacherData teacherData=new TeacherData(name,email,post,downloadUrl,uniqeKey);

        myRef.child(uniqeKey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddTeacherActivity.this, "Profile Added successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddTeacherActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //--------------------------------------------------------------------------------------------------//

}