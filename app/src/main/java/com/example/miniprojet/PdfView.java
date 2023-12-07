package com.example.miniprojet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojet.entites.Pdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PdfView extends AppCompatActivity {

    private TextView text1;
    private PDFView pdfView;

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference pdfDatabase=database.getReference("pdfs");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        pdfView = findViewById(R.id.pdfView);
        text1 = findViewById(R.id.text1);

        pdfDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s:snapshot.getChildren()){
                    if(s!=null && s.getKey().equals("wsinjZSicnagOtq5wkz4mLuHXz33")){
                Pdf pdf=s.getValue(Pdf.class);
                text1.setText(pdf.getName());
                new RetrivePdfStream().execute(pdf.getUrl());}}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PdfView.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });}

        class RetrivePdfStream extends AsyncTask<String,Void, InputStream>{

            @Override
            protected InputStream doInBackground(String... strings) {
                InputStream inputStream=null;
                try{
                    URL url=new URL(strings[0]);
                    HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                    if(urlConnection.getResponseCode()==200){
                        inputStream=new BufferedInputStream(urlConnection.getInputStream());
                    }
                }catch (IOException e){
                    return null;
                }
                return inputStream;
            }

            @Override
            protected void onPostExecute(InputStream inputStream){
                pdfView.fromStream(inputStream).load();
            }
        }
    }

