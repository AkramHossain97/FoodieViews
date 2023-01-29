package com.example.foodieviews;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Fragment extends Fragment {


    CircleImageView mProfileImg;
    TextView mProUserName , mProLuserName , mProLEm;
    private DatabaseReference mDatabaseUser;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment, container, false);

        mProfileImg = root.findViewById(R.id.profile_image);
        mProUserName = root.findViewById(R.id.profileUserName);
        mProLuserName = root.findViewById(R.id.LproUN);
        mProLEm = root.findViewById(R.id.LproEM);


        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseUser=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());


        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfileInfo userProfileInfo = snapshot.getValue(UserProfileInfo.class);
                mProUserName.setText(userProfileInfo.getUsername());
                mProLuserName.setText(userProfileInfo.getUsername());
                mProLEm.setText(userProfileInfo.getEmail());
                Picasso.get().load(userProfileInfo.profile_picture).into(mProfileImg);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return root;
    }




}
