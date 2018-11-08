package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private Button buttonResetPassword;
    private EditText editTextemail;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
         getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Trans));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_forgot_password);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        buttonResetPassword = (Button) findViewById(R.id.ButtonResetPassword);
        editTextemail = (EditText) findViewById(R.id.editTextEmail);
        textViewSignin = (TextView) findViewById(R.id.textViewSignup);
        buttonResetPassword.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }
    //OVERRIDING ONCLICKLISTENER
    @Override
    public void onClick(View v) {
        if (v == textViewSignin) {
            onBackPressed();
        }
        if (v == buttonResetPassword) {
            resetPassword();
        }

    }
    //FUNCTION TO RESET PASSWORD
    private void resetPassword() {
        String email = editTextemail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        } else {

            progressDialog.setMessage("Sending Email....");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {

                        Toast.makeText(ForgotPassword.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(ForgotPassword.this, "Unable to send Email!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
