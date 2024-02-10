package com.example.myapplicationmanagment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationmanagment.ExamItem;
import com.example.myapplicationmanagment.R;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    private List<ExamItem> examItemList;

    public ExamAdapter(List<ExamItem> examItemList) {
        this.examItemList = examItemList;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_item, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        ExamItem examItem = examItemList.get(position);
        holder.bind(examItem);
    }

    @Override
    public int getItemCount() {
        return examItemList.size();
    }

    static class ExamViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewSubject;
        private TextView textViewDate;
        private TextView textViewTime;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubject = itemView.findViewById(R.id.textViewSubject);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }

        public void bind(ExamItem examItem) {
            textViewSubject.setText(examItem.getSubject());
            textViewDate.setText(examItem.getDate());
            textViewTime.setText(examItem.getTime());
        }
    }
}
