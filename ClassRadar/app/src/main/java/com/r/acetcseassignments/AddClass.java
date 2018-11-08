package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddClass extends AppCompatActivity implements View.OnClickListener ,AdapterView.OnItemSelectedListener {
    Spinner roomType,roomType1;
    Button back,add;
    EditText editTextClassName;
    String type,type1;
    DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Transparent));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);

        setContentView(R.layout.activity_add_class);
        roomType=findViewById(R.id.roomType);
        roomType1=findViewById(R.id.roomTpye1);
        editTextClassName=findViewById(R.id.editTextClassName);
        progressDialog = new ProgressDialog(this);
        back=findViewById(R.id.back);
        add=findViewById(R.id.add);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.roomType,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomType.setAdapter(adapter);
        roomType.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.roomType1,R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomType1.setAdapter(adapter1);
        roomType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type1=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        back.setOnClickListener(this);
        add.setOnClickListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type=parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v==add){
            if(!editTextClassName.getText().toString().isEmpty()) {
                progressDialog.setMessage("Adding....");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                databaseReference = FirebaseDatabase.getInstance().getReference().child(type).child(type1 + editTextClassName.getText().toString());
                databaseReference.setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                            editTextClassName.setText("");
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Empty field!", Toast.LENGTH_SHORT).show();

            }
        }
        if(v==back){
            onBackPressed();
        }

    }
}
