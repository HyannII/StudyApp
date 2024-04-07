package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activities.ReadDocumentActivity;
import com.example.myapplication.Activities.YoutubePlayerActivity;
import com.example.myapplication.Models.CourseModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {
    Context context;
    ArrayList<CourseModel> cours;
    String action;
    public CourseAdapter(Context context, ArrayList<CourseModel> cours, String action){
        this.context = context;
        this.cours = cours;
        this.action = action;
    }

    @NonNull
    @Override
    public CourseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_list_items,parent,false);
        return new CourseAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.MyViewHolder holder, int position) {
        CourseModel courseModel = cours.get(position);

        holder.Tilte.setText(cours.get(position).getTitle());
        holder.imageView.setImageResource(cours.get(position).getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action.equals("read")) {
                    Intent intent = new Intent(context, ReadDocumentActivity.class);
                    intent.putExtra("name", courseModel.getTitle());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                } else if (action.equals("video")){
                    Intent intent = new Intent(context, YoutubePlayerActivity.class);
                    intent.putExtra("name", courseModel.getTitle());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cours.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView Tilte;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.imageView);
            Tilte = itemView.findViewById(R.id.txtDetail);
        }
    }
}
