package com.example.miniprojet.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojet.DetailsCandidates;
import com.example.miniprojet.R;
import com.example.miniprojet.entites.Job;
import com.example.miniprojet.entites.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.MyViewHolder>{
    private Context context;
    private List<User> UsersList;

    private FirebaseAuth authProfile;


    public CandidateAdapter(Context context, List<User> UsersList) {
        this.context = context;
        this.UsersList = UsersList;
    }
    @NonNull
    @Override
    public CandidateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidates_recyler, parent, false);

            return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String idUser = UsersList.get(position).getIdUser();
        User currentUser = UsersList.get(position);

        holder.tvusername.setText(currentUser.getFullName());
        holder.tvContact.setText(currentUser.getContact());

        // Example conversion for non-string data types
        holder.tvExperience.setText(String.valueOf(currentUser.getExperiences()));

        // Check for null before setting text
        if (currentUser.getSkills() != null) {
            holder.tvSkills.setText(currentUser.getSkills());
        } else {
            holder.tvSkills.setText("No skills available");
        }

        holder.tvidValue.setText(currentUser.getIdUser());

        if (holder.cardView != null) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, DetailsCandidates.class);
                    intent.putExtra("iduser", idUser);
                    context.startActivity(intent);

                }
            });}
    }



    @Override
    public int getItemCount() {
        return UsersList.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvusername, tvContact, tvExperience, tvSkills , tvidValue;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvusername = itemView.findViewById(R.id.recName);
            tvContact = itemView.findViewById(R.id.recContact);
            tvExperience = itemView.findViewById(R.id.recexp);
            tvSkills = itemView.findViewById(R.id.recSkills);
            cardView = itemView.findViewById(R.id.recCard);
            tvidValue = itemView.findViewById(R.id.recId);


        }
    }}

