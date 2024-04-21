package com.example.myapplication.Adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.ResultModel;
import com.example.myapplication.R;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
    Context context;
    ArrayList<ResultModel> results;
    public ResultAdapter(Context context, ArrayList<ResultModel> results) {
        this.context = context;
        this.results = results;
    }
    @NonNull
    @Override
    public ResultAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_result_list_items,parent,false);
        return new ResultAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.MyViewHolder holder, int position) {
        holder.examineeName.setText("Examinee: " + results.get(position).getUserName());
        Timestamp timestamp = results.get(position).getStartTime(); // Thay thế ... bằng timestamp từ Firestore

        // Định dạng ngày/tháng/năm và giờ/phút/giây
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        // Chuyển đổi timestamp thành định dạng văn bản
        String formattedDate = sdf.format(new Date(timestamp.getSeconds() * 1000));
        holder.startTime.setText("Start time: " + formattedDate);
        holder.correctNum.setText(String.valueOf(results.get(position).getCorrect()));
        holder.wrongNum.setText(String.valueOf(results.get(position).getWrong()));
        holder.timeLeft.setText(results.get(position).getTimeLeft());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public ArrayList<ResultModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultModel> results) {
        this.results = results;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView examineeName,startTime,correctNum,wrongNum,timeLeft;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            examineeName = itemView.findViewById(R.id.txtExemineeName);
            startTime = itemView.findViewById(R.id.txtStartTime);
            correctNum = itemView.findViewById(R.id.txtCorrectNum);
            wrongNum = itemView.findViewById(R.id.txtWrongNum);
            timeLeft = itemView.findViewById(R.id.txtTimeLeft);
        }
    }
}
