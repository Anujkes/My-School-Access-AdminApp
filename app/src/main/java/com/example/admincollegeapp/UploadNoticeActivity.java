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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class UploadNoticeActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private  CardView selectImage;
    private Button uploadBtn;
   private final int REQ=1;
   private Bitmap bitmap;
   private ImageView image;
    private EditText title;
   private DatabaseReference reference;
   private StorageReference storageReference;
    private String downloadUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);


        selectImage=findViewById(R.id.selectImage);
        image=findViewById(R.id.image);
        title=findViewById(R.id.title);
        uploadBtn=findViewById(R.id.uploadBtn);
        pd=new ProgressDialog(this);
          reference= FirebaseDatabase.getInstance().getReference();
          storageReference= FirebaseStorage.getInstance().getReference();

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(title.getText().toString().isEmpty())
                {
                    title.setError("Empty");
                    title.requestFocus();
                }
                else if(bitmap==null)
                {
                   uploadData();// ye database me dalega
                }
                else
                {
                   uploadImage();// ye phle storage me fir waha se database me dalega : uploadDate() function ko call kr ke
                }


            }



        });




    }

//-----   ye hamari galery ki image ko firebase storage me store kr ke ek url dega-----------------//
    private void uploadImage() {

        pd.setMessage("Uploading...");
        pd.show();




        ByteArrayOutputStream baos =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();

        final StorageReference filepath;
        filepath=storageReference.child("Notice").child(finalimg+"jpg");

        final UploadTask uploadTask=filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UploadNoticeActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(UploadNoticeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
//------------------------------------------------------------------------------------------//



//------------jo url mila usko title ,date , ke sath RealTime database me store krenge-------------------//
    private void uploadData() {

        reference=reference.child("Notice");
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


        // title//
        String Title=title.getText().toString();

        UploadingData noticeData=new UploadingData(Title,downloadUrl,date,time,uniqeKey);


        reference.child(uniqeKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(UploadNoticeActivity.this, "Notice uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadNoticeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

  //--------------------------------------------------------------------------------------------------//











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
          image.setImageBitmap(bitmap);

        }



    }
}