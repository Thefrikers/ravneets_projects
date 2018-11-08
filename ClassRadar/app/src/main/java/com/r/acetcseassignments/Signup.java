package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity implements View.OnClickListener ,AdapterView.OnItemSelectedListener {
    private Button buttonRegistered;
    private EditText editTextemail;
    private ShowHidePasswordEditText editTextPassword;
    private TextView textViewSignin;
    Spinner acctype;
    String type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Trans));
        setContentView(R.layout.activity_signup);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);
        buttonRegistered = (Button) findViewById(R.id.ButtonRegister);
        editTextemail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (ShowHidePasswordEditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        buttonRegistered.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        acctype=findViewById(R.id.acctype);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.accountType,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acctype.setAdapter(adapter);
        acctype.setOnItemSelectedListener(this);

    }



    @Override
    public void onClick(View v) {
        if (v == buttonRegistered) {
            String email = editTextemail.getText().toString().trim();
            final String password = editTextPassword.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!(password.length() >5)){
                Toast.makeText(getApplicationContext(),"Password must be at least 6 characters long",Toast.LENGTH_SHORT).show();
                return;

            }
            if (!isEmailValid(email)){
                Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                return;
            }
           Pair pairs[]=new Pair[1];
            pairs[0]=new Pair<View,String>(editTextemail,"Email");
            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(this,pairs);
            if(type.equals("Student")) {
                Intent intent = new Intent(getApplicationContext(), NameActivity.class);
                intent.putExtra("email", editTextemail.getText().toString());
                intent.putExtra("password",editTextPassword.getText().toString());
                startActivity(intent,options.toBundle());
            }
            else {
                Intent intent = new Intent(getApplicationContext(), NameActivityTeacher.class);
                intent.putExtra("email", editTextemail.getText().toString());
                intent.putExtra("password",editTextPassword.getText().toString());
                startActivity(intent,options.toBundle());
            }

            // finish();
        }
        if (v == textViewSignin) {
            onBackPressed();


        }

    }


    public boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
