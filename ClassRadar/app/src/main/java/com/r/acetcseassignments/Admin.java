package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Admin extends AppCompatActivity implements View.OnClickListener{
    Button addClass,removeClass,editClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Trans));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);

        setContentView(R.layout.activity_admin);
        addClass=findViewById(R.id.addClass);
        removeClass=findViewById(R.id.removeClass);

        addClass.setOnClickListener(this);
        removeClass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==addClass){
            startActivity(new Intent(this,AddClass.class));
        }
        if(v==removeClass){
            startActivity(new Intent(this,Remove.class));
        }


    }
}
