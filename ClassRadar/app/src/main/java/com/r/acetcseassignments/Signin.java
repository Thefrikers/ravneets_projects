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
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signin extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextemail;
    private EditText editTextPassword;
    private TextView textViewSignUp;
    private TextView textViewForgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CheckBox checkBox_remembermMe;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static Activity login;
    private DatabaseReference databaseReference;
    String accType;
    boolean teacher=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        getWindow().setStatusBarColor(getResources().getColor(R.color.Trans));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_signin);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = sharedPreferences.edit();
        login=this;
        checkBox_remembermMe = (CheckBox) findViewById(R.id.checkBox_rememberMe);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        //CHECKING IF USER HAS LOGINED AND STATE OF REMEMBER ME CHECK BOK
        if (firebaseAuth.getCurrentUser() != null && sharedPreferences.getBoolean("state", false)) {
            finish();
            if(sharedPreferences.getBoolean("student",false)){
                startActivity(new Intent(getApplicationContext(), MainActivityStudent.class));
            }
            else {
                startActivity(new Intent(getApplicationContext(), MainActivityTeacher.class));
            }
        }
        buttonSignIn = (Button) findViewById(R.id.ButtonSignin);
        textViewForgotPassword = (TextView) findViewById(R.id.textviewForgotPassword);
        editTextemail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignup);
        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);
    }
    //FUNCTION TO LOGIN
    private void userLogin() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.equals("admin")&&password.equals("admin")){
          startActivity(new Intent(this,Admin.class));
          finish();
        }
        progressDialog.setMessage("Signing in....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    checkEmailVerification();
                } else {
                    Toast.makeText(Signin.this, "Could not Sign In", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //OVERRIDING ONCLICKLISTENER
    @Override
    public void onClick(View v) {
        if (v == buttonSignIn) {
            if (checkBox_remembermMe.isChecked()) {
                editor = sharedPreferences.edit();
                editor.putBoolean("state", true);
                editor.commit();
            }

            userLogin();
        }
        if (v == textViewSignUp) {

            Pair pairs[]=new Pair[2];
            pairs[0]=new Pair<View,String>(findViewById(R.id.textView10),"signin");
            pairs[1]=new Pair<View,String>(textViewSignUp,"signup");
            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(this,pairs);
            startActivity(new Intent(getApplicationContext(), Signup.class));



        }
        if (v == textViewForgotPassword) {
            Pair pairs[]=new Pair[1];
            pairs[0]=new Pair<View,String>(editTextemail,"Email");

            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(this,pairs);
            startActivity(new Intent(getApplicationContext(), ForgotPassword.class),options.toBundle());

        }


    }
    //FUNCTION TO CHECK USER HAS VERIFIED EMAIL OR NOT
    private void checkEmailVerification() {
        final FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();
        if (emailFlag) {

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(firebaseUser.getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        editor = sharedPreferences.edit();
                        editor.putBoolean("student", true);
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(),MainActivityStudent.class));
                       finish();
                       progressDialog.dismiss();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Teachers").child(firebaseUser.getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        editor = sharedPreferences.edit();
                        editor.putBoolean("student", false);
                        editor.commit();
                       startActivity(new Intent(getApplicationContext(),MainActivityTeacher.class));
                       finish();
                       progressDialog.dismiss();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "Verify your email", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
    }


}

