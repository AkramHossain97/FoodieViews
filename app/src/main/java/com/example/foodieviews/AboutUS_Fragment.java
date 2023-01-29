package com.example.foodieviews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sarnava.textwriter.TextWriter;

public class AboutUS_Fragment extends Fragment {
    FloatingActionButton NewMail ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.about_us_fragment, container, false);


        NewMail=root.findViewById(R.id.fabEmail);


        NewMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "foodieviewsorg@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Question About FoodieViews");
                intent.putExtra(Intent.EXTRA_TEXT, " ");
                startActivity(Intent.createChooser(intent, ""));
            }
        });


        return root;
    }

}
