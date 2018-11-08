package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;
//CLASS FOR ONBOARDING SCREEN
public class OnBoardingActivity extends TutorialActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishText("TAKE ME IN!");
        setCancelText("SKIP");
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);
        addFragment(new Step.Builder().setTitle("ON YOUR MAKRS!")
                .setContent("Now find classes and lab just by one click. This app let's you to find free classes and lab just by snap of your fingers.")
                .setBackgroundColor(Color.parseColor("#263238")) // int background color
                .setDrawable(R.drawable.onboarding) // int top drawable
                .build());
        addFragment(new Step.Builder().setTitle("FIND TEACHERS!")
                .setContent("Finding teachers made easy. You can also find teachers using ClassRadar.")
                .setBackgroundColor(Color.parseColor("#0277BD")) // int background color
                .setDrawable(R.drawable.teacheronboarding) // int top drawable
                .build());




    }
    @Override
    public void finishTutorial() {
        startActivity(new Intent(OnBoardingActivity.this,Signin.class));
        finish();
        // Your implementation
    }
}
