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
import com.example.myapplication.Models.DocumentModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {
    Context context;
    ArrayList<DocumentModel> documents;
    String action;
    public DocumentAdapter(Context context, ArrayList<DocumentModel> documents, String action){
        this.context = context;
        this.documents = documents;
        this.action = action;
    }

    @NonNull
    @Override
    public DocumentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_list_items,parent,false);
        return new DocumentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentAdapter.MyViewHolder holder, int position) {
        DocumentModel documentModel = documents.get(position);

        holder.Tilte.setText(documents.get(position).getTitle());
        holder.TitleContent.setText(documents.get(position).getTitleContent());
        holder.imageView.setImageResource(documents.get(position).getImage());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReadDocumentActivity.class);
            switch (action) {
                case "read": {
                    intent.putExtra("name", documentModel.getTitle());
                    intent.putExtra("position", position);
                    intent.putExtra("action", "read");
                    context.startActivity(intent);
                    break;
                }
                case "video": {
                    intent.putExtra("name", documentModel.getTitle());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                    break;
                }
                case "exercise": {
                    intent.putExtra("name", documentModel.getTitle());
                    intent.putExtra("content", documentModel.getTitleContent());
                    intent.putExtra("position", position);
                    intent.putExtra("action", "exercise");
                    context.startActivity(intent);
                    break;
                }
                case "introduction": {
                    intent.putExtra("name", documentModel.getTitle());
                    intent.putExtra("content", documentModel.getTitleContent());
                    intent.putExtra("position", position);
                    intent.putExtra("action", "introduction");
                    context.startActivity(intent);
                    break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView Tilte,TitleContent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.imageView);
            Tilte = itemView.findViewById(R.id.txtDetail);
            TitleContent = itemView.findViewById(R.id.txtContent);
        }
    }
}
