package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class NameActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    private Button buttonSave;
    private EditText editTextename;
    private EditText editTextRollNumber;
    private TextView textViewemail;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,databaseReference2;
    private StorageReference storageReference;
    public String rollNumber;
    private EditText editTextEmail;
    private Uri imageUri;
    private Uri resultUri;
    private CircleImageView userImage;
    String imgUrl;
    private ProgressDialog progressDialog;
    private TextView chooseProfileImage;
    String email,password;
    TextView textViewSignup;
    String batch,type;
    Spinner stype;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Trans));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_name);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        password=bundle.getString("password");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        buttonSave = (Button) findViewById(R.id.ButtonSave);
        editTextename = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        userImage = (CircleImageView) findViewById(R.id.userImage);
        editTextRollNumber=findViewById(R.id.editTextRollNumber);
        stype=findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.type,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stype.setAdapter(adapter);
        stype.setOnItemSelectedListener(this);
        textViewSignup=findViewById(R.id.textViewSignup);
        chooseProfileImage = (TextView) findViewById(R.id.chooseProfilePhoto);
        editTextEmail.setEnabled(false);
        editTextEmail.setKeyListener(null);
        editTextEmail.setText(email);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Students");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    data=dataSnapshot.toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference().child("Student_Images");
        chooseProfileImage.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

    }
    //FUNCTION TO UPLOAD USER INFO TO FIREBASE
    private void saveUserInformation() {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final String name = editTextename.getText().toString().trim();
        final StorageReference sr = storageReference.child(user.getUid() + ".jpg");
        sr.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        rollNumber=editTextRollNumber.getText().toString();
                        imgUrl = uri.toString();
                        if(type.equals("Leet")){
                            batch="20"+rollNumber.substring(0,2)+"-20"+String.valueOf(Integer.valueOf(rollNumber.substring(0,2))+3);
                        }
                        else {
                            batch="20"+rollNumber.substring(0,2)+"-20"+String.valueOf(Integer.valueOf(rollNumber.substring(0,2))+4);
                        }
                        StudentInfo studentInfo = new StudentInfo(rollNumber,email,name,batch,type,imgUrl,"Student");
                        databaseReference.child(user.getUid()).setValue(studentInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent=new Intent(NameActivity.this, Signin.class);
                                startActivity(intent);
                                finishAffinity();
                                firebaseAuth.signOut();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Success....Email Verification sent!", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(NameActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    //OVERRIDING ONCLICKLISTENER
    @Override
    public void onClick(View v) {
        if (v == buttonSave) {
            if (resultUri == null) {
                Toast.makeText(getApplicationContext(), "Please choose an image", Toast.LENGTH_SHORT).show();
            } else {
                if (editTextRollNumber.getText().toString().equals("") ||
                        editTextename.getText().toString().equals("") ||
                        editTextEmail.getText().toString().equals("") ||
                        onlyLettersSpaces(editTextename.getText().toString()) ||
                        editTextRollNumber.getText().toString().length() != 7) {
                    Toast.makeText(getApplicationContext(), "Empty fields/Invalid values", Toast.LENGTH_SHORT).show();
                } else {

                        if(data==null){
                        registerUser();
                        }
                        else{
                            if(data.contains(editTextRollNumber.getText().toString())){
                                Toast.makeText(getApplicationContext(),"Roll no. already registered ",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                registerUser();
                            }
                        }

                }
            }

    }
        if (v == chooseProfileImage) {
            selectimage();
        }
        if(v==textViewSignup){
            onBackPressed();
        }


    }

    //FUNCTION TO SELECT USER IMAGE FROM GALLERY
    public void selectimage() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                userImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    //FUNCTION TO CHECK NAME OF USER
    public static boolean onlyLettersSpaces(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isLetter(ch) || ch == ' ') {
                continue;
            }
            return true;
        }
        return false;
    }
    //FUNCTION TO SEND VERIFICATION EMAIL
    private void sendEmailVerification() {
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        saveUserInformation();

                    } else {
                        Toast.makeText(getApplicationContext(), "Verification unsuccessful try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void registerUser() {
        progressDialog.setMessage("Signing up....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendEmailVerification();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Unable to signup!Try Again",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}