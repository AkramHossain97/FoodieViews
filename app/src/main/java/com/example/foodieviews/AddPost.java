package com.example.foodieviews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPost extends AppCompatActivity {

    private ImageButton mSelectImage;
    private static final int GALLERY_REQUEST=1;
    private EditText mPostDesc;
    private EditText mPostTitle;
    private Button submitButton;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    public String formattedDate ;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabaseUserNew;
    FirebaseAuth mFireAuth;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentuser;
    public String User_Profile_Picture ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white)); //status bar or the time bar at the top
        }
        mDatabaseUserNew=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("profile_picture");
         User_Profile_Picture = mDatabaseUserNew.toString();

        Date c = Calendar.getInstance().getTime();
       // System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
         formattedDate = df.format(c);

        mFireAuth=FirebaseAuth.getInstance();
        mCurrentuser=mFireAuth.getCurrentUser();


        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentuser.getUid());

        mSelectImage = (ImageButton) findViewById(R.id.imageButton);
        mPostDesc = (EditText) findViewById(R.id.editTextTextPersonName) ;
        mPostTitle = (EditText) findViewById(R.id.editTextTextMultiLine) ;
        submitButton = (Button) findViewById(R.id.button2);
        mProgress = new ProgressDialog(this);
//selecting image from gallery
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
//submiting the post
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartPosting();

            }
        });

    }
    //posting user inputted information into the database
    private void StartPosting() {

        mProgress.setMessage("Posting....");


        String title_val = mPostTitle.getText().toString().trim();
        String desc_val = mPostDesc.getText().toString().trim();
//checking if all the fields are occupied
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null){

            mProgress.show();
//uploading image to Firebase Storage
            StorageReference filepath = mStorage.child("blog_images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            //posting post info to database
                            DatabaseReference newPost=mDatabase.push();
                            newPost.child("postname").setValue(desc_val);
                            newPost.child("posttext").setValue(title_val);
                            newPost.child("Time").setValue(formattedDate);
                            newPost.child("profile_picture").setValue(User_Profile_Picture);
                            newPost.child("post_image").setValue(downloadUrl.toString());
                            newPost.child("uid").setValue(mFireAuth.getUid());

                            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    newPost.child("postname").setValue(desc_val);
                                    newPost.child("posttext").setValue(title_val);
                                    newPost.child("post_image").setValue(downloadUrl.toString());
                                    newPost.child("Time").setValue(formattedDate);
                                    newPost.child("uid").setValue(mFireAuth.getUid());
                                    newPost.child("profile_picture").setValue(snapshot.child("profile_picture").getValue());
                                    newPost.child("username").setValue(snapshot.child("username").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                Intent intToUP = new Intent(AddPost.this,User_Posts.class);
                                                finish();
                                                startActivity(intToUP);
                                            }
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                            mProgress.dismiss();
                        }
                    });
                }
            });
        }
    }
    //image data from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){

            mImageUri = data.getData();

            mSelectImage.setImageURI(mImageUri);

        }

    }


}