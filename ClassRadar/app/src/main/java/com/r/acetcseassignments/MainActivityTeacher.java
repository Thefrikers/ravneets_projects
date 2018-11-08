package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivityTeacher extends AppCompatActivity implements View.OnClickListener{
    ImageView classes,labs,dashboard,more;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Transparent));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);
        setContentView(R.layout.activity_main_teacher);
        classes=findViewById(R.id.classes);
        labs=findViewById(R.id.labs);
        progressDialog=new ProgressDialog(this);
        more=findViewById(R.id.more);
        dashboard=findViewById(R.id.dashboard);
        classes.setOnClickListener(this);
        labs.setOnClickListener(this);
        dashboard.setOnClickListener(this);
        more.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==classes){
            Pair pairs[]=new Pair[1];
            pairs[0]=new Pair<View,String>(classes,"img");
            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(this,pairs);
            startActivity(new Intent(getApplicationContext(), EnterClass.class),options.toBundle());
        }
        if(v==labs){
            Pair pairs[]=new Pair[1];
            pairs[0]=new Pair<View,String>(labs,"img1");
            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(this,pairs);
            startActivity(new Intent(getApplicationContext(), EnterLab.class),options.toBundle());
        }
        if(v==dashboard) {
            Pair pairs[] = new Pair[1];
            pairs[0] = new Pair<View, String>(dashboard, "img2");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
            startActivity(new Intent(getApplicationContext(), Dashboard.class),options.toBundle());
        }
        if(v==more){
            PopupMenu popup = new PopupMenu(this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.setting, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.profile:
                            startActivity(new Intent(getApplicationContext(),MyProfileTeacher.class));
                            return true;
                        case R.id.logout:
                            SharedPreferences sharedPreferences;
                            final SharedPreferences.Editor editor;
                            sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
                            editor = sharedPreferences.edit();
                            progressDialog.setMessage("Signing out...");
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                            AuthUI.getInstance()
                                    .signOut(getApplicationContext())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // user is now signed out
                                            editor.clear();
                                            editor.commit();
                                            startActivity(new Intent(MainActivityTeacher.this, Signin.class));
                                            progressDialog.dismiss();

                                            finish();
                                        }
                                    });


                            return true;
                        default:
                            return false;

                    }
                }


            });
            popup.show();

        }
    }
}
