package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

//CLASS TO CHECK INTERNET CONNECTION
public class NoInternetConnection extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewTryAgain;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getSupportActionBar().hide();
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        setContentView(R.layout.activity_no_internet_connection);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);
        progressDialog = new ProgressDialog(this);
        textViewTryAgain = (TextView) findViewById(R.id.textViewTryAgain);
        textViewTryAgain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == textViewTryAgain) {
            progressDialog.setMessage("Checking....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            if (isInternetAvailable()) {
                progressDialog.dismiss();
                finish();
                startActivity(new Intent(NoInternetConnection.this, Signin.class));
            } else {
                progressDialog.dismiss();
                Toast.makeText(NoInternetConnection.this, "No Internet connection", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //FUNCTION TO CHECK INTERNET
    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;

        } else {
            return false;
        }
    }
}
