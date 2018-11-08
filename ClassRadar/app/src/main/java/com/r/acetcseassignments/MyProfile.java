package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class MyProfile extends AppCompatActivity  implements View.OnClickListener{

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    TextView name,email,rollno,batch,type;
    private ImageView userImage;

    private ProgressDialog progressDialog;
    private TextView changeProfilePhoto;
    private StorageReference storageReference;

    String imgUrl;
    ImageView more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Trans));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_my_profile);
        name=findViewById(R.id.name);
        rollno=findViewById(R.id.rollno);
        email=findViewById(R.id.email);
        batch=findViewById(R.id.batch);
        type=findViewById(R.id.type);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);
        progressDialog= new ProgressDialog(this);
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        userImage=findViewById(R.id.userimage_account_settings);
        changeProfilePhoto=findViewById(R.id.changeProfilePhoto);
        more=findViewById(R.id.more);
        more.setOnClickListener(this);
        changeProfilePhoto.setOnClickListener(this);
        storageReference= FirebaseStorage.getInstance().getReference().child("Student_Images");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Students").child(firebaseUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name.setText(Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString());
                rollno.setText("Roll no. - "+Objects.requireNonNull(dataSnapshot.child("rollNumber").getValue()).toString());
                batch.setText("Batch - "+Objects.requireNonNull(dataSnapshot.child("batch").getValue()).toString());
                email.setText(Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString());
                type.setText("Type - "+Objects.requireNonNull(dataSnapshot.child("type").getValue()).toString());
                Picasso.get().load(Objects.requireNonNull(dataSnapshot.child("imgUrl").getValue()).toString()).transform(new CircleTransform()).into(userImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //OVERRIDING ONCLICKLISTENER
    @Override
    public void onClick(View v) {

        if(v==changeProfilePhoto){

            selectimage();


        }
        if(v==more){
            PopupMenu popup = new PopupMenu(this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.setting_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.password:
                            startActivity(new Intent(getApplicationContext(),ChangePassword.class));
                            return true;



                        default:
                            return false;

                    }
                }


            });
            popup.show();

        }

    }
    //SELECT IMAGE FROM GALLERY
    public void selectimage(){
        Intent i=new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Uri resultUri;
        if(requestCode==12&&resultCode==RESULT_OK&&data!=null){
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //CHANGING IMAGE
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                progressDialog.setMessage("Saving....");
                progressDialog.show();
                Picasso.get().load(resultUri).transform(new CircleTransform()).into(userImage);
                databaseReference= FirebaseDatabase.getInstance().getReference().child("Students").child(firebaseUser.getUid());
                final StorageReference sr=storageReference.child(firebaseUser.getUid()+".jpg");
                sr.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                imgUrl=uri.toString();
                                databaseReference.child("imgUrl").setValue(imgUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(),"Profile Photo Updated",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        startActivity(new Intent(getApplicationContext(),MainActivityStudent.class));
                                    }
                                });



                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.i("log","log");

            }
        }
    }
    //FUNCTION TO CHECK NAME
    public static boolean onlyLettersSpaces(String s){
        for(int i=0;i<s.length();i++){
            char ch = s.charAt(i);
            if (Character.isLetter(ch) || ch == ' ') {
                continue;
            }
            return true;
        }
        return false;
    }



}
