package com.example.foodieviews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleUserPosts extends AppCompatActivity {

    private CircleImageView Simage , ScommenterImage;
    private TextView SUsername , Stime , STitle , SDesc ;
    private ImageView SPostImage ;
    private EditText NewComment ;
    private ImageButton SendComment ;
    private RecyclerView mCommentList;
    private DatabaseReference mDatabaseS ;
    private String mPost_key = null;
    private DatabaseReference JKDatabaseUser , NCDatabase;
    FirebaseAuth JKFirebaseAuth;
    FirebaseUser JKUser = FirebaseAuth.getInstance().getCurrentUser();
    String CommentUserName ;
    SmileyRating smileyRating ;
    private DatabaseReference mDatabaseStar;
   public ImageButton starBtn;
   public TextView StarTextCounter;
    private boolean mProcessStar=false;
    private FirebaseAuth mAuthS;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user_posts);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white)); //status bar or the time bar at the top
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mAuthS=FirebaseAuth.getInstance();

        mDatabaseS = FirebaseDatabase.getInstance().getReference().child("Post");
        mPost_key = getIntent().getExtras().getString("Post_id");
        Simage = (CircleImageView)findViewById(R.id.SinglePostProfilePicture);
        SUsername = (TextView) findViewById(R.id.SinglePost_userName);
        Stime = (TextView) findViewById(R.id.SinglePost_Time);
        SPostImage = (ImageView) findViewById(R.id.SinglePost_image);
        STitle = (TextView) findViewById(R.id.SinglePost_title);
        SDesc = (TextView) findViewById(R.id.SinglePost_desc);
        ScommenterImage =(CircleImageView) findViewById(R.id.CommentProfilePicture);
        NewComment = (EditText) findViewById(R.id.CommentPost);
        SendComment = (ImageButton) findViewById(R.id.SendButton);
        mCommentList = (RecyclerView) findViewById(R.id.CommentList);
        starBtn = (ImageButton) findViewById(R.id.StarButton);
        StarTextCounter = (TextView) findViewById(R.id.StarText);
       /* smileyRating = (SmileyRating) findViewById(R.id.smile_rating);*/



        FirebaseUser CommentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String CUserID = CommentUser.getUid();
        JKFirebaseAuth=FirebaseAuth.getInstance();
        NCDatabase=FirebaseDatabase.getInstance().getReference().child("Post").child(mPost_key).child("Comments");
        JKDatabaseUser=FirebaseDatabase.getInstance().getReference().child("users").child(JKUser.getUid());

        mDatabaseStar=FirebaseDatabase.getInstance().getReference().child("Post").child(mPost_key).child("Stars");

        mDatabaseStar.keepSynced(true);


        starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessStar=true;
                mDatabaseStar.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(mProcessStar) {
                            if (snapshot.hasChild(mAuthS.getCurrentUser().getUid())) {
                                mDatabaseStar.child(mAuthS.getCurrentUser().getUid()).removeValue();
                                mProcessStar = false;
                            } else {
                                mDatabaseStar.child(mAuthS.getCurrentUser().getUid()).setValue("True");
                                mProcessStar = false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        mDatabaseStar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(mAuthS.getCurrentUser().getUid())){
                    int StarCounter = (int)snapshot.getChildrenCount();
                    StarTextCounter.setText(StarCounter+" People Recommended It");
                    starBtn.setImageResource(R.drawable.star_yellow);
                }else {
                    int StarCounter = (int)snapshot.getChildrenCount();
                    StarTextCounter.setText(StarCounter+" People Recommended It");
                    starBtn.setImageResource(R.drawable.star_gray);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       /* smileyRating.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {

                SmileyRating.Type smiley = smileyRating.getSelectedSmiley();

                if (SmileyRating.Type.GREAT == type) {
                    Toast.makeText(SingleUserPosts.this, "Rating Added", Toast.LENGTH_LONG);                }
                // You can get the user rating too
                // rating will between 1 to 5, but -1 is none selected
                int rating = type.getRating();
                // You can compare it with rating Type
                Toast.makeText(SingleUserPosts.this, rating, Toast.LENGTH_LONG);

            }
        });*/



        mCommentList.setLayoutManager(new LinearLayoutManager(this));

        JKDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfileInfo userProfileInfo = snapshot.getValue(UserProfileInfo.class);

                Picasso.get().load(userProfileInfo.profile_picture).into(ScommenterImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mDatabaseS.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String package_title = (String) snapshot.child("postname").getValue();
                String package_description = (String) snapshot.child("posttext").getValue();
                String package_image = (String) snapshot.child("post_image").getValue();
                String CMusername = (String) snapshot.child("username").getValue();
                String CmTime = (String) snapshot.child("Time").getValue();
                String Cmuserpic = (String) snapshot.child("profile_picture").getValue();

                STitle.setText(package_title);
                SDesc.setText(package_description);
                Picasso.get().load(package_image).into(SPostImage);
                SUsername.setText(CMusername);
                Stime.setText(CmTime);
                Picasso.get().load(Cmuserpic).into(Simage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        SendComment.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view) {

                JKDatabaseUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            String CommentUserName = snapshot.child("username").getValue().toString();
                            String CommentUserPict = snapshot.child("profile_picture").getValue().toString();
                            processcomment(CommentUserName , CommentUserPict);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            private void processcomment(String CommentUserName, String CommentUserPict) {
                String SRCommentText = NewComment.getText().toString();
                if (!(SRCommentText.isEmpty())) {
                    String CommentText = NewComment.getText().toString();
                    String randompostkey = CUserID + "" + new Random().nextInt(1000);
                    Calendar dateValue = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yy");
                    String cdate = dateFormat.format(dateValue.getTime());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm");
                    String ctime = timeFormat.format(dateValue.getTime());
                    HashMap cmnt = new HashMap();
                    cmnt.put("uid", CUserID);
                    cmnt.put("username", CommentUserName);
                    cmnt.put("profile_picture", CommentUserPict);
                    cmnt.put("CommentMsg", CommentText);
                    cmnt.put("Date", cdate);
                    cmnt.put("Time", ctime);
                    NCDatabase.child(randompostkey).updateChildren(cmnt)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(SingleUserPosts.this, "Comment Added", Toast.LENGTH_LONG);
                                        NewComment.setText(null);
                                    } else {

                                        Toast.makeText(getApplicationContext(), task.toString(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                }

                else {


                    Toast.makeText(SingleUserPosts.this, "Please Enter Comment", Toast.LENGTH_SHORT).show();
                }


            }
        });
        
    }


    @Override
    protected void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Post")
                .child(mPost_key)
                .child("Comments")
                .orderByChild("Time")
                .limitToLast(50);
        FirebaseRecyclerOptions<UserCommentInfo> options =
                new FirebaseRecyclerOptions.Builder<UserCommentInfo>()
                        .setQuery(query, UserCommentInfo.class)
                        .build();
//Recycler for viewing the information of Comments from database
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<UserCommentInfo, SingleUserPosts.PostViewHolder>(options) {
            @Override
            public SingleUserPosts.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
               View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_row, parent, false);

                return new SingleUserPosts.PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(SingleUserPosts.PostViewHolder holder, int position, UserCommentInfo model) {

                final String post_key= getRef(position).getKey();
                holder.setCommentMsg(model.getCommentMsg());
                holder.setUsername(model.getUsername());

                holder.setProfile_picture(model.getProfile_picture());


            }
        };
        mCommentList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class PostViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public CircleImageView post_pro_pic ;


        public PostViewHolder(View itemView) {
            super(itemView);

            mView=itemView;



        }


        public void setCommentMsg(String commentMsg){

            TextView post_dec = (TextView) mView.findViewById(R.id.TheComment);
            post_dec.setText(commentMsg);

        }


        public void setUsername(String username){

            TextView post_Username = (TextView) mView.findViewById(R.id.TheUsername);
            post_Username.setText(username);

        }


        public void setProfile_picture(String profile_picture){

            post_pro_pic = (CircleImageView) mView.findViewById(R.id.TheCommenterProfilePicture);
            Picasso.get().load(profile_picture).into(post_pro_pic);

        }


    }


}