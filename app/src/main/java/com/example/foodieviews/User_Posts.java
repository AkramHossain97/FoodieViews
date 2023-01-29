package com.example.foodieviews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class User_Posts extends AppCompatActivity {
    private RecyclerView mPostList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLike;
    private boolean mProcessLike=false;
    private FirebaseAuth mAuth;
    private FloatingActionButton addBtn;
    ImageButton SchBtn;
    EditText SchText;
    String SearchText ;
    View view ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__posts);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white)); //status bar or the time bar at the top
        }

        addBtn = findViewById(R.id.addButton);
        SchText = findViewById(R.id.SearchPost);
        SchBtn = findViewById(R.id.searchButton);



        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseLike=FirebaseDatabase.getInstance().getReference().child("Likes");

        mDatabaseLike.keepSynced(true);

        mPostList = (RecyclerView) findViewById(R.id.Post_List);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intToUserAddPosts = new Intent(User_Posts.this,AddPost.class);
                finish();
                startActivity(intToUserAddPosts);
                Animatoo.animateSlideUp(User_Posts.this);
            }
        });



        SchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 SearchText = SchText.getText().toString();
               // searchInputToLower = inputText.getText().toString().toLowerCase();

               // searchInputTOUpper = inputText.getText().toString().toUpperCase();
                firebaseSearch(SearchText);
            }
        });




    }

    private void firebaseSearch(String SearchText) {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Post")
                .limitToLast(50);
        FirebaseRecyclerOptions<Bpost> options =
                new FirebaseRecyclerOptions.Builder<Bpost>()
                        .setQuery(query, Bpost.class)
                        .build();
//Recycler for viewing the information of posts from database
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Bpost, PostViewHolder>(options) {
            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                 view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_row, parent, false);



                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PostViewHolder holder, int position, Bpost model) {


                if(model.getPostname().toLowerCase().contains(SearchText.toLowerCase()) ||
                        model.getPosttext().toLowerCase().contains(SearchText.toLowerCase())){

                    final String post_key= getRef(position).getKey();
                holder.setPostname(model.getPostname());
                holder.setPosttext(model.getPosttext());
                holder.setPost_image(getApplicationContext(), model.getPost_image());
                holder.setUsername(model.getUsername());
                holder.setTime(model.getTime());
                holder.setProfile_picture(model.getProfile_picture());




                holder.setLikeBtn(post_key);


                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent SinglePostIntent = new Intent(User_Posts.this,SingleUserPosts.class);
                            SinglePostIntent.putExtra("Post_id",post_key);
                            startActivity(SinglePostIntent);


                        }
                    });


                holder.mBtnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mProcessLike=true;

                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(mProcessLike) {
                                    if (snapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessLike = false;
                                    } else {
                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RValues");
                                        mProcessLike = false;


                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });

            }
                else
                {

                    holder.itemView.setVisibility(GONE);
                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.itemView.setLayoutParams(params);

                }



            }

        };

        mPostList.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Post")
                .limitToLast(50);
        FirebaseRecyclerOptions<Bpost> options =
                new FirebaseRecyclerOptions.Builder<Bpost>()
                        .setQuery(query, Bpost.class)
                        .build();
//Recycler for viewing the information of posts from database
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Bpost, PostViewHolder>(options) {
            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                 view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_row, parent, false);

                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PostViewHolder holder, int position, Bpost model) {

                final String post_key= getRef(position).getKey();
                holder.setPostname(model.getPostname());
                holder.setPosttext(model.getPosttext());
                holder.setPost_image(getApplicationContext(), model.getPost_image());
                holder.setUsername(model.getUsername());
                holder.setTime(model.getTime());
                holder.setProfile_picture(model.getProfile_picture());

                holder.setLikeBtn(post_key);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent SinglePostIntent = new Intent(User_Posts.this,SingleUserPosts.class);
                        SinglePostIntent.putExtra("Post_id",post_key);
                        startActivity(SinglePostIntent);


                    }
                });


                holder.mBtnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mProcessLike=true;

                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(mProcessLike) {
                                    if (snapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessLike = false;
                                    } else {
                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RValues");
                                        mProcessLike = false;


                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });

            }
        };
        mPostList.setAdapter(adapter);
        adapter.startListening();

    }




//view holder of storing the post information

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton mBtnLike;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;
        TextView LikeView;
        public CircleImageView post_pro_pic ;


        public PostViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
            mBtnLike = (ImageButton) mView.findViewById(R.id.LikeButton);
            LikeView = (TextView)mView.findViewById(R.id.LikesText) ;
            mAuth=FirebaseAuth.getInstance();
            mDatabaseLike=FirebaseDatabase.getInstance().getReference().child("Likes");

            mDatabaseLike.keepSynced(true);


        }

        public void setLikeBtn(String post_key){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){

                        int LikeCounter = (int)snapshot.child(post_key).getChildrenCount();
                        LikeView.setText(LikeCounter+" Likes");
                        mBtnLike.setImageResource(R.drawable.thumbsup_red);

                    }else {

                        int LikeCounter = (int)snapshot.child(post_key).getChildrenCount();
                        LikeView.setText(LikeCounter+" Likes");
                        mBtnLike.setImageResource(R.drawable.thumbsup_gray);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        public void setPostname(String postname){

            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(postname);

        }

        public void setPosttext(String posttext){

            TextView post_dec = (TextView) mView.findViewById(R.id.post_desc);
            post_dec.setText(posttext);

        }

        public void setPost_image(Context ctx , String post_image) {
            ImageView post_img = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(post_image).into(post_img);

        }

        public void setUsername(String username){

            TextView post_Username = (TextView) mView.findViewById(R.id.post_userName);
            post_Username.setText(username);

        }

        public void setTime(String time){

            TextView post_time = (TextView) mView.findViewById(R.id.Post_Time);
            post_time.setText(time);

        }

        public void setProfile_picture(String profile_picture){

            post_pro_pic = (CircleImageView) mView.findViewById(R.id.PostProfilePicture);
            Picasso.get().load(profile_picture).resize(800,0).centerCrop().into(post_pro_pic);

        }


    }

}