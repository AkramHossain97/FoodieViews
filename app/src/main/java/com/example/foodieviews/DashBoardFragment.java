package com.example.foodieviews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class DashBoardFragment extends Fragment {

    ImageButton UserPosts , UserTopRated , PostedOnFacebook , UserReviewedPosts ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.dashboard_fragment, container, false);

        UserPosts = root.findViewById(R.id.UserFDposts);
        UserTopRated = root.findViewById(R.id.toprated);
        PostedOnFacebook = root.findViewById(R.id.fbposted);
        UserReviewedPosts = root.findViewById(R.id.userreviewed);



        UserPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intToUserPosts = new Intent(getActivity(),User_Posts.class);

                startActivity(intToUserPosts);
                Animatoo.animateZoom(getContext());


            }
        });

        UserReviewedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intToReviewedPosts = new Intent(getActivity(),ReviewedPosts.class);

                startActivity(intToReviewedPosts);
                Animatoo.animateZoom(getContext());


            }
        });


        UserTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intToTopRated = new Intent(getActivity(),TopPost.class);

                startActivity(intToTopRated);
                Animatoo.animateZoom(getContext());


            }
        });



        return root;


    }

}
