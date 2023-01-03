package com.example.admincollegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UploadPdfActivity extends AppCompatActivity {
    private String title;
    private Button uploadPdfBtn;
    private CardView uploadPdf;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private final int REQ=1;
    private Uri pdfData=null;
    private EditText pdftitle;

    private ProgressDialog pd;
    private TextView pdftextview;
    String pdfname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);



        uploadPdfBtn=findViewById(R.id.uploadPdfBtn);
        uploadPdf=findViewById(R.id.uploadPdf);
        pd=new ProgressDialog(this);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        pdftitle=findViewById(R.id.pdftitle);
        pdftextview=findViewById(R.id.pdftextview);



        uploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });



//---------------------uploade Pdf Button ke click krne pr----------------------------//

        uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              title=pdftitle.getText().toString();
              if(pdfData==null)
                  Toast.makeText(UploadPdfActivity.this, "Upload any pdf", Toast.LENGTH_SHORT).show();
              else   if(title.isEmpty())
              {
                  pdftitle.setError("Empty");
                  pdftitle.requestFocus();
              }
              else
              {
                 pd.setMessage("Uploading...");
                 pd.show();
                uploadmyPdf();

              }

            }
        });

//------------------------------------------------------------------------------------//
    }



//----------------------- pdf select from gallery-------------------------------------------------------//

    private void openGallery() {

        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf file"),REQ);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK)
        {
            pdfData=data.getData();

            //--------------pdf ka name textview me show karane ke liye------------------//
            if(pdfData.toString().startsWith("content://"))
            {
                Cursor cursor = null;
                try {
                    cursor=UploadPdfActivity.this.getContentResolver().query(pdfData,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst())
                        pdfname=cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                        pdftextview.setText(pdfname);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

            }
            else if(pdfData.toString().startsWith("file://"))
                pdfname=new File(pdfData.toString()).getName();
            pdftextview.setText(pdfname);
         //-------------------------------------------------------------------------------------//
        }



    }
 //---------------------------------------------------------------------------------------------------------------//



    private void uploadmyPdf() {
        storageReference=storageReference.child("Pdf").child(""+pdfname+" "+System.currentTimeMillis()+".pdf");

        storageReference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());

                        Uri uri= uriTask.getResult();
                        uploaData(String.valueOf(uri));

                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UploadPdfActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });


    }
    private void uploaData(String downloadPdfUrl) {

        DatabaseReference myRef;
        myRef=databaseReference.child("Pdf");
        final String uniqeKey =databaseReference.push().getKey();


        EbookData data=new EbookData(title,downloadPdfUrl);




        myRef.child(uniqeKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadPdfActivity.this, "Pdf uploaded successfully", Toast.LENGTH_SHORT).show();
                pdftitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadPdfActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });



    }
}