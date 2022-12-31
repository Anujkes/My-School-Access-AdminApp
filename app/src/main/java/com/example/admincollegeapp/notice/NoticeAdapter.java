package com.example.admincollegeapp.notice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.admincollegeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NoticeData> list;

    public NoticeAdapter(Context context, ArrayList<NoticeData> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_item_layout,parent ,false);
       return new NoticeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.ViewHolder holder, int position) {
                    position=holder.getAdapterPosition();
      NoticeData curItem=list.get(position);

      holder.noticeTitle.setText(curItem.getTitle());


        try {

            if(!curItem.getImage().isEmpty())
            {
                Glide.
                        with(context)
                        .load(curItem.getImage())
                        .into(holder.noticeImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.noticeDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder =new AlertDialog.Builder(context);
                builder.setMessage("Are you sure want to delete this notice ? ");
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                {
                                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Notice");
                                    reference.child(curItem.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    notifyItemRemoved(holder.getAdapterPosition());
                                }
                            }
                        }
                );

                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              dialogInterface.cancel();
                            }
                        }
                );
                AlertDialog dialog=null;
                try {
                     dialog =builder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(dialog!=null)
                    dialog.show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       private ImageView noticeImage;
       private TextView noticeTitle;
       private Button noticeDeleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noticeImage=itemView.findViewById(R.id.noticeImage);
            noticeTitle=itemView.findViewById(R.id.noticeTitle);
            noticeDeleteBtn=itemView.findViewById(R.id.noticeDeleteBtn);
        }
    }
}
