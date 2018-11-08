package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

public class ChangePassword extends AppCompatActivity {
    ShowHidePasswordEditText currentPassword;
    ShowHidePasswordEditText newPassword;
    EditText newPasswordVerify;
    Button changePassword;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Trans));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_change_password);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);
        progressDialog=new ProgressDialog(this);
        currentPassword=findViewById(R.id.currentPassword);
        newPassword=findViewById(R.id.newPassword);
        newPasswordVerify=findViewById(R.id.newPasswordVerify);
        changePassword=findViewById(R.id.ButtonChangePassword);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPassword.getText().toString().equals(newPasswordVerify.getText().toString())){
                    if(newPassword.getText().toString().length()>5&&newPasswordVerify.getText().toString().length()>5) {
                        newPasswordVerify.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done, 0);
                        updatePassword();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Password must be at least 6 characters long",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Password doesn't match!",Toast.LENGTH_SHORT).show();
                }



            }
        });
        newPasswordVerify.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    if(newPassword.getText().toString().equals(newPasswordVerify.getText().toString())){
                        if(newPassword.getText().toString().length()>5&&newPasswordVerify.getText().toString().length()>5) {
                            newPasswordVerify.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done, 0);

                        }
                    }

                }
                return false;
            }
        });
    }
    private void updatePassword() {
        final String email = firebaseUser.getEmail();
        progressDialog.setMessage("Updating password...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        if (email!=null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword.getText().toString());
            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        firebaseUser.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Password updated!", Toast.LENGTH_SHORT).show();
                                    currentPassword.setText("");
                                    newPassword.setText("");
                                    newPasswordVerify.setText("");
                                    finish();
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Password update unsuccessful", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid current password", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
}



