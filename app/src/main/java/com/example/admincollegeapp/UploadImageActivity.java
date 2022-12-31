package com.example.admincollegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admincollegeapp.notice.NoticeData;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadImageActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private final int REQ=1;
    private Bitmap bitmap=null;
    private  Spinner category;
    private CardView galleryImage;
    private ImageView imageView;
    private Button uploadImageBtn;
   private String categorySelected="";
    private DatabaseReference reference;
    private StorageReference storageReference;
    private String downloadUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        category=findViewById(R.id.category);
        galleryImage=findViewById(R.id.galleryImage);
        imageView=findViewById(R.id.imageView);
        uploadImageBtn=findViewById(R.id.uploadImageBtn);
        pd=new ProgressDialog(this);


        reference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();


        // category array//
       String [] items=new String[]{"Select Category","Convocation","Independence Day","Others"};
//----------------------------//



 // spinner  and all----------------------------------------------------------------------//

        ArrayAdapter<String> spinnerAdp=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,items);
        category.setAdapter(spinnerAdp);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categorySelected=category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


  //--------------------------------------------------------------------------------------//


        galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });




        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if(bitmap==null)
                {
                    Toast.makeText(UploadImageActivity.this, "Please Upload Image", Toast.LENGTH_SHORT).show();
                }
              else if(categorySelected=="" || categorySelected=="Select Category")
              {
                  Toast.makeText(UploadImageActivity.this, "Select category", Toast.LENGTH_SHORT).show();
              }
                else
                {
                    pd.setMessage("Uploading...");
                    pd.show();
                    uploadImage();
                }


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
            imageView.setImageBitmap(bitmap);

        }



    }



    //-----   ye hamari galery ki image ko firebase storage me store kr ke ek url dega-----------------//
    private void uploadImage() {

        ByteArrayOutputStream baos =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();

        final StorageReference filepath;
        filepath=storageReference.child("Gallery").child(finalimg+"jpg");

        final UploadTask uploadTask=filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UploadImageActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadData();
                                }
                            });
                        }
                    });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(UploadImageActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
//------------------------------------------------------------------------------------------//



    //------------jo url mila usko title ,date , ke sath RealTime database me store krenge-------------------//
    private void uploadData() {

        reference=reference.child("Gallery").child(categorySelected);
        final String uniqeKey = reference.push().getKey();


        //-----------for date and time-------------------//

        //date//
        Calendar calfordate= Calendar.getInstance();
        SimpleDateFormat curdate=new SimpleDateFormat("dd-MM-yy");
        String date = curdate.format(calfordate.getTime());


        //time//

        Calendar calfortime= Calendar.getInstance();
        SimpleDateFormat curtime=new SimpleDateFormat("hh-mm a");
        String time = curdate.format(calfortime.getTime());


        NoticeData ImageData=new NoticeData(categorySelected,downloadUrl,date,time,uniqeKey);


        reference.child(uniqeKey).setValue(ImageData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(UploadImageActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadImageActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //--------------------------------------------------------------------------------------------------//



}