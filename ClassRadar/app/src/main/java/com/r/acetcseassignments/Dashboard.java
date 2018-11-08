package com.r.acetcseassignments;

import android.app.Activity;
import android.app.ActivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    ListView listView;
    ArrayList<roomData> list;
    CustomAdapter adapter1;
    DatabaseReference databaseReference,databaseReference1;
    FirebaseUser firebaseUser;
    String uid;
    String tname;
    String s[];
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Transparent));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null,null, getResources().getColor(R.color.appbar));
        ((Activity)this).setTaskDescription(taskDescription);

        setContentView(R.layout.activity_dashboard);
        listView=findViewById(R.id.lv);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        uid=firebaseUser.getUid();
        list = new ArrayList<>();
        adapter1 = new CustomAdapter(this, list);
        listView.setAdapter(null);
        progressBar=findViewById(R.id.progressBar);

        textView=findViewById(R.id.noRoomJoined);
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Teachers").child(uid);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JOINED").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressBar.setVisibility(View.VISIBLE);
                    list.clear();


                        String id = dataSnapshot.getKey().toString();

                        String room = dataSnapshot.getValue().toString();
                        s=room.split("@",2);
                        final roomData[] roomData = new roomData[1];
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                tname=dataSnapshot.child("name").getValue().toString();
                                if(s[0].equals("L")) {
                                    roomData[0] = new roomData(s[1], tname, "lab_not");
                                }
                                else{
                                    roomData[0] = new roomData(s[1], tname, "class_not");
                                }
                                list.add(roomData[0]);
                                listView.setAdapter(adapter1);
                                progressBar.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });





                }
                else {
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp = (TextView) view.findViewById(R.id.className);
                String str = temp.getText().toString();
                if(s[0].equals("L")){
                    databaseReference1 = FirebaseDatabase.getInstance().getReference().child("LAB").child(str);
                }
                else if(s[0].equals("C")){
                    databaseReference1 = FirebaseDatabase.getInstance().getReference().child("CLASS").child(str);

                }
                databaseReference = FirebaseDatabase.getInstance().getReference().child("JOINED").child(uid);
                databaseReference.removeValue();
                databaseReference1.setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Done!",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_SHORT).show();

                        }

                    }
                });


            }
        });

    }

}



