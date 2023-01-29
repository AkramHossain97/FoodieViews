package com.example.foodieviews;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginTabFragment extends Fragment {

    EditText phoneNum , pass;
    Button LogInBtn;
    TextView ForgotPass;
    FirebaseAuth mFirebaseAuth;
    ProgressBar mLoginProgress;
    float o = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        ViewGroup root =(ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);
        phoneNum=root.findViewById(R.id.PhoneNumber);
        pass = root.findViewById(R.id.Password);
        LogInBtn = root.findViewById(R.id.LoginButton);
        ForgotPass = root.findViewById(R.id.ForgetPassword);
        mLoginProgress = root.findViewById(R.id.LoginProgress);
        mFirebaseAuth = FirebaseAuth.getInstance();



        phoneNum.setTranslationX(800);
        pass.setTranslationX(800);
        LogInBtn.setTranslationX(800);
        ForgotPass.setTranslationX(800);


        phoneNum.setAlpha(o);
        pass.setAlpha(o);
        LogInBtn.setAlpha(o);
        ForgotPass.setAlpha(o);

        phoneNum.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        LogInBtn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        ForgotPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();


            LogInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String email = phoneNum.getText().toString();
                    String pwd = pass.getText().toString();
                    if (email.isEmpty()) {
                        phoneNum.setError("Please Enter Email ID");
                        phoneNum.requestFocus();
                    } else if (pwd.isEmpty()) {
                        pass.setError("Please Enter a Valid Password");
                        pass.requestFocus();
                    } else if (email.isEmpty() && pwd.isEmpty()) {
                        Toast.makeText(getActivity(), "Fields Are Empty !", Toast.LENGTH_SHORT).show();
                    } else if (!(email.isEmpty() && pwd.isEmpty())) {
                        mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Login Error , Please Log In Again ", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    //email verification check
                                    if(mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                        Intent intToHome = new Intent(getActivity(), Home.class);
                                        getActivity().finish();
                                        startActivity(intToHome);
                                    }
                                    else {
                                        Toast.makeText(getActivity(), "Please Verify Your Email ! ", Toast.LENGTH_SHORT).show();

                                    }
                                }

                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "Error Ocurred ! ", Toast.LENGTH_SHORT).show();

                    }


                }


            });




        return root;

    }


}
