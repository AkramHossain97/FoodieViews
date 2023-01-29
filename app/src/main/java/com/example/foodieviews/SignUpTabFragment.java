package com.example.foodieviews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class SignUpTabFragment extends Fragment {

    EditText Remail,Rname,Rpassword;
    Button SignUpBtn;
    ProgressBar SignUpProgress;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){



    ViewGroup root =(ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);

        Remail = root.findViewById(R.id.SignEmail);
        Rname = root.findViewById(R.id.Name);
        Rpassword = root.findViewById(R.id.SignUpPassword);
        SignUpBtn = root.findViewById(R.id.SignUpButton);
        SignUpProgress = root.findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Email = Remail.getText().toString();
                String Name = Rname.getText().toString();
                String Password = Rpassword.getText().toString();



                if (Email.isEmpty()){
                   Remail.setError("Please Enter Email ID");
                    Remail.requestFocus();
                }
                else if (Password.isEmpty()){
                    Rpassword.setError("Please Enter a Valid Password");
                    Rpassword.requestFocus();
                }
                else if (Name.isEmpty()){
                    Rname.setError("Please Enter a Valid Password");
                    Rname.requestFocus();
                }
                else if (Email.isEmpty() && Password.isEmpty() && Name.isEmpty()){
                    Toast.makeText(getActivity(),"Fields Are Empty !",Toast.LENGTH_SHORT).show();
                }
                else if(!(Email.isEmpty() && Password.isEmpty() && Name.isEmpty())){
                    mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                SignUpProgress.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),"SignUp Unsuccessful , Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> taske) {
                                        if(taske.isSuccessful()){
                                            SignUpProgress.setVisibility(View.VISIBLE);
                                            rootNode=FirebaseDatabase.getInstance();
                                            reference = rootNode.getReference("users");

                                            String email = Remail.getText().toString();
                                            String username = Rname.getText().toString();
                                            String uid = mAuth.getCurrentUser().getUid();
                                            String Profile_Picture = Rname.getText().toString();
                                            RegisterUserHelperClass helperClass = new RegisterUserHelperClass(email,username,Profile_Picture,uid);
                                            reference.child(uid).setValue(helperClass);
                                            getActivity().finish();
                                            startActivity(new Intent(getActivity(),Login.class));


                                        }

                                    }

                                });
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(getActivity(),"Error Ocurred ! ",Toast.LENGTH_SHORT).show();

                }


            }


            });



        return root;

    }

}
