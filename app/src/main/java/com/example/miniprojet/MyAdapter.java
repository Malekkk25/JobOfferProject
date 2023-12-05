package com.example.miniprojet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.miniprojet.entites.Job;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private Context context;
    private List<Job> jobList;

    public MyAdapter(Context context, List<Job> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Long idJob = jobList.get(position).getIdJob();
        holder.tvJobTitle.setText(jobList.get(position).getJobTitle());
        holder.tvJobDate.setText(jobList.get(position).getJobDate());
        holder.tvJobDescription.setText(jobList.get(position).getJobDescription());
        holder.tvJobLocation.setText(jobList.get(position).getJobLocation());
        holder.tvCategory.setText(jobList.get(position).getCategory());
        holder.tvExperience.setText(jobList.get(position).getExperience());
        holder.tvSkills.setText(jobList.get(position).getSkills());
        holder.tvidValue.setText(idJob != null ? String.valueOf(idJob): null);
        if (holder.cardView != null) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, JobDetails.class);
                    Long idJob = jobList.get(holder.getAdapterPosition()).getIdJob();
                    intent.putExtra("jobTitle", jobList.get(holder.getAdapterPosition()).getJobTitle());
                    intent.putExtra("jobDate", jobList.get(holder.getAdapterPosition()).getJobDate());
                    intent.putExtra("jobDescription", jobList.get(holder.getAdapterPosition()).getJobDescription());
                    intent.putExtra("jobLocation", jobList.get(holder.getAdapterPosition()).getJobLocation());
                    intent.putExtra("category", jobList.get(holder.getAdapterPosition()).getCategory());
                    intent.putExtra("experience", jobList.get(holder.getAdapterPosition()).getExperience());
                    intent.putExtra("skills", jobList.get(holder.getAdapterPosition()).getSkills());
                    intent.putExtra("idValue", idJob != null ? String.valueOf(idJob): null);
                    context.startActivity(intent);
                }
            });}

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void searchJob(ArrayList<Job> jobList) {
        this.jobList = jobList;
        notifyDataSetChanged();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvJobTitle, tvJobDate, tvJobDescription, tvJobLocation, tvCategory, tvExperience, tvSkills , tvidValue;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJobTitle = itemView.findViewById(R.id.recTitle);
            tvJobDate = itemView.findViewById(R.id.recdate);
            tvJobDescription = itemView.findViewById(R.id.recDescription);
            tvJobLocation = itemView.findViewById(R.id.recLocation);
            tvCategory = itemView.findViewById(R.id.recCategory);
            tvExperience = itemView.findViewById(R.id.recExperience);
            tvSkills = itemView.findViewById(R.id.recSkills);
            cardView = itemView.findViewById(R.id.recCard);
            tvidValue = itemView.findViewById(R.id.recId);


        }
    }}
