package com.example.admincollegeapp.faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.admincollegeapp.R;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    //1 data
    private List<TeacherData> list;
    private Context context;


    public TeacherAdapter(List<TeacherData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.faculty_item_layout,parent,false);
        return new TeacherAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAdapter.ViewHolder holder, int position) {

       TeacherData item=list.get(position);
        holder.teacherName.setText(item.getName());
        holder.teacherEmail.setText(item.getEmail());
        holder.teacherPost.setText(item.getPost());

        try {

            if(item.getImage().isEmpty())
            {
                Glide.
                        with(context)
                        .load("https://firebasestorage.googleapis.com/v0/b/admin-college-app-d29ea.appspot.com/o/AddedeData%2Funnamed.jpg?alt=media&token=5009e0e1-869a-47f3-826b-ed4a49814025")
                        .into(holder.teacherImage);

            }
            else {
                Glide.
                        with(context)
                        .load(item.getImage())
                        .into(holder.teacherImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.teacherUpdateBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(context, UpdateFacultyInfoActivity.class);
               i.putExtra("name",item.getName());
               i.putExtra("email",item.getEmail());
               i.putExtra("post",item.getPost());
               i.putExtra("image",item.getImage());

               context.startActivity(i);


           }
       });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView teacherName,teacherEmail,teacherPost;
        private Button teacherUpdateBtn;
        private ImageView teacherImage;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName =itemView.findViewById(R.id.teacherName);
            teacherEmail =itemView.findViewById(R.id.teacherEmail);
            teacherPost =itemView.findViewById(R.id.teacherPost);
            teacherUpdateBtn =itemView.findViewById(R.id.teacherUpdateBtn);
            teacherImage =itemView.findViewById(R.id.teacherImage);

        }
    }
}
