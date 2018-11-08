package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.net.InetAddress;
//CLASS FOR SPLASH SCREEN
public class SplashScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    ImageView icon;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_splash_screen);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);

        // getSupportActionBar().hide();
        StartAnimations();
        sharedPreferences  = getApplicationContext().getSharedPreferences("onBoarding", 0);
        editor = sharedPreferences.edit();
    }
    //FUNCTION TO ANIMATE ICON
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        ConstraintLayout l=(ConstraintLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView background = (ImageView) findViewById(R.id.imageViewsplashScreenBackground);

        ImageView text = (ImageView) findViewById(R.id.imageViewsplashScreenText);

        background.clearAnimation();
        background.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        text.clearAnimation();
        text.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate_corner);



        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2500) {
                        sleep(100);
                        waited += 100;
                    }


                    if(isInternetAvailable()){
                        if(sharedPreferences.getBoolean("onboardingscreen",true)){
                            editor = sharedPreferences.edit();
                            editor.putBoolean("onboardingscreen", false);
                            editor.commit();
                            startActivity(new Intent(SplashScreen.this,OnBoardingActivity.class));
                            finish();
                        }else {
                            Intent intent = new Intent(SplashScreen.this, Signin.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            SplashScreen.this.finish();
                        }
                    }
                    else{
                        finish();
                        startActivity(new Intent(SplashScreen.this,NoInternetConnection.class));
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashScreen.this.finish();
                }

            }
        };
        splashTread.start();

    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}



