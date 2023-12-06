package com.example.miniprojet.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojet.R;
import com.example.miniprojet.entites.User;
import com.example.miniprojet.listJobsUser;

import java.util.ArrayList;
import java.util.List;

public class CompagnyAdapter extends RecyclerView.Adapter<CompagnyAdapter.MyViewHolder> {
    private Context context;

    private List<User> compagnyList;

    public CompagnyAdapter(Context context,List<User> compagnyList){
        this.context=context;
        this.compagnyList=compagnyList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_compagnies_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String idComp=compagnyList.get(position).getIdUser();
        holder.nameComp.setText(compagnyList.get(position).getFullName());
        holder.emailComp.setText(compagnyList.get(position).getEmail());
        holder.contactComp.setText(compagnyList.get(position).getContact());
        holder.summaryComp.setText(compagnyList.get(position).getSummary());
        if(holder.cardView!=null){
            holder.cardView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, listJobsUser.class);
                    String idCom = compagnyList.get(holder.getAdapterPosition()).getIdUser();
                    i.putExtra("idComp", compagnyList.get(holder.getAdapterPosition()).getIdUser());
                    i.putExtra("nomCom", compagnyList.get(holder.getAdapterPosition()).getFullName());


                    context.startActivity(i);


                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return compagnyList.size();
    }

    public void searchCompagny(ArrayList<User> compagnyList){
        this.compagnyList=compagnyList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameComp,emailComp,contactComp,summaryComp;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nameComp=itemView.findViewById(R.id.recTitle);
            emailComp=itemView.findViewById(R.id.recEmail);
            contactComp=itemView.findViewById(R.id.recContact);
            summaryComp=itemView.findViewById(R.id.recSumm);
            cardView = itemView.findViewById(R.id.recCard);
        }
    }
}
