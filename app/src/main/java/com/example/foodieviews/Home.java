package com.example.foodieviews;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.FrameLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class Home extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {


    private static final int POS_CLOSE=0;
    private static final int POS_DASHBOARD=1;
    private static final int POS_MY_PROFILE=2;
    private static final int POS_NEARBY_RES=3;
    private static final int POS_SETTINGS=4;
    private static final int POS_ABOUT_US=5;
    private static final int POS_LOGOUT=7;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white)); //status bar or the time bar at the top
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav =new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();


        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();


        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(

         createItemFor(POS_CLOSE), createItemFor(POS_DASHBOARD).setChecked(true), createItemFor(POS_MY_PROFILE), createItemFor(POS_NEARBY_RES),
                createItemFor(POS_SETTINGS), createItemFor(POS_ABOUT_US), new SpaceItem(260) ,createItemFor(POS_LOGOUT)
        ));

        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);


    }

    private DrawerItem createItemFor(int position){

        return new SimpleItem(screenIcons[position],screenTitles[position])
                .withIconTint(color(R.color.yellow))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.yellow))
                .withSelectedTextTint(color(R.color.yellow))
                ;
    }


    @ColorInt
    private int color(@ColorRes int res){

        return ContextCompat.getColor(this,res );

    }



    private String[] loadScreenTitles() {

        return getResources().getStringArray(R.array.id_activityScreenTitles);

    }

    private Drawable[] loadScreenIcons() {

        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for(int i=0 ; i<ta.length(); i++ ){

            int id = ta.getResourceId(i,0);
            if(id!=0){

                icons[i] = ContextCompat.getDrawable(this,id);
            }
        }
        ta.recycle();
        return icons;

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void OnItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

         switch (position){

             case 1:
                 DashBoardFragment dashBoardFragment = new DashBoardFragment();
                 dashBoardFragment.setEnterTransition(new Slide(Gravity.END));
                 dashBoardFragment.setExitTransition(new Slide(Gravity.START));
                 transaction.replace(R.id.main_container,dashBoardFragment);
                 break;

             case 2:
                 Profile_Fragment profile_fragment = new Profile_Fragment();
                 profile_fragment.setEnterTransition(new Slide(Gravity.END));
                 profile_fragment.setExitTransition(new Slide(Gravity.START));
                 transaction.replace(R.id.main_container,profile_fragment);
                 break;

             case 3:
                 NearByRes_Fragment nearByRes_fragment = new NearByRes_Fragment();
                 nearByRes_fragment.setEnterTransition(new Slide(Gravity.END));
                 nearByRes_fragment.setExitTransition(new Slide(Gravity.START));
                 transaction.replace(R.id.main_container,nearByRes_fragment);
                 break;

             case 4:
                 Settings_Fragment settings_fragment = new Settings_Fragment();
                 settings_fragment.setEnterTransition(new Slide(Gravity.END));
                 settings_fragment.setExitTransition(new Slide(Gravity.START));
                 transaction.replace(R.id.main_container,settings_fragment);
                 break;

             case 5:

                 AboutUS_Fragment aboutUS_fragment = new AboutUS_Fragment();
                 aboutUS_fragment.setEnterTransition(new Slide(Gravity.END));
                 aboutUS_fragment.setExitTransition(new Slide(Gravity.START));
                 transaction.replace(R.id.main_container,aboutUS_fragment);
                 break;

             case 7:

                 FirebaseAuth.getInstance().signOut();
                 mGoogleSignInClient.signOut();
                 Home.this.finish();
                 startActivity(new Intent(Home.this,Login.class));
                 break;

         }



        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();

    }



}